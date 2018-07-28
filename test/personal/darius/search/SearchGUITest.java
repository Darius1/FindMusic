package personal.darius.search;

import java.util.concurrent.TimeoutException;

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
	
	/**
	 * Tests the default search option and only displays songs for today
	 * @throws Exception 
	 *
	 */
	@Test
	public void testDefaultSearch() throws Exception {
		verifyThat(".button", LabeledMatchers.hasText("Search"));
		
		this.clickOn("Search");
		this.clickOn("No");
		this.clickOn("Return to Main Menu");
		this.stop();
		
	 }

}