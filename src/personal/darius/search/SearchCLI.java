package personal.darius.search;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import personal.darius.data.Song;
import personal.darius.dataStructures.ArrayList;
import personal.darius.sort.Sorter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * This class will handle all of the search functionality in the FindMusic program and will
 * present information as a CLI
 * 
 * @author Darius McFarland
 */
public class SearchCLI {
	
    /** Holds the website url that will be searched */
    @SuppressWarnings("unused")
	private String website;
    
    /** Holds all of the scraped songs and sorts them alphabetically */
    private ArrayList<Song> sortedSongs;
    
    /** Reads the arguments supplied by the user on the command line */
    private CommandLine cmd;
    
    private int testNumber;
    
    /**
     * Search constructor that will be used for the CLI
     *
     * @param args the command line arguments the user supplies
     */
    public SearchCLI(String[] args) {
    	cmd = null;
    	sortedSongs = new ArrayList<Song>();
    	testNumber = 0;
    	
    	if (setupCLI(args)) {
    		parseInput(args);
    	}
    }

    /**
     * Searches a website using an artist's name and current date as parameters to find new music
     * released by the artist for that day
     * @param  artist the artist's name
     * @param  website the website url
     * 
     * @return true if the artist released music on that date, false otherwise
     */
    public boolean searchForRelease(String artist, String website) {
    	Document doc = null;
    	
        try {
        	if (website.equals("testPage.html")) {
        		File input = new File(website);
    			doc = Jsoup.parse(input, "UTF-8", "http://hotnewhiphop.com/");
        	} else {
        		doc = Jsoup.connect(website).get();
        	}
            
            Elements findSong = doc.select("div.cover-title");
            Elements findArtist = doc.select("div.dailySongChart-artist");
            int numberOfSongsToday = 0;

            for (int i = 0; i < findSong.size(); i++) {
            	if (findArtist.get(i).text().contains(artist)) {
            		System.out.println("Song: " + findSong.get(i).text());
            		System.out.println("Artist: " + findArtist.get(i).text());
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
     * Searches the specified website for songs that were released today and the previous day
     * @param website the website being searched
     */
    public void findNewSongs(String website) {
    	int songsReleasedToday = 0;
    	boolean checkedYesterday = false;
    	boolean dateChecked = false;
    	Document doc = null;
    	String currentDate = "";
    	String choice = "";
    	
    	try {
    		if (website.equals("testPage.html")) {
    			File input = new File(website);
    			doc = Jsoup.parse(input, "UTF-8", "http://hotnewhiphop.com/");
    		} else {
    			doc = Jsoup.connect(website).get();
    		}
    		
			Elements findSong = doc.select(".dailySongChart-item");

			songsReleasedToday = findSong.size();
			
			for (int i = 0; i < songsReleasedToday; i++) {
				
				if (website.equals("testPage.html")) {
					currentDate = "Saturday Jul 28, 2018";
				} else {
					// Example formatted date: Monday Jan 1, 2000		
					LocalDate date = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
					currentDate = date.format(formatter);
				}

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
						
						if (testNumber == 1) {
							choice = "no";
						} else if (testNumber == 2) {
							choice = "yes";
						} else if (testNumber == 3) {
							choice = "what?";
						} else {
							choice = scan.next();
							choice = choice.toLowerCase();
						}

						while (!choice.startsWith("n")) {
							while (!choice.startsWith("y") && !choice.startsWith("n")) {
								System.out.println("Please enter either yes or no.");
								System.out.println("Check songs from yesterday " + yesterday() + " (y/n)?");
								
								if (testNumber == 3) {
									choice = "yes";
								} else {
									choice = scan.next();
									choice = choice.toLowerCase();
								}
								
								if (choice.startsWith("n")) {
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
					// Determines what the release date of the song should be
					if (checkedYesterday) {
						sortedSongs.insert(formattedSong(findSong.get(i), yesterday()));
					} else {
						sortedSongs.insert(formattedSong(findSong.get(i), currentDate));
					}
				}
				
			}
			
			// add the songs to the database
//			System.out.println("Adding to database!");
//			SongStorage database = new SongStorage();
//			database.addSongsToDatabase(sortedSongs);
			
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
    	Document doc = null;
    	String currentDate = "";
    	
    	try {
    		
			if (website.equals("testPage.html")) {
    			File input = new File(website);
    			doc = Jsoup.parse(input, "UTF-8", "http://hotnewhiphop.com/");
    		} else {
    			doc = Jsoup.connect(website).get();
    		}
			
			Elements findSong = doc.select(".dailySongChart-item");

			songsReleasedToday = findSong.size();
			
			for (int i = 0; i < songsReleasedToday; i++) {
				
				if (website.equals("testPage.html")) {
					currentDate = "Saturday Jul 28, 2018";
				} else {
					// Example formatted date: Monday Jan 1, 2000		
					LocalDate date = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
					currentDate = date.format(formatter);
				}
				

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
					if (checkedYesterday) {
						sortedSongs.insert(formattedSong(findSong.get(i), yesterday()));
					} else {
						sortedSongs.insert(formattedSong(findSong.get(i), currentDate));
					}
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
     * @param releaseDate the date the song first appeared in the search
     * @return a new Song object
     */
    private Song formattedSong(Element element, String releaseDate) {
    	String artist = element.getElementsByClass("dailySongChart-artist").text();
    	String title = element.getElementsByClass("cover-title").text();
    	
    	Song newSong = new Song(artist, title);
    	newSong.setReleaseDate(releaseDate);
    	
		return newSong;
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
        if (testNumber != 0) {
        	return "Friday Jul 27, 2018";
        }
        return dateFormat.format(yesterday);
    }
    
    /**
     * Prints out songs in alphabetical order
     */
    public void printSongs() {
    	Sorter<Song> sorter = new Sorter<Song>();
    	
    	sorter.alphabeticalSort(sortedSongs);
    	
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
    	findMusicOptions.addOption("t", "test", false, "tests all of the SearchCLI methods");
    	
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
    	} else if (cmd.hasOption("t")) {
    		// default search for only songs released today
    		testNumber = 1;
    		findNewSongs("testPage.html");
    		// need to reset the arraylist so it will be empty on each test run
    		sortedSongs = new ArrayList<Song>();
    		
    		// default search for songs released both days
    		testNumber = 2;
    		findNewSongs("testPage.html");
    		sortedSongs = new ArrayList<Song>();
    		
    		// default search with invalid input
    		testNumber = 3;
    		findNewSongs("testPage.html");
    		sortedSongs = new ArrayList<Song>();
    		
    		// artist search with artist that hasn't released music
    		searchForRelease("Drake", "testPage.html");
    		sortedSongs = new ArrayList<Song>();
    		
    		// artist search with artist that has released music
    		searchForRelease("Dave East", "testPage.html");
    		sortedSongs = new ArrayList<Song>();
    		
    		// ordered search results
    		findNewSongsNoPrompts("testPage.html");
    		printSongs();
    		sortedSongs = new ArrayList<Song>();
    	}
    }

    /**
     * Starting point for the program
     * @param args not used
     */
    public static void main(String[] args) {
    	new SearchCLI(args);
    }
}