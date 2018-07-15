package personal.darius.search;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import personal.darius.data.Song;
import personal.darius.dataStructures.ArrayList;

public class SearchGUITest extends ApplicationTest {
    
    /** The main menu of the GUI */
    Scene mainScene;
    
    /** The results screen */
    Scene resultsScene;
    
    /** The options screen */
    Scene optionsScene;
    
    /** The scrollable list of songs on the results screen */
    ListView<Song> songList;
    
    /** Holds the songs that are scraped so that they can be added to the ListView dynamically */
    ObservableList<Song> searchedSongs = FXCollections.observableArrayList();
    
    /** Holds all of the scraped songs and sorts them alphabetically */
    private ArrayList<Song> sortedSongs;
    
    /** Keeps track of the search option the user chooses */
    private int searchChoice;
    
    /** Stores the current theme color as a string for easy CSS switching */
    private String themeColor = "Blue";

	@Override
	public void start(Stage primaryStage) throws Exception {
//		SearchGUI testedClass = new SearchGUI();
//		
//		primaryStage.setTitle("Find Music version 1.0");
//    	
//    	Label findMusicLabel = new Label("Find Music");
//    	findMusicLabel.setStyle("-fx-text-fill: #336699;" + "-fx-font-size: 25px;" + "-fx-font-weight: bold;");
//    	
//    	Label optionsLabel = new Label("Search Options");
//    	optionsLabel.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-font-size: 20px;" + "-fx-font-weight: bold;");
//    	
//    	Label resultsLabel = new Label("Search Results");
//    	resultsLabel.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-font-size: 20px;" + "-fx-font-weight: bold;");
//    	
//    	Label themeLabel = new Label("Theme: ");
//    	themeLabel.setStyle("-fx-text-fill: #336699;" + "-fx-font-size: 14px;" + "-fx-font-weight: normal;");
//    	themeLabel.setPadding(new Insets(18, 10, 0, 12));
//    	
//    	Button searchButton = new Button("Search");
//    	searchButton.setOnAction(new EventHandler<ActionEvent>() {
//
//    		/**
//    		 * Performs the search that the user has specified
//    		 *
//    		 * @param event the action that triggered this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				sortedSongs = new ArrayList<Song>();
//				
//				// Clears out the list view so the program will start "fresh" each time search is
//				// pressed
//				if (!searchedSongs.isEmpty()) {
//					searchedSongs.clear();
//				}
//				
//				// Default search
//				if (searchChoice == 1) {
//					testedClass.findNewSongs("http://www.hotnewhiphop.com");
//					primaryStage.setScene(resultsScene);
//				} else if (searchChoice == 2) {
//					String artist = testedClass.createSearchByArtistNamePopup();
//					
//					// Will return null if the user presses cancel when the
//					// artist search popup appears
//					if (artist == null) {
//						primaryStage.setScene(mainScene);
//					} else {
//						// Will return false if an empty string is passed in for an artist name
//						if (testedClass.searchForRelease(artist, "http://www.hotnewhiphop.com", primaryStage)) {
//							if (searchedSongs.isEmpty()) {
//								// display a popup letting the user know their selected artist hasn't
//								// released anything today
//								testedClass.createNoSongsByArtistPopup(artist, primaryStage);
//							} else {
//								primaryStage.setScene(resultsScene);
//							}
//						} else {
//							// Reset to the main menu after the error popup is displayed
//							primaryStage.setScene(mainScene);
//						}
//					}
//					
//				} else if (searchChoice == 3) {
//					testedClass.findNewSongs("http://www.hotnewhiphop.com");
//					testedClass.printSongs();
//					primaryStage.setScene(resultsScene);
//				}
//			}
//		});
//    	searchButton.setPrefSize(100, 20);
//    	searchButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: #336699;");
//    	
//    	Button optionsButton = new Button("Options");
//    	optionsButton.setOnAction(new EventHandler<ActionEvent>() {
//
//    		/**
//    		 * Switches the screen from the main menu to the options menu
//    		 *
//    		 * @param event the event that triggers this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				primaryStage.setScene(optionsScene);
//			}
//		});
//    	optionsButton.setPrefSize(100, 20);
//    	optionsButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: #336699;");
//    	
//    	Button returnButton = new Button("Return to Main Menu");
//    	returnButton.setOnAction(new EventHandler<ActionEvent>() {
//
//    		/**
//    		 * Switches the screen from the options menu to the main menu
//    		 *
//    		 * @param event the event that triggers this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				primaryStage.setScene(mainScene);
//			}
//		});
//    	returnButton.setPrefSize(200, 20);
//    	returnButton.setStyle("-fx-background-color: #336699;" + "-fx-text-fill: #FFFFFF;");
//    	
//    	Button resultsReturn = new Button("Return to Main Menu");
//    	resultsReturn.setOnAction(new EventHandler<ActionEvent>() {
//
//    		/**
//    		 * Switches the screen from the results menu to the main menu
//    		 *
//    		 * @param event the event that triggers this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				primaryStage.setScene(mainScene);
//			}
//		});
//    	resultsReturn.setPrefSize(200, 20);
//    	resultsReturn.setStyle("-fx-background-color: #336699;" + "-fx-text-fill: #FFFFFF;");
//    	
//    	// MainLayout will be the main splash screen for the program
//    	BorderPane mainLayout = new BorderPane();
//    	
//    	StackPane center = new StackPane();
//    	center.getChildren().add(findMusicLabel);
//    	
//    	HBox bottom = new HBox();
//    	bottom.setPadding(new Insets(15, 12, 15, 12));
//    	bottom.setSpacing(10);
//    	bottom.setStyle("-fx-background-color: #336699;");
//    	bottom.setAlignment(Pos.BOTTOM_CENTER);
//    	
//    	bottom.getChildren().addAll(searchButton, optionsButton);
//    	
//    	mainLayout.setBottom(bottom);
//    	mainLayout.setCenter(center);
//    	
//    	mainScene = new Scene(mainLayout, 600, 310);
//    	
//    	// ResultsLayout will display all of the songs that were released today
//    	BorderPane resultsLayout = new BorderPane();
//    	
//    	songList = new ListView<Song>();
//    	
//    	// Adds the search results to the list view
//    	songList.setItems(searchedSongs);
//    	
//    	VBox songResults = new VBox();
//    	songResults.getChildren().add(songList);
//    	
//    	HBox resultsBottom = new HBox();
//    	resultsBottom.setPadding(new Insets(15, 12, 15, 12));
//    	resultsBottom.setSpacing(10);
//    	resultsBottom.setStyle("-fx-background-color: #FFFFFF;");
//    	resultsBottom.setAlignment(Pos.BOTTOM_CENTER);
//    	resultsBottom.getChildren().add(resultsReturn);
//    	
//    	HBox resultsTop = new HBox();
//    	resultsTop.setPadding(new Insets(15, 12, 12, 12));
//    	resultsTop.setSpacing(10);
//    	resultsTop.setAlignment(Pos.CENTER);
//    	resultsTop.setStyle("-fx-background-color: #336699;");
//    	resultsTop.getChildren().add(resultsLabel);
//    	
//    	resultsLayout.setBottom(resultsBottom);
//    	resultsLayout.setCenter(songResults);
//    	resultsLayout.setTop(resultsTop);
//    	
//    	resultsScene = new Scene(resultsLayout, 600, 310);
//    	
//    	// OptionsLayout displays all of the search options and the theme selector
//    	BorderPane optionsLayout = new BorderPane();
//    	
//    	HBox optionsBottom = new HBox();
//    	optionsBottom.setPadding(new Insets(15, 12, 15, 12));
//    	optionsBottom.setSpacing(10);
//    	optionsBottom.setStyle("-fx-background-color: #FFFFFF;");
//    	optionsBottom.setAlignment(Pos.BOTTOM_CENTER);
//    	optionsBottom.getChildren().add(returnButton);
//    	
//    	HBox optionsTop = new HBox();
//    	optionsTop.setPadding(new Insets(15, 12, 12, 12));
//    	optionsTop.setSpacing(10);
//    	optionsTop.setStyle("-fx-background-color: #336699;");
//    	optionsTop.setAlignment(Pos.CENTER);
//    	optionsTop.getChildren().add(optionsLabel);
//    	
//    	VBox optionsList = new VBox();
//    	HBox themeBox = new HBox();
//    	
//    	optionsLayout.setBottom(optionsBottom);
//    	optionsLayout.setLeft(optionsList);
//    	optionsLayout.setTop(optionsTop);
//    	
//    	optionsScene = new Scene(optionsLayout, 600, 310);
//    	
//    	// The options will be presented as a group of radio buttons
//    	ToggleGroup optionButtons = new ToggleGroup();
//    	
//    	RadioButton defaultSearch = new RadioButton("Default Search");
//    	defaultSearch.setToggleGroup(optionButtons);
//    	// Have the defaultSearch button be selected by default
//    	defaultSearch.setSelected(true);
//    	defaultSearch.setPadding(new Insets(10, 10, 10, 5));
//    	defaultSearch.setOnAction(new EventHandler<ActionEvent>() {
//    		
//    		/**
//    		 * Sets the default search option the first radio button to be automatically selected
//    		 *
//    		 * @param event the event that triggers this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				defaultSearch.setSelected(true);
//				searchChoice = 1;
//			}
//    		
//		});
//    	
//    	RadioButton artistSearch = new RadioButton("Search By Artist Name");
//    	artistSearch.setToggleGroup(optionButtons);
//    	artistSearch.setPadding(new Insets(10, 10, 10, 5));
//    	artistSearch.setOnAction(new EventHandler<ActionEvent>() {
//    		/**
//    		 * Sets the search option to be the artist search
//    		 *
//    		 * @param event the event that triggers this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				artistSearch.setSelected(true);
//				searchChoice = 2;
//			}
//    		
//		});
//    	
//    	RadioButton alphabeticalSearch = new RadioButton("Sort Results Alphabetically");
//    	alphabeticalSearch.setToggleGroup(optionButtons);
//    	alphabeticalSearch.setPadding(new Insets(10, 10, 10, 5));
//    	alphabeticalSearch.setOnAction(new EventHandler<ActionEvent>() {
//    		
//    		/**
//    		 * Sets the search option to be the alphabetical search
//    		 *
//    		 * @param event the event that triggers this method
//    		 */
//			@Override
//			public void handle(ActionEvent event) {
//				alphabeticalSearch.setSelected(true);
//				searchChoice = 3;
//			}
//    		
//		});
//    	
//    	// The list of theme color choices
//    	ObservableList<String> options = FXCollections.observableArrayList("Blue", "Light Blue", "Red", "Green", "Orange", "Pink", "Black", "Purple", "Gray");
//    	ComboBox<String> comboBox = new ComboBox<String>(options);
//    	comboBox.setVisibleRowCount(2);
//
//    	themeBox.getChildren().addAll(themeLabel, comboBox);
//    	
//    	// Add the buttons to the optionsList
//    	optionsList.getChildren().addAll(defaultSearch, artistSearch, alphabeticalSearch, themeBox);
//    	
//    	// Sets the default search option
//    	if (defaultSearch.isSelected()) {
//    		searchChoice = 1;
//    	}
//    	
//    	// Handles switching the theme CSS
//    	comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//
//    		/**
//    		 * Changes the CSS for the program to the appropriate sheet for the selected color
//    		 *
//    		 * @param observable The list of choices
//    		 * @param oldValue the previous theme color choice
//    		 * @param newValue the new theme color choice
//    		 */
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				returnButton.setStyle("-fx-background-color: " + testedClass.changeTheme(newValue) + ";" + "-fx-text-fill: #FFFFFF;");
//				resultsReturn.setStyle("-fx-background-color: " + testedClass.changeTheme(newValue) + ";" + "-fx-text-fill: #FFFFFF;");
//				optionsTop.setStyle("-fx-background-color: " + testedClass.changeTheme(newValue) + ";");
//				resultsTop.setStyle("-fx-background-color: " + testedClass.changeTheme(newValue) + ";");
//				bottom.setStyle("-fx-background-color: " + testedClass.changeTheme(newValue) + ";");
//				searchButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: " + testedClass.changeTheme(newValue) + ";");
//				optionsButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: " + testedClass.changeTheme(newValue) + ";");
//				findMusicLabel.setStyle("-fx-text-fill: " + testedClass.changeTheme(newValue) + ";" + "-fx-font-size: 25px;" + "-fx-font-weight: bold;");
//				themeLabel.setStyle("-fx-text-fill: " + testedClass.changeTheme(newValue) + ";" + "-fx-font-size: 14px;" + "-fx-font-weight: normal;");
//				
//				if (newValue.equals("Light Blue")) {
//					mainScene.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
//			    	optionsScene.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
//			    	resultsScene.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
//				} else {
//					mainScene.getStylesheets().add(SearchGUI.class.getResource("material" + newValue + ".css").toExternalForm());
//			    	optionsScene.getStylesheets().add(SearchGUI.class.getResource("material" + newValue + ".css").toExternalForm());
//			    	resultsScene.getStylesheets().add(SearchGUI.class.getResource("material" + newValue + ".css").toExternalForm());
//				}
//				themeColor = newValue;
//			}
//		});
//    	
//    	// Set the CSS of the program to use Blue as the default color
//    	mainScene.getStylesheets().add(SearchGUI.class.getResource("materialBlue.css").toExternalForm());
//    	optionsScene.getStylesheets().add(SearchGUI.class.getResource("materialBlue.css").toExternalForm());
//    	resultsScene.getStylesheets().add(SearchGUI.class.getResource("materialBlue.css").toExternalForm());
//    	
//    	
//    	// Set the mainScene as the default scene to display on program start
//    	primaryStage.setScene(mainScene);
//    	primaryStage.show();
	}
	
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
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Search");
	   this.clickOn("No");
	   this.clickOn("Return to Main Menu");
	 }
	
	/**
	 * Tests the default search option and displays songs for yesterday and today
	 *
	 */
	@Test
	public void testDefaultSearchYesterday() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Search");
	   this.clickOn("Yes");
	   this.clickOn("Return to Main Menu");
	 }
	
	/**
	 * Tests the artist search option
	 *
	 */
	@Test
	public void testArtistSearch() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Search By Artist Name");
	   this.clickOn("Return to Main Menu");
	   this.clickOn("Search");
	   this.clickOn("What artist would you like to search for?");
	   this.write("Curren$y");
	   this.clickOn("OK");
	   this.clickOn("Return to Main Menu");
	 }
	
	/**
	 * Tests the artist search option with an artist that doesn't have any songs today
	 *
	 */
	@Test
	public void testArtistSearchNoSongsToday() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Search By Artist Name");
	   this.clickOn("Return to Main Menu");
	   this.clickOn("Search");
	   this.clickOn("What artist would you like to search for?");
	   this.write("Drake");
	   this.clickOn("OK");
	   this.clickOn("Ok");
	 }
	
	/**
	 * Tests the artist search option
	 *
	 */
	@Test
	public void testAlphabeticalSort() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Sort Results Alphabetically");
	   this.clickOn("Return to Main Menu");
	   this.clickOn("Search");
	   this.clickOn("No");
	   this.clickOn("Return to Main Menu");
	 }
	
	/**
	 * Tests the artist search option and checks for songs released yesterday and today
	 *
	 */
	@Test
	public void testAlphabeticalSortYesterday() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Sort Results Alphabetically");
	   this.clickOn("Return to Main Menu");
	   this.clickOn("Search");
	   this.clickOn("Yes");
	   this.clickOn("Return to Main Menu");
	 }
	
	/**
	 * Tests the artist search option with the user clicking cancel
	 *
	 */
	@Test
	public void testArtistSearchCancel() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Search By Artist Name");
	   this.clickOn("Return to Main Menu");
	   this.clickOn("Search");
	   this.clickOn("Cancel");
	 }
	
	/**
	 * Tests the artist search option with an empty search entry
	 *
	 */
	@Test
	public void testArtistSearchEmptySearch() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Search By Artist Name");
	   this.clickOn("Return to Main Menu");
	   this.clickOn("Search");
	   this.clickOn("OK");
	   this.clickOn("Ok");
	 }
	
	/**
	 * Tests selecting, unselecting, and reselecting the default search option
	 *
	 */
	@Test
	public void testSelectDefaultSearch() {
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
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
	   verifyThat(".button", NodeMatchers.hasText("Search"));
	    	
	   this.clickOn("Options");
	   this.clickOn("Theme: ");
	   this.clickOn(".combo-box");
	   this.clickOn("Blue");
//	   this.clickOn("Light Blue");
	 }
}
