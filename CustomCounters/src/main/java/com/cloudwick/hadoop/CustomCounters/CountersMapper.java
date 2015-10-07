package com.cloudwick.hadoop.CustomCounters;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CountersMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);
		if(value.toString().contains(DriverManager.LOG_COUNTERS.INFO.name()))
			context.getCounter(DriverManager.LOG_COUNTERS.INFO).increment(1);
		else if(value.toString().contains(DriverManager.LOG_COUNTERS.DEBUG.name()))
			context.getCounter(DriverManager.LOG_COUNTERS.DEBUG).increment(1);
		else if(value.toString().contains(DriverManager.LOG_COUNTERS.EXCEPTION.name()))
			context.getCounter(DriverManager.LOG_COUNTERS.EXCEPTION).increment(1);
	}
}
