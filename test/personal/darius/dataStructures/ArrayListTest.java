package personal.darius.dataStructures;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the ArrayList class and its methods
 * 
 * @author Darius McFarland
 *
 */
public class ArrayListTest {

	/**
	 * Tests the ArrayList constructor
	 */
	@Test
	public void testArrayList() {
		ArrayList<String> testList = new ArrayList<String>();
		assertNotNull(testList);
	}

	/**
	 * Tests the insert() method
	 */
	@Test
	public void testInsert() {
		ArrayList<String> testList = new ArrayList<String>();
		
		testList.insert("What's");
		testList.insert("good");
		testList.insert("Github?");
		assertEquals(3, testList.size());
	}

	/**
	 * Tests the size() method
	 */
	@Test
	public void testSize() {
		ArrayList<String> testList = new ArrayList<String>();
		assertEquals(0, testList.size());
		
		testList.insert("howdy");
		assertEquals(1, testList.size());
	}

	/**
	 * Tests the get() method
	 */
	@Test
	public void testGet() {
		ArrayList<String> testList = new ArrayList<String>();
		
		testList.insert("You");
		testList.insert("already");
		testList.insert("know");
		testList.insert("who");
		testList.insert("it");
		testList.insert("is");
		
		assertEquals("You", testList.get(0));
		assertEquals("is", testList.get(5));
		assertEquals("know", testList.get(2));
	}

	/**
	 * Tests the contains() method
	 */
	@Test
	public void testContains() {
		ArrayList<String> testList = new ArrayList<String>();
		
		testList.insert("because");
		testList.insert("you");
		testList.insert("already");
		testList.insert("know");
		testList.insert("who");
		testList.insert("made");
		testList.insert("this");
		testList.insert("commit");
		
		assertTrue(testList.contains("because"));
		assertTrue(testList.contains("made"));
		assertTrue(testList.contains("commit"));
		
		assertFalse(testList.contains("Pokemon"));
	}

	/**
	 * Tests the isEmpty() method
	 */
	@Test
	public void testIsEmpty() {
		ArrayList<String> testList = new ArrayList<String>();
		assertTrue(testList.isEmpty());
		
		testList.insert("hey");
		assertFalse(testList.isEmpty());
	}

	/**
	 * Tests the set() method
	 */
	@Test
	public void testSet() {
		ArrayList<String> testList = new ArrayList<String>();
		
		testList.insert("Mary");
		testList.insert("had");
		testList.insert("a");
		testList.insert("little");
		testList.insert("lamb");
		
		assertEquals("had", testList.get(1));
		
		testList.set(1, "ate");
		
		assertEquals("ate", testList.get(1));
	}

}
