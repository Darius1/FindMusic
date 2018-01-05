import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import edu.ncsu.csc216.get_outdoors.util.SortedArrayList;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * This class will handle all of the search functionality in the FindMusic program
 */
public class Search {
    /** Holds the artist name */
    private String artist;
    /** Holds the website url that will be searched */
    private String website;
    /** Holds the song title */
    private String song;
    /** Holds all of the scraped songs and sorts them alphabetically */
    private SortedArrayList<String> sortedSongs;
    
    private CommandLine cmd;


    /**
     * Search object constructor that takes in the artist name as a parameter
     * @param artist the artist's name
     * @param
     */
    public Search(String artist) {
        this.artist = artist;
        website = "";
        song = "";
        sortedSongs = new SortedArrayList<String>();
    }
    
    public Search(String[] args) {
    	cmd = null;
    	sortedSongs = new SortedArrayList<String>();
    	if (setupCLI(args)) {
    		parseInput(args);
    	}
    }

    /**
     * Searches a website using an artist's name and current date as parameters to find new music
     * released by the artist for that day
     * @param  artist the artist's name
     * @param  website the website url
     * @param  date the date
     * @return true if the artist released music on that date, false otherwise
     */
    public boolean searchForRelease(String artist, String website) {
        try {
            Document doc = Jsoup.connect(website).get();
            Elements findSong = doc.select("div.cover-title");
            Elements findArtist = doc.select("div.dailySongChart-artist");
            int numberOfSongsToday = 0;

            System.out.printf("%5s", "Song " + "\t\t\t|\t\tArtist\n");
            System.out.println("----------------------------------------------");

            for (int i = 0; i < findSong.size(); i++) {

            	if (findArtist.get(i).text().contains(artist)) {
            		System.out.println(findSong.get(i).text() + " | " + findArtist.get(i).text());
            		numberOfSongsToday++;
            	}
            }

            System.out.println("\nNumber of songs " + artist + " released today: " + numberOfSongsToday);

            if (numberOfSongsToday == 0) {
        		System.out.println("The artist you selected has not released any music today.");
        	}
            
            return true;
        } catch (IOException e) {
            System.out.println("There was an error fetching your website.");
            return false;
        }
    }

    /**
     * Returns the artist's name
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Returns the website name
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Returns the song title
     * @return song
     */
    public String getSong() {
        return song;
    }

    /**
     * Creates a string in the format "Artist: Song"
     * @return string
     */
    public String toString() {
        return artist + ": " + song;
    }
    
    /**
     * Searches the specified website for songs that were released today and the previous day
     * @param website the website being searched
     */
    public void findNewSongs(String website) {
    	int songsReleasedToday = 0;
    	boolean checkedYesterday = false;
    	boolean dateChecked = false;
    	
    	try {
			Document doc = Jsoup.connect(website).get();
			Elements findSong = doc.select(".dailySongChart-item");

			songsReleasedToday = findSong.size();
			
			for (int i = 0; i < songsReleasedToday; i++) {
				
				// Example formatted date: Monday Jan 1, 2000		
				LocalDate date = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
				String currentDate = date.format(formatter);

				// HotNewHipHop places the dailySongChart-day-date class tag on the very first song posted each day 
				// Need to check this tag to get the date information
				if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && !checkedYesterday) {
					if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(currentDate)) {
						System.out.println("Songs released on " + currentDate);
						System.out.println();
						System.out.println("Artist: " + findSong.get(i).getElementsByClass("dailySongChart-artist").text());
						System.out.println("Song: " + findSong.get(i).getElementsByClass("cover-title").text());
						System.out.println();
						dateChecked = true;
					} else {
						if (!dateChecked) {
							System.out.println("no songs released today.");
						}
						
						System.out.println("Check songs from yesterday " + yesterday() + " (y/n)?");
						Scanner scan = new Scanner(System.in);
						
						String choice = scan.next();
						choice = choice.toLowerCase();
						
						
						while (!choice.startsWith("n")) {
							while (!choice.startsWith("y") && !choice.startsWith("n")) {
								System.out.println("Please enter either yes or no.");
								System.out.println("Check songs from yesterday " + yesterday() + " (y/n)?");
								
								choice = scan.next();
								choice = choice.toLowerCase();
								
								if (choice.startsWith("n")) {
									//System.exit(1);
									break;
								}
							}
							
							if (choice.startsWith("y")) {
								if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(yesterday())) {

									System.out.println("\nSongs released on " + yesterday());
									System.out.println();
									System.out.println("Artist: " + findSong.get(i).getElementsByClass("dailySongChart-artist").text());
									System.out.println("Song: " + findSong.get(i).getElementsByClass("cover-title").text());
									System.out.println();
									
									checkedYesterday = true;
									break;
								}
							}
						}
						scan.close();
						
						if (choice.startsWith("n")) {
							//System.exit(1);
							break;
						}
					}
				} else if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && checkedYesterday) {
					break;
				} else {
					System.out.println("Artist: " + findSong.get(i).getElementsByClass("dailySongChart-artist").text());
					System.out.println("Song: " + findSong.get(i).getElementsByClass("cover-title").text());
					System.out.println();
				}
				
			if (findSong.size() > 0) {
				sortedSongs.add(formattedSong(findSong.get(i)));
			}
				
			}
		} catch (IndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Fetches the songs released today and yesterday with no system output
     *
     * @param website the website the songs will be scraped from
     */
    public void findNewSongsNoPrompts(String website) {
    	int songsReleasedToday = 0;
    	boolean checkedYesterday = false;
    	boolean dateChecked = false;
    	
    	try {
			Document doc = Jsoup.connect(website).get();
			Elements findSong = doc.select(".dailySongChart-item");

			songsReleasedToday = findSong.size();
			
			for (int i = 0; i < songsReleasedToday; i++) {
				
				// Example formatted date: Monday Jan 1, 2000		
				LocalDate date = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
				String currentDate = date.format(formatter);

				// HotNewHipHop places the dailySongChart-day-date class tag on the very first song posted each day 
				// Need to check this tag to get the date information
				if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && !checkedYesterday) {
					if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(currentDate)) {
						dateChecked = true;
					} else {
						if (!dateChecked) {
							System.out.println("no songs released today.");
						}
					}
					if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(yesterday())) {
						checkedYesterday = true;
					}
				} else if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && checkedYesterday) {
					break;
				} else {
				}
				
			if (findSong.size() > 0) {
				sortedSongs.add(formattedSong(findSong.get(i)));
			}
				
			}
		} catch (IndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * Grabs the song and artist text from the element parameter
     *
     * @param element the part of the webpage the data will be gathered from
     * @return a String in the format "Artist: Song"
     */
    private String formattedSong(Element element) {
    	artist = element.getElementsByClass("dailySongChart-artist").text();
    	song = element.getElementsByClass("cover-title").text();
    	
		return toString();
	}

    /**
     * Gets yesterday's date and formats it in the form "Day of week Month Date, Year"
     * Example formatted date: Monday Jan 1, 2000
     *
     * @return a properly formatted date
     */
	private String yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        
        DateFormat dateFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");
        return dateFormat.format(yesterday);
    }
    
	/**
	 * Gets the sortedSongs arrayList
	 *
	 * @return the arrayList
	 */
    public SortedArrayList<String> getSortedSongs() {
    	return sortedSongs;
    }
    
    /**
     * Prints out songs in alphabetical order
     */
    public void printSongs() {
    	for (int i = 0; i < sortedSongs.size(); i++) {
    		System.out.println(i + 1 + ".\t" + sortedSongs.get(i));
    	}
    }
    
    /**
     * Initializes the command line interface
     *
     * @param args the arguments needed
     * @return true if the interface is created without error
     */
    public boolean setupCLI(String[] args) {
    	Options findMusicOptions = new Options();
    	
    	findMusicOptions.addOption("s", "search", false, "search for songs released today");
    	findMusicOptions.addOption("h", "help", false, "displays usage information");
    	findMusicOptions.addOption("a", "artist", true, "searches for newly released music by the specified artist");
    	findMusicOptions.addOption("v", "version", false, "displays the version information");
    	findMusicOptions.addOption("o", "order", false, "orders the released songs alphabetically and displays them");
    	
    	CommandLineParser parser = new DefaultParser();
    	
    	try {
    		cmd = parser.parse(findMusicOptions, args);
    		
    		// Prints the help message
    		if (cmd.hasOption("h")) {
    			throw new ParseException("");
    		}
    		
    		// Prevents the user from running the program with no arguments
    		if (cmd.getOptions().length == 0) {
    			throw new ParseException("");
    		}
    	} catch (ParseException e) {
    		HelpFormatter formatter = new HelpFormatter();
    		String footer = "\nPlease report issues at https://github.com/Darius1/FindMusic/issues";
    		
    		formatter.printHelp("Search", null, findMusicOptions, footer, true);
    		return false;
    	}
    	
    	return true;
    	
    }
    
    /**
     * Runs the program based off of the arguments provided
     *
     * @param args the command line arguments entered by the user
     */
    public void parseInput(String[] args) {
    	if (cmd.hasOption("s")) {
    		findNewSongs("http://www.hotnewhiphop.com");
    	} else if (cmd.hasOption("h")) {
    		//print help information
    	} else if (cmd.hasOption("a")) {
    		searchForRelease(args[1], "http://www.hotnewhiphop.com");
    	} else if (cmd.hasOption("v")) {
    		System.out.println("FindMusic Version 1.0 Initial Release");
    	} else if (cmd.hasOption("o")) {
    		findNewSongsNoPrompts("http://www.hotnewhiphop.com");
    		printSongs();
    	}
    }

    /**
     * Tests the functionality of the Search class
     * @param args not used
     */
    public static void main(String[] args) {
    	new Search(args);
    }
}
