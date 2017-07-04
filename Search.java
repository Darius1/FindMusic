import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.select.Elements;

import java.io.*;

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


    /**
     * Search object constructor that takes in the artist name as a parameter
     * @param artist the artist's name
     * @param
     */
    public Search(String artist) {
        this.artist = artist;
        website = "";
        song = "";

    }

    /**
     * Searches a website using an artist's name and current date as parameters to find new music
     * released by the artist for that day
     * @param  artist the artist's name
     * @param  website the website url
     * @param  date the date
     * @return true if the artist released music on that date, false otherwise
     */
    public boolean searchForRelease(String artist, String website, String date) {
        try {
            Document doc = Jsoup.connect(website).get();
            Elements findSong = doc.select("div.cover-title");
            Elements findArtist = doc.select("div.dailySongChart-artist");
            int numberOfSongsToday = 0;

            System.out.printf("%5s", "Song " + "\t\t\t|\t\tArtist\n");
            System.out.println("----------------------------------------------");


            // Found code similar to this in a Youtube video. Need to learn how to use ArrayLists and remember to read methods inherited section of APIs.
            for (int i = 0; i < findSong.size(); i++) {
            	//System.out.println(findArtist.get(i).text());

            	if (findArtist.get(i).text().contains(artist)) {
            		System.out.println(findSong.get(i).text() + " | " + findArtist.get(i).text());
            		numberOfSongsToday++;
            		//song = findSong.get(i).text();
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
     * Tests the functionality of the Search class
     * @param args not used
     */


    public static void main(String[] args) {
        //Search test = new Search("Jay Z");
        //System.out.println(test.getArtist());
        //System.out.println(test.getSong());
        //System.out.println(test);
        //test.searchForRelease("Jay Z","http://www.hotnewhiphop.com","date");
    }


}
