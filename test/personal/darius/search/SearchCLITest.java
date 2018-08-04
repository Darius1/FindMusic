package personal.darius.search;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the SearchCLI methods
 * 
 * @author Darius McFarland
 *
 */
public class SearchCLITest {

	/**
	 * Tests artist search, default search, and ordered search commands
	 */
	@Test
	public void testSearches() {
		String[] args = new String[1];
		args[0] = "--test";
		
		SearchCLI test = new SearchCLI(args);
		assertNotNull(test);
	}
	
	/**
	 * Tests the help command
	 */
	@Test
	public void testHelp() {
		String[] args = new String[1];
		args[0] = "--help";
		
		SearchCLI test = new SearchCLI(args);
		assertNotNull(test);
	}
	
	/**
	 * Tests the version command
	 */
	@Test
	public void testVersion() {
		String[] args = new String[1];
		args[0] = "--version";
		
		SearchCLI test = new SearchCLI(args);
		assertNotNull(test);
	}
}
