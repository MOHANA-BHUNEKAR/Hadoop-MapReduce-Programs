package com.cloudwick.hadoop.SecondarySort;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinReducer extends Reducer<CompositeKeyWritable, Text, Text, Text>{
	@Override
	protected void reduce(
			CompositeKeyWritable key,
			Iterable<Text> value,
			Reducer<CompositeKeyWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//super.reduce(arg0, arg1, arg2);
	 	 
		String deptName= value.iterator().next().toString();
		while(value.iterator().hasNext()){
			 
			context.write( new Text(value.iterator().next().toString()),new Text(deptName));	
		}
		
	}
}
