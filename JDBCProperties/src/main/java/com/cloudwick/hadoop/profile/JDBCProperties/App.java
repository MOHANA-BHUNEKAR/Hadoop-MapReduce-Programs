package com.cloudwick.hadoop.profile.JDBCProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
 

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException, SQLException {
		FileInputStream fileInput =null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			
			File file = new File("\target\\classes\\database.properties"); 
			fileInput = new FileInputStream(file);
			Properties properties = new Properties(); 
			properties.load(fileInput);
			

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				System.out.println(key + ": " + value);
			}

			String username = properties.getProperty("db.username").trim();
			String password = properties.getProperty("db.password").trim();
			String url = properties.getProperty("db.url").trim();
			String driver = properties.getProperty("db.driver").trim();

			System.out
					.println("-------- MySQL JDBC Connection Testing ------------");

			Class.forName(driver);
			System.out.println("MySQL JDBC Driver Registered!"); 
			connection = DriverManager.getConnection(url, username, password);

			if (connection != null) {
				System.out
						.println("You made it, take control your database now!");
			} else {
				System.out.println("Failed to make connection!");
			}
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Addresses");
			while (resultSet.next()) {
				String zip = resultSet.getString("zip");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				System.out.println("zip: " + zip + ", city: " + city
						+ ", state: " + state);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace(); 
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(fileInput!=null)
				fileInput.close();
			if(connection!=null)
				connection.close();
			if(statement!=null)
				statement.close();
			if(resultSet!=null)
				resultSet.close();
		}
	}
}
