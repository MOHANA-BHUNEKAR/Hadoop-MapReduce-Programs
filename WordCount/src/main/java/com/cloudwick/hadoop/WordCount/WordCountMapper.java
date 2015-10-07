package com.cloudwick.hadoop.WordCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		// super.map(key, value, context);
		// context.write(key, value);

		String line = value.toString();
		String[] arrayLetters = line.split(" "); 
		
		IntWritable intConstant = new IntWritable(1);
		for (String word : arrayLetters) {
			context.write(new Text(word),intConstant);
		}
	}
}
