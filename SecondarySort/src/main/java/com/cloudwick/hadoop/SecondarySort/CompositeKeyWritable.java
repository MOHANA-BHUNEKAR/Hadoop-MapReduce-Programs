package com.cloudwick.hadoop.SecondarySort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class CompositeKeyWritable implements 
		WritableComparable<CompositeKeyWritable> {
	private String deptId;
	private int flag; // value 1 for emp and 0 for dept

	public String getDeptId() {
		return deptId;
	}

	public void setDeptNId(String deptId) {
		this.deptId = deptId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public CompositeKeyWritable() {
	}

	public CompositeKeyWritable(String deptId, int flag) {
		this.deptId = deptId;
		this.flag = flag;
	}

	@Override
	public String toString() {
		return (new StringBuilder().append(deptId).append(",").append(flag))
				.toString();
	}

	public void readFields(DataInput dataInput) throws IOException {
		// TODO Auto-generated method stub
		deptId = WritableUtils.readString(dataInput);
		flag = WritableUtils.readVInt(dataInput);
	}

	public void write(DataOutput dataOutput) throws IOException {
		// TODO Auto-generated method stub
		WritableUtils.writeString(dataOutput, deptId);
		WritableUtils.writeVInt(dataOutput, flag);
	}
	
	public int compareTo(CompositeKeyWritable objKeyPair) {
		// TODO Auto-generated method stub
		int result = deptId.compareTo(objKeyPair.deptId);
		if (result == 0) {
			result = flag - objKeyPair.flag;
		}
		return result;
	}

}
