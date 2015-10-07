package com.cloudwick.hadoop.Avro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.avro.example.Employee;

public class WriteToAvroFromText {

	private static BufferedReader bufferedReader;
	private static List<Employee> listEmployees;

	public static void main(String[] args) {
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
			listEmployees = new ArrayList<Employee>();
			bufferedReader = new BufferedReader(new FileReader("Employees.txt"));
			while ((strLineRead = bufferedReader.readLine()) != null) {
				// User user1 = new User();
				// user1.setName("Alyssa");
				// user1.setFavoriteNumber(256);
				// // Leave favorite color null
				//
				// // Alternate constructor
				// User user2 = new User("Ben", 7, "red");
				//
				// // Construct via builder
				// User user3 = User.newBuilder().setName("Charlie")
				// .setFavoriteColor("blue").setFavoriteNumber(null)
				// .build();

				String empRrcordArray[] = strLineRead.split(",");
				if (empRrcordArray.length == 8) {
					Employee employee = Employee
							.newBuilder()
							.setId(Integer.parseInt(empRrcordArray[0].trim()))
							.setName(empRrcordArray[1])
							.setDesignation(empRrcordArray[2])
							.setMgrid(
									Integer.parseInt(empRrcordArray[3].trim()))
							.setHiredate(empRrcordArray[4])
							.setSalary(
									Double.parseDouble(empRrcordArray[5].trim()))
							.setCommission(
									Double.parseDouble(empRrcordArray[6].trim()))
							.setDeptid(
									Integer.parseInt(empRrcordArray[7].trim()))
							.build();
					listEmployees.add(employee);
				}

			}
			if (listEmployees != null)
				writeToAvro();
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
			DatumWriter<Employee> empDatumWriter = new SpecificDatumWriter<Employee>(
					Employee.class);
			DataFileWriter<Employee> dataFileWriter = new DataFileWriter<Employee>(
					empDatumWriter);
			dataFileWriter.create(Employee.getClassSchema(), new File(
					"employees.avro"));
			for (Employee employee : listEmployees) {

				dataFileWriter.append(employee);

			}

			dataFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
