package com.cloudwick.hadoop.SecondarySort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EmpMapper extends Mapper<LongWritable, Text, CompositeKeyWritable, Text>{
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, CompositeKeyWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.map(key, value, context);
		String[] empValues = value.toString().split(",");
		CompositeKeyWritable compositeKeyWritable = new CompositeKeyWritable(empValues[2], 1);
		context.write(compositeKeyWritable, new Text(empValues[0]+","+empValues[1]));
	}
}
