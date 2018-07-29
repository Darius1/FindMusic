package personal.darius.search;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import static org.testfx.api.FxAssert.verifyThat;

public class SearchGUITest extends ApplicationTest {

	@Before
	public void setup() throws TimeoutException {
		FxToolkit.registerPrimaryStage();
    	FxToolkit.showStage();
    	
    	FxToolkit.setupApplication(SearchGUI.class);
	}
	
	@After
	public void tearDown() {
		try {
			FxToolkit.cleanupStages();
			FxToolkit.hideStage();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the default search option and only displays songs for today
	 * @throws Exception 
	 *
	 */
	@Test
	public void testDefaultAndAlphabeticalSearch() throws Exception {
		verifyThat(".button", LabeledMatchers.hasText("Search"));
		
		this.clickOn("Options");
		this.clickOn("Search Options");
		this.clickOn("Return to Main Menu");
		
		// Tests default search
		this.clickOn("Search");
		this.clickOn("No");
		
		// Tests the alphabetical search
		this.clickOn("Search");
		this.clickOn("No");
	 }
	
	/**
	 * Tests the default search option and only displays songs for both days
	 * @throws Exception 
	 *
	 */
	@Test
	public void testDefaultAndAlphabeticalSearchYesterday() throws Exception {
		verifyThat(".button", LabeledMatchers.hasText("Search"));
		
		this.clickOn("Options");
		this.clickOn("Search Options");
		this.clickOn("Return to Main Menu");
		
		// Tests default search
		this.clickOn("Search");
		this.clickOn("Yes");
		
		// Tests the alphabetical search
		this.clickOn("Search");
		this.clickOn("Yes");
	 }
	
	/**
	 * Tests the artist search option
	 * @throws Exception 
	 *
	 */
	@Test
	public void testArtistSearch() throws Exception {
		verifyThat(".button", LabeledMatchers.hasText("Search"));
		
		this.clickOn("Options");
		this.drag("Search Options");
		this.clickOn(0, 0);
		this.clickOn("Return to Main Menu");
		
		// Tests searching for an artist that hasn't released any songs
		this.clickOn("Search");
		this.clickOn("What artist would you like to search for?");
		this.write("Drake");
		this.clickOn("OK");
		this.clickOn("Ok");
		
		// Tests canceling a search
		this.clickOn("Search");
		this.clickOn("Cancel");
		
		// Tests trying to enter a blank artist name in the search bar
		this.clickOn("Search");
		this.clickOn("OK");
		this.clickOn("Ok");
	 }
	
	/**
	 * Tests searching for an artist that has released music
	 *
	 */
	@Test
	public void testArtistThatReleasedMusicSearch() {
	   verifyThat(".button", LabeledMatchers.hasText("Search"));
	   	
	   this.clickOn("Options");
	   this.drag("Search Options");
	   this.clickOn(".combo-box");
	   this.clickOn("Return to Main Menu");
	   
	   // Tests searching for an artist that has released songs
	   this.clickOn("Search");
	   this.clickOn("What artist would you like to search for?");
	   this.write("Dave East");
	   this.clickOn("OK");
	   this.clickOn("Return to Main Menu");
	 }
	
	/**
	 * Tests selecting, unselecting, and reselecting the default search option
	 *
	 */
	@Test
	public void testSelectDefaultSearch() {
	   verifyThat(".button", LabeledMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Search By Artist Name");
	   this.clickOn("Default Search");
	 }
	
	/**
	 * Tests selecting different themes
	 *
	 */
	@Test
	public void testThemes() {
	   verifyThat(".button", LabeledMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Theme: ");
	   this.clickOn(".combo-box");
	   this.clickOn("Blue");
//	   this.clickOn("Light Blue");
	 }

}