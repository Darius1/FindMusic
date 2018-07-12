package personal.darius.data;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the Song class and its methods
 * 
 * @author Darius McFarland
 *
 */
public class SongTest {

	/** Static song artist to use to aid with testing */
	private static final String ARTIST = "Drake";
	
	/** Static song title to use to aid with testing */
	private static final String SONGTITLE = "Star 67";
	
	/**
	 * Tests the Song constructor
	 */
	@Test
	public void testSong() {
		Song testSong = new Song(ARTIST, SONGTITLE);
		assertNotNull(testSong);
	}

	/**
	 * Tests the getArtist() method
	 */
	@Test
	public void testGetArtist() {
		Song testSong = new Song(ARTIST, SONGTITLE);
		assertEquals("Drake", testSong.getArtist());
	}

	/**
	 * Tests the getTitle() method
	 */
	@Test
	public void testGetTitle() {
		Song testSong = new Song(ARTIST, SONGTITLE);
		assertEquals("Star 67", testSong.getTitle());
	}
	
	/**
	 * Tests the getReleaseDate() and setReleaseDate() methods
	 */
	@Test
	public void testReleaseDateMethods() {
		Song testSong = new Song(ARTIST, SONGTITLE);
		
		testSong.setReleaseDate("February 13, 2015");
		
		assertEquals("February 13, 2015", testSong.getReleaseDate());
	}

	/**
	 * Tests the toString() method
	 */
	@Test
	public void testToString() {
		Song testSong = new Song(ARTIST, SONGTITLE);
		assertEquals("Drake: Star 67", testSong.toString());
	}

	/**
	 * Tests the compareTo() method
	 */
	@Test
	public void testCompareTo() {
		Song testSong = new Song(ARTIST, SONGTITLE);
		Song comparedSong = new Song("Earl Sweatshirt", "New Faces");
		
		// Ensure that Drake comes before Earl Sweatshirt alphabetically
		assertEquals(-1, testSong.compareTo(comparedSong));
		assertEquals(1, comparedSong.compareTo(testSong));
	}

}
