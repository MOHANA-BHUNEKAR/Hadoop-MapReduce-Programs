package com.cloudwick.hadoop.AvroNoCodeGen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter; 
import org.apache.avro.specific.SpecificDatumWriter;
 

public class WriteToAvroFromText {
	private static BufferedReader bufferedReader;
	private static List<GenericRecord> listEmployees;
	private static Schema schema;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			readTextFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readTextFile() throws IOException {
		String strLineRead = "";
		try { 
			bufferedReader = new BufferedReader(new FileReader("Employees.txt"));
			listEmployees =new ArrayList<GenericRecord>();
			schema = new Schema.Parser().parse(new File("src/main/avro/avroSample.avsc"));  
			while ((strLineRead = bufferedReader.readLine()) != null) { 

				String empRrcordArray[] = strLineRead.split(",");
				if (empRrcordArray.length == 8) {
					GenericRecord employee = new GenericData.Record(schema);
					employee.put("id", Integer.parseInt(empRrcordArray[0].trim()));
					employee.put("name", empRrcordArray[1]);
					employee.put("designation", empRrcordArray[2]);
					employee.put("mgrid",Integer.parseInt(empRrcordArray[3].trim()));
					employee.put("hiredate",empRrcordArray[4]);
					employee.put("salary", Double.parseDouble(empRrcordArray[5].trim()));
					employee.put("commission",Double.parseDouble(empRrcordArray[6].trim()));
					employee.put("deptid", Integer.parseInt(empRrcordArray[7].trim()));
					listEmployees.add(employee);
					System.out.println("read");
					System.out.println(employee);
				}
				if (listEmployees != null)
					writeToAvro();
			} 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}
	
	public static void writeToAvro() {
		try {
			File file = new File("employees.avro");
			DatumWriter<GenericRecord> empDatumWriter = new SpecificDatumWriter<GenericRecord>(schema);
			DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(
					empDatumWriter);
			dataFileWriter.create(schema, new File(
					"employees.avro"));
			for (GenericRecord employee : listEmployees) {
				System.out.println("Write");
				System.out.println(employee);
				dataFileWriter.append(employee);

			}

			dataFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
