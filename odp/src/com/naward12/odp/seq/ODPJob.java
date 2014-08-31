package com.naward12.odp.seq;

import nl.surfsara.warcutils.WarcSequenceFileInputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class ODPJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = this.getConf();
		
		String modelPath = args[0];
		String dictionaryPath = args[1];
		String documentFrequencyPath = args[2];
		String labelIndexPath = args[3];
		String inputPath = args[4];
		String outputPath = args[5];
		
		conf.setStrings(ODPCoreClassify.MODEL_PATH_CONF, modelPath);
		conf.setStrings(ODPCoreClassify.DICTIONARY_PATH_CONF, dictionaryPath);
		conf.setStrings(ODPCoreClassify.DOCUMENT_FREQUENCY_PATH_CONF, documentFrequencyPath);
		conf.setStrings(ODPCoreClassify.LABEL_INDEX_PATH_CONF, labelIndexPath);

		Job job = Job.getInstance(conf, "ODP Classify");
		job.setJarByClass(ODPJob.class);

		FileInputFormat.addInputPath(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));

		job.setMapperClass(ODPClassifier.class);
		job.setReducerClass(Reducer.class);
		job.setInputFormatClass(WarcSequenceFileInputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// Execute job and return status
		return job.waitForCompletion(true) ? 0 : 1;

	}


}
