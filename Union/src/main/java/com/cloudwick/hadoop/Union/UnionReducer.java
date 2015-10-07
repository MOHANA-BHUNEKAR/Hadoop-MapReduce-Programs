package com.cloudwick.hadoop.Union;

import java.io.IOException; 

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class UnionReducer extends Reducer<Text, Text, Text, Text>{
	@Override
	protected void reduce(Text key, Iterable<Text> value,
			Reducer<Text, Text, Text, Text>.Context context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		//super.reduce(arg0, arg1, arg2);  
		context.write(key,new Text(value.iterator().next().toString()));
	}

}
