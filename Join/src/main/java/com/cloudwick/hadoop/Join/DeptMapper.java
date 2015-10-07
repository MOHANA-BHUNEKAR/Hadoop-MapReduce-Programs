package com.cloudwick.hadoop.Join;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DeptMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);

		// ex record: 101,sales (deptId,deptName)
		String[] deptValues = value.toString().split(","); 
		context.write(new IntWritable(Integer.parseInt(deptValues[0]) ) , new Text(deptValues[1]));
	}
}
