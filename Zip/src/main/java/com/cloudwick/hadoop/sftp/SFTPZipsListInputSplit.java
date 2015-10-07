package com.cloudwick.hadoop.sftp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException; 

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class SFTPZipsListInputSplit extends InputSplit implements Writable{
	private String sourceName;

	public SFTPZipsListInputSplit() {
		// TODO Auto-generated constructor stub
	}
	
	public SFTPZipsListInputSplit(String source){
		this.sourceName = source;
	}
	
	
	
	@Override
	public long getLength() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return sourceName.length();
	}

	/*Source is at single location so the 1st index contains the source name */
	@Override
	public String[] getLocations() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new String []{
				getSource()
		};
	}
	
	public String getSource(){
		return sourceName;
	}

	public void readFields(DataInput input) throws IOException {
		// TODO Auto-generated method stub
		this.sourceName = input.readUTF();
		
	}

	public void write(DataOutput output) throws IOException {
		// TODO Auto-generated method stub
		output.writeUTF(sourceName);
	}
	
	
	

}
