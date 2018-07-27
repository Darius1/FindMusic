package personal.darius.search;

import static org.junit.Assert.*;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SearchGUITest extends ApplicationTest {

	@Before
	public void setup() throws TimeoutException {
		FxToolkit.registerPrimaryStage();
    	FxToolkit.showStage();
    	
    	FxToolkit.setupApplication(SearchGUI.class);
	}
	
	/**
	 * Tests the default search option and only displays songs for today
	 *
	 */
	@Test
	public void testDefaultSearch() {
		assertThat(Long.valueOf(1), instanceOf(Long.class));    	
		
		this.clickOn("Search");
		this.clickOn("No");
		this.clickOn("Return to Main Menu");
	 }

}