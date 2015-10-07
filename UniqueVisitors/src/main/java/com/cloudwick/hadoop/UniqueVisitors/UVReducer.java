package com.cloudwick.hadoop.UniqueVisitors;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class UVReducer extends Reducer<Text, Text, Text, IntWritable>{
	@Override
	protected void reduce(Text key, Iterable<Text> value,
			Reducer<Text, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.reduce(arg0, arg1, arg2);
		
		//Creating set enables duplicate users to be shown as a single user (sets does not allow duplicates)
		Set logSet = new HashSet();
		
		//value is iterable containing value of users may contain duplicates. To avoid duplicates we using set to get unique users
		for (Text text : value) {
			logSet.add(text);
		}
		context.write(key, new IntWritable(logSet.size()));
	}
	
}
