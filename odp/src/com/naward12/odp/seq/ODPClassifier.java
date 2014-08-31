package com.naward12.odp.seq;

import java.io.IOException;
import org.apache.commons.io.IOUtils;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attribute;
import org.jsoup.select.Elements;
import org.jwat.common.HttpHeader;
import org.jwat.common.Payload;
import org.jwat.warc.WarcRecord;


class ODPClassifier extends Mapper<LongWritable, WarcRecord, Text, Text> {
	
	
	
	private static enum Counters {
		CURRENT_RECORD, NUM_HTTP_RESPONSE_RECORDS
	}
	
	private static ODPCoreClassify myODPclassifier;

	@Override
	protected void setup(Context context) throws IOException {
		initClassifier(context);
	}

	private static void initClassifier(Context context) throws IOException {
		if (myODPclassifier == null) {
			myODPclassifier = new ODPCoreClassify(context.getConfiguration());
		}
	}
	
	
	
	@Override
	public void map(LongWritable key, WarcRecord value, Context context) throws IOException, InterruptedException {
		context.setStatus(Counters.CURRENT_RECORD + ": " + key.get());

		// Only process http response content. Note that the outlinks can also be found in the wat metadata.
		if ("application/http; msgtype=response".equals(value.header.contentTypeStr)) {
			// org.jwat.warc.WarcRecord is kind enough to also parse http headers for us:
			HttpHeader httpHeader = value.getHttpHeader();
			if (httpHeader == null) {
				// No header so we are unsure that the content is text/html: NOP
			} else {
				if (httpHeader.contentType != null && httpHeader.contentType.contains("text/html")) {
					// Note that if you really want to do this right; you should look at the character encoding as well.
					// We'll leave that as an exercise for you ;-).
					context.getCounter(Counters.NUM_HTTP_RESPONSE_RECORDS).increment(1);
					// Get the html payload
					Payload payload = value.getPayload();
					if (payload == null) {
						// NOP
					} else {
						String warcContent = IOUtils.toString(payload.getInputStreamComplete());
						if (warcContent == null && "".equals(warcContent)) {
							// NOP
						} else {
							String targetURI = value.header.warcTargetUriStr;
							Document doc = Jsoup.parse(warcContent);
							
							doc.select("script, style, .hidden").remove();

							// Remove all style and event-handler attributes from all elements.
							Elements all = doc.select("*");
							for (org.jsoup.nodes.Element el : all) { 
							  for (Attribute attr : el.attributes()) { 
							    String attrKey = attr.getKey();
							    if (attrKey.equals("style") || attrKey.startsWith("on")) { 
							      el.removeAttr(attrKey);
							    } 
							  }
							}
							
							String body = doc.text();
							String title = doc.title();
							String htmlContent = title + body;
							if (htmlContent != null && !("".equals(htmlContent))) {
								String bestCategories = myODPclassifier.classify(htmlContent);
								
								context.write(new Text(targetURI), new Text(bestCategories));
							}
						}
					}
				}
			}
		}
	}
}
