package com.cloudwick.hadoop.Avro;
 
import java.io.BufferedWriter;
import java.io.File; 
import java.io.FileWriter;
import java.io.IOException; 

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.avro.example.Employee;

public class WriteToTextFromAvro {
	private static BufferedWriter bufferedWriter; 

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
		try {
			// Deserialize Employees from disk
			DatumReader<Employee> empDatumReader = new SpecificDatumReader<Employee>(
					Employee.class);
			DataFileReader<Employee> dataFileReader = new DataFileReader<Employee>(
					new File("employees.avro"), empDatumReader);
			Employee employee = null;
			//listEmployees = new ArrayList<Employee>();
			bufferedWriter = new BufferedWriter(new FileWriter("EmployeesOut.txt"));
			while (dataFileReader.hasNext()) {
				// Reuse user object by passing it to next(). This saves us from
				// allocating and garbage collecting many objects for files with
				// many items.

				employee = dataFileReader.next(employee); 
				bufferedWriter.write(employee.id+","+employee.name+","+employee.designation+","+employee.mgrid+","+employee.hiredate+","+employee.salary+","+employee.commission+","+employee.deptid+"\n");
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
