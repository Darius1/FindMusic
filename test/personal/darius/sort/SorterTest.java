package personal.darius.sort;

import static org.junit.Assert.*;

import org.junit.Test;

import personal.darius.data.Song;
import personal.darius.dataStructures.ArrayList;

/**
 * Tests the Sorter class and its methods
 * 
 * @author Darius McFarland
 *
 */
public class SorterTest {

	/**
	 * Tests the Sorter constructor
	 */
	@Test
	public void testSorter() {
		Sorter<Song> sorter = new Sorter<Song>();
		assertNotNull(sorter);
	}

	/**
	 * Tests the alphabeticalSort() method
	 */
	@Test
	public void testAlphabeticalSort() {
		Sorter<Song> sorter = new Sorter<Song>();
		
		Song tuscanLeather = new Song("Drake", "Tuscan Leather");
		Song rollingThunder = new Song("Action Bronson", "Rolling Thunder");
		Song diablo = new Song("Mac Miller", "Diablo");
		Song rusty = new Song("Tyler the Creator", "Rusty");
		Song thatsHowIFeel = new Song("Young Dolph", "That's How I Feel");
		Song rella = new Song("Tyler the Creator", "Rella");
		Song doNotDisturb = new Song("Drake", "Do Not Disturb");
		
		ArrayList<Song> songs = new ArrayList<Song>();
		
		songs.insert(tuscanLeather);
		songs.insert(rollingThunder);
		songs.insert(diablo);
		songs.insert(rusty);
		songs.insert(thatsHowIFeel);
		songs.insert(rella);
		songs.insert(doNotDisturb);
		assertEquals(7, songs.size());
		
		assertEquals("Drake: Tuscan Leather", songs.get(0).toString());
		assertEquals("Action Bronson: Rolling Thunder", songs.get(1).toString());
		assertEquals("Mac Miller: Diablo", songs.get(2).toString());
		assertEquals("Tyler the Creator: Rusty", songs.get(3).toString());
		assertEquals("Young Dolph: That's How I Feel", songs.get(4).toString());
		assertEquals("Tyler the Creator: Rella", songs.get(5).toString());
		assertEquals("Drake: Do Not Disturb", songs.get(6).toString());
		
		// Run the merge sort and check to ensure that everything is sorted alphabetically
		
		sorter.alphabeticalSort(songs);
		
		assertEquals("Action Bronson: Rolling Thunder", songs.get(0).toString());
		assertEquals("Drake: Do Not Disturb", songs.get(1).toString());
		assertEquals("Drake: Tuscan Leather", songs.get(2).toString());
		assertEquals("Mac Miller: Diablo", songs.get(3).toString());
		assertEquals("Tyler the Creator: Rella", songs.get(4).toString());
		assertEquals("Tyler the Creator: Rusty", songs.get(5).toString());
		assertEquals("Young Dolph: That's How I Feel", songs.get(6).toString());
	}

}
