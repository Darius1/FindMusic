package personal.darius.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import personal.darius.data.Song;
import personal.darius.dataStructures.ArrayList;

public class SongStorage {
	
	public void addSongsToDatabase(ArrayList<Song> songList) {
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
				String createTable = "CREATE TABLE IF NOT EXISTS songs (SongID int NOT NULL, Title varchar(100) UNIQUE, Artist varchar(100), ReleaseDate varchar(100))";
				System.out.println("The SQL command is: " + createTable + "\n");
				
				statement.execute(createTable);
				
				// Show what's in the songs table
				displaySongTable(statement);
				
				if (songList.isEmpty()) {
					System.out.println("There are no songs that can be added.");
					System.exit(0);
				}
				
				// Insert all of the songs searched today into the songs table
				for (int i = 0; i < songList.size(); i++) {
					try {
						statement.execute(insertSong(songList.get(i), i + 1));
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("An error occurred while adding your song.");
						break;
					}
				}
				
				
				// Display the songs table again
				displaySongTable(statement);
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Inserts a song into the MySQL song database
	 *
	 * @param song The Song to add to the database
	 * @param id The value the SongID field will take. The SongID will also be updated to this 
	 * value if a duplicate occurs.
	 * 
	 * @return the query as a string
	 */
	private String insertSong(Song song, int id) {
		
		/* If the user runs this program multiple times on the same day, I don't want duplicates 
		 * to appear in the table. Song titles must be unique for a new song to be added to the
		 * table. If a duplication occurs, the SongID is just reset to what it was prior to the
		 * duplication.
		 */
		return "INSERT INTO songs (SongID, Title, Artist, ReleaseDate) VALUES (" + id + ", " + "\"" + song.getTitle() + "\", \"" + song.getArtist() + "\", \"" + song.getReleaseDate() + "\") ON DUPLICATE KEY UPDATE SongID=" + id;
	}
	
	/**
	 * Shows the contents of the Songs MySQL table
	 * 
	 * @param statement the Statement object
	 */
	private void displaySongTable(Statement statement) {
		// Show the current state of the table
		// Order based off song id to put the list in order of when a song was released
		String showTable = "SELECT * FROM songs ORDER BY SongID ASC";
		System.out.println("The SQL command is: " + showTable + "\n");
		
		try {
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
			
		} catch (SQLException e) {
			System.out.println("An error occurred when displaying the table.");
		}
	}
}
