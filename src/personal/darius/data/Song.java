package personal.darius.data;

/**
 * The song class is the base class of the entire project. It is made up of a title, and an artist.
 * 
 * @author Darius McFarland
 *
 */
public class Song implements Comparable<Song> {

	/** Holds the artist's name */
    private String artist;
    
    /** Holds the song's title */
    private String title;
    
    /** The day the song first appeared in the search */
    private String releaseDate;
    
    /**
     * Creates a new Song object that contains a song title and artist
     *
     * @param artist the creator of the song
     * @param title the name of the song
     */
    public Song(String artist, String title) {
    	this.artist = artist;
    	this.title = title;
    }
    
    /**
     * Returns the artist's name
     * @return artist
     */
    public String getArtist() {
    	return artist;
    }
    
    /**
     * Returns the song title
     * @return the title of the song
     */
    public String getTitle() {
    	return title;
    }
    
    /**
     * Sets the releaseDate field
     *
     * @param date the date the releaseDate field will be set to
     */
    public void setReleaseDate(String date) {
    	this.releaseDate = date;
    }
    
    /**
     * Returns the song release date
     * @return the release date of the song
     */
    public String getReleaseDate() {
    	return releaseDate;
    }
    
    /**
     * Creates a string in the format "Artist: Song"
     * @return string
     */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(artist);
		builder.append(": ");
		builder.append(title);
		return builder.toString();
	}

	/**
	 * Compares two songs based on their names
	 *
	 * @param otherSong the song this is being compared to
	 * @return -1 if this comes before otherSong alphabetically. 1 if this comes after
	 * otherSong alphabetically, and 0 if this and otherSong are equal
	 */
	@Override
	public int compareTo(Song otherSong) {
		String thisSong = this.toString();
		String comparedSong = otherSong.toString();
		
		if (thisSong.compareTo(comparedSong) < 0) {
			return -1;
		} else if (thisSong.compareTo(comparedSong) > 0 ) {
			return 1;
		}
		return 0;
	}

}
