//import org.jsoup.Jsoup;

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
        return false;
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
        Search test = new Search("Earl");
        System.out.println(test.getArtist());
        System.out.println(test.getSong());
        System.out.println(test);
    }


}
