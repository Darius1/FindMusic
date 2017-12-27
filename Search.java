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
									System.exit(1);
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
							System.exit(1);
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
     * Tests the functionality of the Search class
     * @param args not used
     */
    public static void main(String[] args) {
        Search test = new Search("Jay Z");
        //System.out.println(test.getArtist());
        //System.out.println(test.getSong());
        //System.out.println(test);
//        test.searchForRelease("Lil Wayne","http://www.hotnewhiphop.com");
        test.findNewSongs("http://www.hotnewhiphop.com");
//        
//        for (int i = 0; i < test.getSortedSongs().size(); i++) {
//        	System.out.println(test.getSortedSongs().get(i));
//        }
        
    }


}
