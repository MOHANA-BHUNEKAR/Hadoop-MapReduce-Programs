package com.cloudwick.hadoop.AvroNoCodeGen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;  

public class WriteToTextFromAvro {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ReadAvroWriteText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ReadAvroWriteText() throws IOException {
		BufferedWriter bufferedWriter = null;
		try {
			Schema schema = new Schema.Parser().parse(new File("src/main/avro/avroSample.avsc")); 
			// Deserialize Employees from disk
			DatumReader<GenericRecord> empDatumReader =  new GenericDatumReader<GenericRecord>(schema);
			DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(
					new File("employees.avro"), empDatumReader);
			GenericRecord employee = null; 
			bufferedWriter = new BufferedWriter(new FileWriter("EmployeesOut.txt"));
			while (dataFileReader.hasNext()) {
				// Reuse user object by passing it to next(). This saves us from
				// allocating and garbage collecting many objects for files with
				// many items.
				employee = dataFileReader.next(employee); 
				bufferedWriter.write(employee.get("id")+","+employee.get("name")+","+employee.get("designation")+","+employee.get("mgrid")
						+","+employee.get("hiredate")+","+employee.get("salary")+","+employee.get("commission")+","+employee.get("deptid")+"\n");
			} 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(bufferedWriter!=null)
				bufferedWriter.close();
		}
	}

}
