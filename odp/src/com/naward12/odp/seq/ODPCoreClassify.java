package com.naward12.odp.seq;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.mahout.classifier.naivebayes.AbstractNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.vectorizer.TFIDF;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

public class ODPCoreClassify {
	
	public final static String MODEL_PATH_CONF = "modelPath";
	public final static String DICTIONARY_PATH_CONF = "dictionaryPath";
	public final static String DOCUMENT_FREQUENCY_PATH_CONF = "documentFrequencyPath";
	public final static String LABEL_INDEX_PATH_CONF = "labelIndexPath";
	
	private static StandardNaiveBayesClassifier classifier;
	private static Map<String, Integer> dictionary;
	private static Map<Integer, Long> documentFrequency;
	private static Analyzer analyzer;
	private static Map<Integer, String> labels;
	
	public ODPCoreClassify(Configuration configuration) throws IOException {
		String modelPath = configuration.getStrings(MODEL_PATH_CONF)[0];
		String dictionaryPath = configuration.getStrings(DICTIONARY_PATH_CONF)[0];
		String documentFrequencyPath = configuration.getStrings(DOCUMENT_FREQUENCY_PATH_CONF)[0];
		String labelIndexPath = configuration.getStrings(LABEL_INDEX_PATH_CONF)[0];
		
		dictionary = readDictionnary(configuration, new Path(dictionaryPath));
		documentFrequency = readDocumentFrequency(configuration, new Path(documentFrequencyPath));
		
		labels = BayesUtils.readLabelIndex(configuration, new Path(labelIndexPath));

		// analyzer used to extract word from tweet
		analyzer = new StandardAnalyzer(Version.LUCENE_43);
		
		NaiveBayesModel model = NaiveBayesModel.materialize(new Path(modelPath), configuration);
		
		 classifier = new StandardNaiveBayesClassifier(model);
	}
	
	public String classify(String text) throws IOException {
		int documentCount = documentFrequency.get(-1).intValue();

		Multiset<String> words = ConcurrentHashMultiset.create();
		
		// extract words from tweet
		TokenStream ts = analyzer.tokenStream("text", new StringReader(text));
		CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
		ts.reset();
		int wordCount = 0;
		while (ts.incrementToken()) {
			if (termAtt.length() > 0) {
				String word = ts.getAttribute(CharTermAttribute.class).toString();
				Integer wordId = dictionary.get(word);
				// if the word is not in the dictionary, skip it
				if (wordId != null) {
					words.add(word);
					wordCount++;
				}
			}
		}
		ts.end();
		ts.close();

		// create vector wordId => weight using tfidf
		Vector vector = new RandomAccessSparseVector(100000);
		TFIDF tfidf = new TFIDF();
		for (Multiset.Entry<String> entry:words.entrySet()) {
			String word = entry.getElement();
			int count = entry.getCount();
			Integer wordId = dictionary.get(word);
			Long freq = documentFrequency.get(wordId);
			double tfIdfValue = tfidf.calculate(count, freq.intValue(), wordCount, documentCount);
			vector.setQuick(wordId, tfIdfValue);
		}
		// With the classifier, we get one score for each label 
		// The label with the highest score is the one the tweet is more likely to
		// be associated to
		Vector resultVector = classifier.classifyFull(vector);
		double bestScore1 = -Double.MAX_VALUE;
		double bestScore2 = -Double.MAX_VALUE;
		double bestScore3 = -Double.MAX_VALUE;
		int bestCategoryId1 = -1;
		int bestCategoryId2 = -1;
		int bestCategoryId3 = -1;
		for(Element element: resultVector.all()) {
			int categoryId = element.index();
			double score = element.get();
			if (score > bestScore1) {
				bestScore1 = score;
				bestCategoryId1 = categoryId;
			}
		}
		
		for(Element element: resultVector.all()) {
			int categoryId = element.index();
			double score = element.get();
			if (score > bestScore2 && score < bestScore1) {
				bestScore2 = score;
				bestCategoryId2 = categoryId;
			}
		}
		
		for(Element element: resultVector.all()) {
			int categoryId = element.index();
			double score = element.get();
			if (score > bestScore3 && score < bestScore2) {
				bestScore3 = score;
				bestCategoryId3 = categoryId;
			}
		}
		
		String bestCategoryName1 = labels.get(bestCategoryId1);
		String bestCategoryName2 = labels.get(bestCategoryId2);
		String bestCategoryName3 = labels.get(bestCategoryId3);
		
		String finalClassifyOutput = bestCategoryName1 + ":" + String.valueOf(bestScore1) + '\t' + bestCategoryName2 + ":" + String.valueOf(bestScore2) + '\t' + bestCategoryName3 + ":" +  String.valueOf(bestScore3);

		return finalClassifyOutput;
	}
	
	private static Map<String, Integer> readDictionnary(Configuration conf, Path dictionnaryPath) {
		Map<String, Integer> dictionnary = new HashMap<String, Integer>();
		for (Pair<Text, IntWritable> pair : new SequenceFileIterable<Text, IntWritable>(dictionnaryPath, true, conf)) {
			dictionnary.put(pair.getFirst().toString(), pair.getSecond().get());
		}
		return dictionnary;
	}

	private static Map<Integer, Long> readDocumentFrequency(Configuration conf, Path documentFrequencyPath) {
		Map<Integer, Long> documentFrequency = new HashMap<Integer, Long>();
		for (Pair<IntWritable, LongWritable> pair : new SequenceFileIterable<IntWritable, LongWritable>(documentFrequencyPath, true, conf)) {
			documentFrequency.put(pair.getFirst().get(), pair.getSecond().get());
		}
		return documentFrequency;
	}

}
