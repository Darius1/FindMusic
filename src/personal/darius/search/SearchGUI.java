package personal.darius.search;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import personal.darius.data.Song;
import personal.darius.dataStructures.ArrayList;
import personal.darius.sort.Sorter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * This class will handle all of the search functionality in the FindMusic program and will display
 * a GUI
 * 
 * @author Darius McFarland
 */
public class SearchGUI extends Application {
	
    /** Holds the website url that will be searched */
    private String website;
    
    /** Holds all of the scraped songs and sorts them alphabetically */
    private ArrayList<Song> sortedSongs;
    
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
    
    /** Keeps track of the search option the user chooses */
    private int searchChoice;
    
    /** Hex code for Wolfpack Red */
    private static final String RED = "#cc0000";
    
    /** Hex code for Blue */
    private static final String BLUE = "#336699";
    
    /** Hex code for Light Blue */
    private static final String LIGHT_BLUE = "#87cefa";
    
    /** Hex code for Green */
    private static final String GREEN = "#00cd66";
    
    /** Hex code for Orange */
    private static final String ORANGE = "#ff8c00";
    
    /** Hex code for Pink */
    private static final String PINK = "#f08080";
    
    /** Hex code for Black */
    private static final String BLACK = "#000000";
    
    /** Hex code for Purple */
    private static final String PURPLE = "#9370db";
    
    /** Hex code for Gray */
    private static final String GRAY = "#999999";
    
    /** Stores the current theme color as a string for easy CSS switching */
    private String themeColor = "Blue";

    /**
     * Searches a website using an artist's name and current date as parameters to find new music
     * released by the artist for that day
     * @param  artist the artist's name
     * @param  website the website url
     * @param primaryStage the main program window. Needed to create the invalid artist popup
     * 
     * @return true if the artist has released music. False if an invalid artist is specified
     * or an error loading the website occurs
     */
    public boolean searchForRelease(String artist, String website, Stage primaryStage) {
    	Document doc = null;
    	
        try {
        	// Don't allow the user to enter a blank artist name
        	if (artist.equals("")) {
        		createInvalidArtistNamePopup(primaryStage);
        		return false;
        	}
        	
        	if (website.equals("testPage.html")) {
    			File input = new File(website);
    			doc = Jsoup.parse(input, "UTF-8", "http://hotnewhiphop.com/");
    		} else {
    			doc = Jsoup.connect(website).get();
    		}
        	
            Elements findSong = doc.select(".dailySongChart-item");

            String currentDate = "";
            
            // If the searchChoice is 4 we're testing and need to use a static date
            if (searchChoice != 4) {
            	// Example formatted date: Monday Jan 1, 2000		
    			LocalDate date = LocalDate.now();
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
    			currentDate = date.format(formatter);
            } else {
            	currentDate = "Saturday Jul 28, 2018";
            }
            
			
            for (int i = 0; i < findSong.size(); i++) {
            	if (findSong.get(i).text().contains(artist)) {
            		sortedSongs.insert(formattedSong(findSong.get(i), currentDate));
					searchedSongs.add(formattedSong(findSong.get(i), currentDate));
            	}
            }
            
            return true;
        } catch (IOException e) {
            System.out.println("There was an error fetching your website.");
            return false;
        }
    }

    /**
     * Returns the website name
     * @return website
     */
    public String getWebsite() {
        return website;
    }
    
    /**
     * Searches the specified website for songs that were released today and the previous day
     * @param website the website being searched
     */
    public void findNewSongs(String website) {
    	int songsReleasedToday = 0;
    	boolean checkedYesterday = false;
    	boolean dateChecked = false;
    	Document doc = null;
    	
    	try {
    		// Will use testPage.html for unit testing
    		if (website.equals("testPage.html")) {
    			File input = new File(website);
    			doc = Jsoup.parse(input, "UTF-8", "http://hotnewhiphop.com/");
    		} else {
    			doc = Jsoup.connect(website).get();
    		}
    		
			Elements findSong = doc.select(".dailySongChart-item");

			songsReleasedToday = findSong.size();
			
			String currentDate = "";
			
			for (int i = 0; i < songsReleasedToday; i++) {
				
				// If the searchChoice is 4 we're testing and need to use a static date
				if (searchChoice != 4) {
					// Example formatted date: Monday Jan 1, 2000		
					LocalDate date = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
					currentDate = date.format(formatter);
				} else {
					currentDate = "Saturday Jul 28, 2018";
				}
				

				// HotNewHipHop places the dailySongChart-day-date class tag on the very first song posted each day 
				// Need to check this tag to get the date information
				if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && !checkedYesterday) {
					if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(currentDate)) {
						
						dateChecked = true;
					} else {

						if (createShowSongsFromYesterdayPopup()) {
							if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(yesterday())) {
								checkedYesterday = true;
							}
						} else {
							break;
						}
						
					}
				} else if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && checkedYesterday) {
					break;
				}
				
				
				if (findSong.size() > 0) {
					// Determines what the release date of the song should be
					if (checkedYesterday) {
						sortedSongs.insert(formattedSong(findSong.get(i), yesterday()));
						searchedSongs.add(formattedSong(findSong.get(i), yesterday()));
					} else {
						sortedSongs.insert(formattedSong(findSong.get(i), currentDate));
						searchedSongs.add(formattedSong(findSong.get(i), currentDate));
					}
				}
				
			}
			
			// add the songs to the database
//			System.out.println("Adding to database!");
//			SongStorage database = new SongStorage();
//			database.addSongsToDatabase(sortedSongs);
			
			
		} catch (IndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Grabs the song and artist text from the element parameter
     *
     * @param element the part of the webpage the data will be gathered from
     * @param releaseDate the date the song first appeared in the search
     * @return a new Song object
     */
    private Song formattedSong(Element element, String releaseDate) {
    	String artist = element.getElementsByClass("dailySongChart-artist").text();
    	String title = element.getElementsByClass("cover-title").text();
    	
    	Song newSong = new Song(artist, title);
    	newSong.setReleaseDate(releaseDate);
    	
		return newSong;
	}

    /**
     * Gets yesterday's date and formats it in the form "Day of week Month Date, Year"
     * Example formatted date: Monday Jan 1, 2000
     *
     * @return a properly formatted date
     */
	private String yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        
        DateFormat dateFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");
        return dateFormat.format(yesterday);
    }
    
    /**
     * Prints out songs in alphabetical order
     */
    private void printSongs() {
    	Sorter<Song> sorter = new Sorter<Song>();
    	
    	sorter.alphabeticalSort(sortedSongs);
    	
    	// Not the most efficient solution. Songs are already added to the observable list when
    	// the search is first performed. Still n log n time since the merge sort drives runtime
    	
    	for (int i = 0; i < sortedSongs.size(); i++) {
    		searchedSongs.set(i, sortedSongs.get(i));
    	}
    }
    
    /**
     * Creates a dialog box that gives the user the choice to display songs that released yesterday
     *
     * @return true if the user clicks yes, no otherwise
     */
    private boolean createShowSongsFromYesterdayPopup() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(null);
 
        alert.setHeaderText(null);
        
        // Gets rid of the default ok button
        alert.getButtonTypes().clear();
        alert.setContentText("Would you like to check for songs that released yesterday?");
        
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        
        alert.getButtonTypes().addAll(yes, no);
        
        DialogPane pane = alert.getDialogPane();
        
        // Styles the alert box with the current theme
        if (themeColor.equals("Light Blue")) {
			pane.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
		} else {
			pane.getStylesheets().add(SearchGUI.class.getResource("material" + themeColor + ".css").toExternalForm());
		}
 
        Optional<ButtonType> choice = alert.showAndWait();
        
        if (choice.get() == yes) {
        	return true;
        } else {
        	return false;
        }
    }
    
    /**
     * Creates a popup box with a text field for the user to specify an artist to search for
     *
     * @return a String containing the artist's name
     */
    private String createSearchByArtistNamePopup() {
    	try {
			TextInputDialog dialog = new TextInputDialog();
			 
			dialog.setTitle("Artist Search");
			dialog.setHeaderText(null);
			dialog.setContentText("What artist would you like to search for?");
			
			DialogPane pane = dialog.getDialogPane();
			
			// Styles the alert box with the current theme
			if (themeColor.equals("Light Blue")) {
				pane.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
		        pane.setStyle("-fx-text-fill: " + changeTheme(themeColor) + ";");
			} else {
				pane.getStylesheets().add(SearchGUI.class.getResource("material" + themeColor + ".css").toExternalForm());
		        pane.setStyle("-fx-text-fill: " + changeTheme(themeColor) + ";");
			}
	        		 
			Optional<String> result = dialog.showAndWait();
			
			return result.get();
		} catch (NoSuchElementException e) {
			// Thrown whenever the user clicks cancel without entering an artist name
		}
		return null;
    }
    
    /**
     * Creates a popup letting the user know that they entered an invalid name for an artist
     *
     * @param primaryStage the main window of the program
     */
    public void createInvalidArtistNamePopup(Stage primaryStage) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(null);
 
        alert.setHeaderText(null);
        
        // Gets rid of the default ok button
        alert.getButtonTypes().clear();
        alert.setContentText("Invalid Artist Name");
        
        ButtonType ok = new ButtonType("Ok");
        
        alert.getButtonTypes().addAll(ok);
        
        DialogPane pane = alert.getDialogPane();
        
        // Styles the alert box with the current theme
        if (themeColor.equals("Light Blue")) {
			pane.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
		} else {
			pane.getStylesheets().add(SearchGUI.class.getResource("material" + themeColor + ".css").toExternalForm());
		}
 
        Optional<ButtonType> choice = alert.showAndWait();
        
        if (choice.get() == ok) {
        	primaryStage.setScene(mainScene);
        } 
    }
    
    /**
     * Creates a popup letting the user know that the artist they searched for hasn't released
     * any music in the past 48 hours
     *
     * @param artist the artist being searched for
     * @param primaryStage the main window of the program
     */
    private void createNoSongsByArtistPopup(String artist, Stage primaryStage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(null);
 
        alert.setHeaderText(null);
        
        // Gets rid of the default ok button
        alert.getButtonTypes().clear();
        alert.setContentText(artist + " has not released any music today.");
        
        ButtonType ok = new ButtonType("Ok");
        
        alert.getButtonTypes().addAll(ok);
        
        DialogPane pane = alert.getDialogPane();
        
        // Styles the alert box with the current theme
        if (themeColor.equals("Light Blue")) {
			pane.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
		} else {
			pane.getStylesheets().add(SearchGUI.class.getResource("material" + themeColor + ".css").toExternalForm());
		}
 
        Optional<ButtonType> choice = alert.showAndWait();
        
        if (choice.get() == ok) {
        	primaryStage.setScene(mainScene);
        }
        
    }
    
    /**
     * Takes the passed in color string and returns that color's hex code
     *
     * @param color the theme color the user has selected
     * @return the hex code associated with the color specified by the user
     */
    private String changeTheme(String color) {
    	if (color.equals("Blue")) {
    		return BLUE;
    	} else if (color.equals("Light Blue")) {
    		return LIGHT_BLUE;
    	} else if (color.equals("Red")) {
    		return RED;
    	} else if (color.equals("Green")) {
    		return GREEN;
    	} else if (color.equals("Orange")) {
    		return ORANGE;
    	} else if (color.equals("Pink")) {
    		return PINK;
    	} else if (color.equals("Black")) {
    		return BLACK;
    	} else if (color.equals("Purple")) {
    		return PURPLE;
    	} else {
    		return GRAY;
    	}
    }

    /**
     * Tests the functionality of the Search class
     * @param args not used
     */
    public static void main(String[] args) {
    	// Used for the GUI
    	launch(args);
    }
    
    /**
     * The starting point for the GUI
     * 
     * @param primaryStage the main window of the program
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	boolean testMode = false;
    	
    	primaryStage.setTitle("Find Music version 1.0");
    	
    	Label findMusicLabel = new Label("Find Music");
    	findMusicLabel.setStyle("-fx-text-fill: #336699;" + "-fx-font-size: 25px;" + "-fx-font-weight: bold;");
    	
    	Label optionsLabel = new Label("Search Options");
    	optionsLabel.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-font-size: 20px;" + "-fx-font-weight: bold;");
    	
    	Label resultsLabel = new Label("Search Results");
    	resultsLabel.setStyle("-fx-text-fill: #FFFFFF;" + "-fx-font-size: 20px;" + "-fx-font-weight: bold;");
    	
    	Label themeLabel = new Label("Theme: ");
    	themeLabel.setStyle("-fx-text-fill: #336699;" + "-fx-font-size: 14px;" + "-fx-font-weight: normal;");
    	themeLabel.setPadding(new Insets(18, 10, 0, 12));
    	
    	Button searchButton = new Button("Search");
    	searchButton.setOnAction(new EventHandler<ActionEvent>() {

    		/**
    		 * Performs the search that the user has specified
    		 *
    		 * @param event the action that triggered this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				sortedSongs = new ArrayList<Song>();
				
				// Clears out the list view so the program will start "fresh" each time search is
				// pressed
				if (!searchedSongs.isEmpty()) {
					searchedSongs.clear();
				}
				
				// Default search
				if (searchChoice == 1) {
					findNewSongs("http://www.hotnewhiphop.com");
					primaryStage.setScene(resultsScene);
					
				} else if (searchChoice == 2) {
					String artist = createSearchByArtistNamePopup();
					
					// Will return null if the user presses cancel when the
					// artist search popup appears
					if (artist == null) {
						primaryStage.setScene(mainScene);
					} else {
						// Will return false if an empty string is passed in for an artist name
						if (searchForRelease(artist, "http://www.hotnewhiphop.com", primaryStage)) {
							if (searchedSongs.isEmpty()) {
								// display a popup letting the user know their selected artist hasn't
								// released anything today
								createNoSongsByArtistPopup(artist, primaryStage);
							} else {
								primaryStage.setScene(resultsScene);
							}
						} else {
							// Reset to the main menu after the error popup is displayed
							primaryStage.setScene(mainScene);
						}
					}
					
				} else if (searchChoice == 3) {
					findNewSongs("http://www.hotnewhiphop.com");
					printSongs();
					primaryStage.setScene(resultsScene);
				} else if (searchChoice == 4) {
					// runs the default search with the saved hnh webpage
					findNewSongs("testPage.html");
					primaryStage.setScene(resultsScene);				
					primaryStage.setScene(mainScene);
					
					// runs the alphabetical search with the saved hnh webpage
					findNewSongs("testPage.html");
					printSongs();
					primaryStage.setScene(resultsScene);
					
				} else if (searchChoice == 5) {
					// runs the artist search with the saved hnh webpage
					String artist = createSearchByArtistNamePopup();
					
					// Will return null if the user presses cancel when the
					// artist search popup appears
					if (artist == null) {
						primaryStage.setScene(mainScene);
					} else {
						// Will return false if an empty string is passed in for an artist name
						if (searchForRelease(artist, "testPage.html", primaryStage)) {
							if (searchedSongs.isEmpty()) {
								// display a popup letting the user know their selected artist hasn't
								// released anything today
								createNoSongsByArtistPopup(artist, primaryStage);
							} else {
								primaryStage.setScene(resultsScene);
							}
						} else {
							// Reset to the main menu after the error popup is displayed
							primaryStage.setScene(mainScene);
						}
					}
				}
			}
		});
    	
    	searchButton.setPrefSize(100, 20);
    	searchButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: #336699;");
    	
    	Button optionsButton = new Button("Options");
    	optionsButton.setOnAction(new EventHandler<ActionEvent>() {

    		/**
    		 * Switches the screen from the main menu to the options menu
    		 *
    		 * @param event the event that triggers this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(optionsScene);
			}
		});
    	
    	optionsButton.setPrefSize(100, 20);
    	optionsButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: #336699;");
    	
    	Button returnButton = new Button("Return to Main Menu");
    	returnButton.setOnAction(new EventHandler<ActionEvent>() {

    		/**
    		 * Switches the screen from the options menu to the main menu
    		 *
    		 * @param event the event that triggers this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(mainScene);
			}
		});
    	returnButton.setPrefSize(200, 20);
    	returnButton.setStyle("-fx-background-color: #336699;" + "-fx-text-fill: #FFFFFF;");
    	
    	Button resultsReturn = new Button("Return to Main Menu");
    	resultsReturn.setOnAction(new EventHandler<ActionEvent>() {

    		/**
    		 * Switches the screen from the results menu to the main menu
    		 *
    		 * @param event the event that triggers this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(mainScene);
			}
		});
    	
    	resultsReturn.setPrefSize(200, 20);
    	resultsReturn.setStyle("-fx-background-color: #336699;" + "-fx-text-fill: #FFFFFF;");
    	
    	// MainLayout will be the main splash screen for the program
    	BorderPane mainLayout = new BorderPane();
    	
    	StackPane center = new StackPane();
    	center.getChildren().add(findMusicLabel);
    	
    	HBox bottom = new HBox();
    	bottom.setPadding(new Insets(15, 12, 15, 12));
    	bottom.setSpacing(10);
    	bottom.setStyle("-fx-background-color: #336699;");
    	bottom.setAlignment(Pos.BOTTOM_CENTER);
    	
    	bottom.getChildren().addAll(searchButton, optionsButton);
    	
    	mainLayout.setBottom(bottom);
    	mainLayout.setCenter(center);
    	
    	mainScene = new Scene(mainLayout, 600, 310);
    	
    	// ResultsLayout will display all of the songs that were released today
    	BorderPane resultsLayout = new BorderPane();
    	
    	songList = new ListView<Song>();
    	
    	// Adds the search results to the list view
    	songList.setItems(searchedSongs);
    	
    	VBox songResults = new VBox();
    	songResults.getChildren().add(songList);
    	
    	HBox resultsBottom = new HBox();
    	resultsBottom.setPadding(new Insets(15, 12, 15, 12));
    	resultsBottom.setSpacing(10);
    	resultsBottom.setStyle("-fx-background-color: #FFFFFF;");
    	resultsBottom.setAlignment(Pos.BOTTOM_CENTER);
    	resultsBottom.getChildren().add(resultsReturn);
    	
    	HBox resultsTop = new HBox();
    	resultsTop.setPadding(new Insets(15, 12, 12, 12));
    	resultsTop.setSpacing(10);
    	resultsTop.setAlignment(Pos.CENTER);
    	resultsTop.setStyle("-fx-background-color: #336699;");
    	resultsTop.getChildren().add(resultsLabel);
    	
    	resultsLayout.setBottom(resultsBottom);
    	resultsLayout.setCenter(songResults);
    	resultsLayout.setTop(resultsTop);
    	
    	resultsScene = new Scene(resultsLayout, 600, 310);
    	
    	// OptionsLayout displays all of the search options and the theme selector
    	BorderPane optionsLayout = new BorderPane();
    	
    	HBox optionsBottom = new HBox();
    	optionsBottom.setPadding(new Insets(15, 12, 15, 12));
    	optionsBottom.setSpacing(10);
    	optionsBottom.setStyle("-fx-background-color: #FFFFFF;");
    	optionsBottom.setAlignment(Pos.BOTTOM_CENTER);
    	optionsBottom.getChildren().add(returnButton);
    	
    	HBox optionsTop = new HBox();
    	optionsTop.setPadding(new Insets(15, 12, 12, 12));
    	optionsTop.setSpacing(10);
    	optionsTop.setStyle("-fx-background-color: #336699;");
    	optionsTop.setAlignment(Pos.CENTER);
    	optionsTop.getChildren().add(optionsLabel);
    	
    	
    	VBox optionsList = new VBox();
    	HBox themeBox = new HBox();
    	
    	optionsLayout.setBottom(optionsBottom);
    	optionsLayout.setLeft(optionsList);
    	optionsLayout.setTop(optionsTop);
    	
    	optionsScene = new Scene(optionsLayout, 600, 310);
    	
    	// The options will be presented as a group of radio buttons
    	ToggleGroup optionButtons = new ToggleGroup();
    	
    	// Clicking the top of the options page will activate test mode
    	// This will allow me to run the unit tests on a saved version of the hnh website
    	optionsTop.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (optionButtons.getSelectedToggle() != null) {
					optionButtons.getSelectedToggle().setSelected(false);
				}
				
				searchChoice = 4;
			}
		});
    	
    	// Dragging on the top of the options page will also activate test mode
    	optionsTop.setOnDragDetected(new EventHandler<Event>() {
    		@Override
			public void handle(Event event) {
				if (optionButtons.getSelectedToggle() != null) {
					optionButtons.getSelectedToggle().setSelected(false);
				}

				searchChoice = 5;
			}
    	});
    	
    	RadioButton defaultSearch = new RadioButton("Default Search");
    	defaultSearch.setToggleGroup(optionButtons);
    	// Have the defaultSearch button be selected by default
    	defaultSearch.setSelected(true);
    	defaultSearch.setPadding(new Insets(10, 10, 10, 5));
    	defaultSearch.setOnAction(new EventHandler<ActionEvent>() {
    		
    		/**
    		 * Sets the default search option the first radio button to be automatically selected
    		 *
    		 * @param event the event that triggers this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				defaultSearch.setSelected(true);
				searchChoice = 1;
			}
    		
		});
    	
    	RadioButton artistSearch = new RadioButton("Search By Artist Name");
    	artistSearch.setToggleGroup(optionButtons);
    	artistSearch.setPadding(new Insets(10, 10, 10, 5));
    	artistSearch.setOnAction(new EventHandler<ActionEvent>() {
    		/**
    		 * Sets the search option to be the artist search
    		 *
    		 * @param event the event that triggers this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				artistSearch.setSelected(true);
				searchChoice = 2;
			}
    		
		});
    	
    	RadioButton alphabeticalSearch = new RadioButton("Sort Results Alphabetically");
    	alphabeticalSearch.setToggleGroup(optionButtons);
    	alphabeticalSearch.setPadding(new Insets(10, 10, 10, 5));
    	alphabeticalSearch.setOnAction(new EventHandler<ActionEvent>() {
    		
    		/**
    		 * Sets the search option to be the alphabetical search
    		 *
    		 * @param event the event that triggers this method
    		 */
			@Override
			public void handle(ActionEvent event) {
				alphabeticalSearch.setSelected(true);
				searchChoice = 3;
			}
    		
		});
    	
    	// The list of theme color choices
    	ObservableList<String> options = FXCollections.observableArrayList("Blue", "Light Blue", "Red", "Green", "Orange", "Pink", "Black", "Purple", "Gray");
    	ComboBox<String> comboBox = new ComboBox<String>(options);
    	comboBox.setVisibleRowCount(2);

    	themeBox.getChildren().addAll(themeLabel, comboBox);
    	
    	// Add the buttons to the optionsList
    	optionsList.getChildren().addAll(defaultSearch, artistSearch, alphabeticalSearch, themeBox);
    	
    	// Sets the default search option
    	if (defaultSearch.isSelected()) {
    		searchChoice = 1;
    	}
    	
    	// Handles switching the theme CSS
    	comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

    		/**
    		 * Changes the CSS for the program to the appropriate sheet for the selected color
    		 *
    		 * @param observable The list of choices
    		 * @param oldValue the previous theme color choice
    		 * @param newValue the new theme color choice
    		 */
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				returnButton.setStyle("-fx-background-color: " + changeTheme(newValue) + ";" + "-fx-text-fill: #FFFFFF;");
				resultsReturn.setStyle("-fx-background-color: " + changeTheme(newValue) + ";" + "-fx-text-fill: #FFFFFF;");
				optionsTop.setStyle("-fx-background-color: " + changeTheme(newValue) + ";");
				resultsTop.setStyle("-fx-background-color: " + changeTheme(newValue) + ";");
				bottom.setStyle("-fx-background-color: " + changeTheme(newValue) + ";");
				searchButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: " + changeTheme(newValue) + ";");
				optionsButton.setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: " + changeTheme(newValue) + ";");
				findMusicLabel.setStyle("-fx-text-fill: " + changeTheme(newValue) + ";" + "-fx-font-size: 25px;" + "-fx-font-weight: bold;");
				themeLabel.setStyle("-fx-text-fill: " + changeTheme(newValue) + ";" + "-fx-font-size: 14px;" + "-fx-font-weight: normal;");
				
				if (newValue.equals("Light Blue")) {
					mainScene.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
			    	optionsScene.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
			    	resultsScene.getStylesheets().add(SearchGUI.class.getResource("materialLightBlue.css").toExternalForm());
				} else {
					mainScene.getStylesheets().add(SearchGUI.class.getResource("material" + newValue + ".css").toExternalForm());
			    	optionsScene.getStylesheets().add(SearchGUI.class.getResource("material" + newValue + ".css").toExternalForm());
			    	resultsScene.getStylesheets().add(SearchGUI.class.getResource("material" + newValue + ".css").toExternalForm());
				}
				themeColor = newValue;
			}
		});
    	
    	// Set the CSS of the program to use Blue as the default color
    	mainScene.getStylesheets().add(SearchGUI.class.getResource("materialBlue.css").toExternalForm());
    	optionsScene.getStylesheets().add(SearchGUI.class.getResource("materialBlue.css").toExternalForm());
    	resultsScene.getStylesheets().add(SearchGUI.class.getResource("materialBlue.css").toExternalForm());
    	
    	
    	// Set the mainScene as the default scene to display on program start
    	primaryStage.setScene(mainScene);
    	primaryStage.show();
    }
}