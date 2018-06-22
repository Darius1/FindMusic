package personal.darius.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SongStorage {
	public static void main(String[] args) {
		
		try (			
			// Establish a connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false", "darius", "xxxx");
				
			Statement statement = connection.createStatement();
			) {
				
			// Create the SQL database that will hold the songs if it doesn't exist already
			String createDatabase = "CREATE DATABASE IF NOT EXISTS songStorage";
			System.out.println("The SQL command is: " + createDatabase + "\n");
			
			// Execute the command
			statement.execute(createDatabase);
			
			// Switch to the songStorage database if hasn't be selected already
			String useDatabase = "USE songStorage";
			System.out.println("The SQL command is: " + useDatabase + "\n");
			
			statement.execute(useDatabase);
			
			// Create the SQL table of songs if it doesn't already exist
			String createTable = "CREATE TABLE IF NOT EXISTS songs (SongID int NOT NULL AUTO_INCREMENT, title varchar(100), artist varchar(100), PRIMARY KEY (SongID))";
			System.out.println("The SQL command is: " + createTable + "\n");
			
			statement.execute(createTable);
			
			// Show the current state of the table
			String showTable = "SELECT * FROM songs";
			System.out.println("The SQL command is: " + showTable + "\n");
			
			// Store the results of the select query
			ResultSet initialTable = statement.executeQuery(showTable);
			int recordCount = 0;
			
			// Display all of the current songs contained in the songs table
			while (initialTable.next()) {
				int songID = initialTable.getInt("SongID");
				String songTitle = initialTable.getString("title");
				String songArtist = initialTable.getString("artist");
				
				System.out.println(songID + " " + songTitle + " " + songArtist);
				recordCount++;
			}
			
			if (recordCount == 0) {
				System.out.println("The Songs table is currently empty.");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
