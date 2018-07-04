package personal.darius.search;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
 * This class will handle all of the search functionality in the FindMusic program
 * @author Darius McFarland
 */
public class Search extends Application {
	
    /** Holds the website url that will be searched */
    private String website;
    
    /** Holds all of the scraped songs and sorts them alphabetically */
    private ArrayList<Song> sortedSongs;
    
    Scene mainScene;
    
    Scene resultsScene;
    
    Scene optionsScene;
    
    ListView<Song> songList;
    
    ObservableList<Song> searchedSongs = FXCollections.observableArrayList();
    
    int searchChoice;

    /**
     * Searches a website using an artist's name and current date as parameters to find new music
     * released by the artist for that day
     * @param  artist the artist's name
     * @param  website the website url
     * 
     * @return true if the artist released music on that date, false otherwise
     */
    public boolean searchForRelease(String artist, String website, Stage primaryStage) {
        try {
        	// Don't allow the user to enter a blank artist name
        	if (artist.equals("")) {
        		createInvalidArtistNamePopup(primaryStage);
        		return false;
        	}
        	
            Document doc = Jsoup.connect(website).get();
            Elements findSong = doc.select(".dailySongChart-item");

            // Example formatted date: Monday Jan 1, 2000		
			LocalDate date = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
			String currentDate = date.format(formatter);
			
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
    	
    	try {
			Document doc = Jsoup.connect(website).get();
			Elements findSong = doc.select(".dailySongChart-item");

			songsReleasedToday = findSong.size();
			
			for (int i = 0; i < songsReleasedToday; i++) {
				
				// Example formatted date: Monday Jan 1, 2000		
				LocalDate date = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
				String currentDate = date.format(formatter);

				// HotNewHipHop places the dailySongChart-day-date class tag on the very first song posted each day 
				// Need to check this tag to get the date information
				if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && !checkedYesterday) {
					if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(currentDate)) {
						
						dateChecked = true;
					} else {
						if (!dateChecked) {
							System.out.println("no songs released today.");
						}

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
    public void printSongs() {
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
 
        Optional<ButtonType> choice = alert.showAndWait();
        
        if (choice.get() == yes) {
        	return true;
        } else {
        	return false;
        }
    }
    
    private String createSearchByArtistNamePopup() {
    	TextInputDialog dialog = new TextInputDialog();
    	 
    	dialog.setTitle("Artist Search");
    	dialog.setHeaderText(null);
    	dialog.setContentText("What artist would you like to search for?");
    	 
    	Optional<String> result = dialog.showAndWait();
    	
    	return result.get();
    }
    
    private void createInvalidArtistNamePopup(Stage primaryStage) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(null);
 
        alert.setHeaderText(null);
        
        // Gets rid of the default ok button
        alert.getButtonTypes().clear();
        alert.setContentText("Invalid Artist Name");
        
        ButtonType ok = new ButtonType("Ok");
        
        alert.getButtonTypes().addAll(ok);
 
        Optional<ButtonType> choice = alert.showAndWait();
        
        if (choice.get() == ok) {
        	primaryStage.setScene(mainScene);
        } 
    }
    
    private void createNoSongsByArtistPopup(String artist, Stage primaryStage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(null);
 
        alert.setHeaderText(null);
        
        // Gets rid of the default ok button
        alert.getButtonTypes().clear();
        alert.setContentText(artist + " has not released any music today.");
        
        ButtonType ok = new ButtonType("Ok");
        
        alert.getButtonTypes().addAll(ok);
 
        Optional<ButtonType> choice = alert.showAndWait();
        
        if (choice.get() == ok) {
        	primaryStage.setScene(mainScene);
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
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	primaryStage.setTitle("Find Music version 1.0");
  	
    	Label findMusicLabel = new Label("Find Music");
    	
    	Label optionsLabel = new Label("Search Options");
    	
    	Label resultsLabel = new Label("Search Results");
    	
    	Button searchButton = new Button("Search");
    	searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				sortedSongs = new ArrayList<Song>();
				
				// Clears out the list view so the program will start "fresh" each time search is
				// pressed
				if (!searchedSongs.isEmpty()) {
					searchedSongs.clear();
				}
				
				if (searchChoice == 1) {
					findNewSongs("http://www.hotnewhiphop.com");
					primaryStage.setScene(resultsScene);
				} else if (searchChoice == 2) {
					String artist = createSearchByArtistNamePopup();
					// Will throw a NoSuchElement Exception if the user presses cancel when the
					// artist search popup appears
					try {
						searchForRelease(artist, "http://www.hotnewhiphop.com", primaryStage);
					} catch (NoSuchElementException e) {
						// Return to the main menu
					}
					
					if (searchedSongs.isEmpty()) {
						// display a popup letting the user no their selected artist hasn't
						// released anything today
						createNoSongsByArtistPopup(artist, primaryStage);
					} else {
						primaryStage.setScene(resultsScene);
					}
				} else if (searchChoice == 3) {
					findNewSongs("http://www.hotnewhiphop.com");
					printSongs();
					primaryStage.setScene(resultsScene);
				}
			}
		});
    	searchButton.setPrefSize(100, 20);
    	
    	Button optionsButton = new Button("Options");
    	optionsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(optionsScene);
			}
		});
    	optionsButton.setPrefSize(100, 20);
    	
    	Button returnButton = new Button("Return to Main Menu");
    	returnButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(mainScene);
			}
		});
    	returnButton.setPrefSize(200, 20);
    	
    	Button returnButton2 = new Button("Return to Main Menu");
    	returnButton2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(mainScene);
			}
		});
    	returnButton2.setPrefSize(200, 20);
    	
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
    	
    	mainScene = new Scene(mainLayout, 500, 250);
    	
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
    	resultsBottom.setStyle("-fx-background-color: #336699;");
    	resultsBottom.setAlignment(Pos.BOTTOM_CENTER);
    	resultsBottom.getChildren().add(returnButton2);
    	
    	HBox resultsTop = new HBox();
    	resultsTop.setPadding(new Insets(15, 12, 15, 12));
    	resultsTop.setSpacing(10);
    	resultsTop.setAlignment(Pos.CENTER);
    	resultsTop.getChildren().add(resultsLabel);
    	
    	resultsLayout.setBottom(resultsBottom);
    	resultsLayout.setCenter(songResults);
    	resultsLayout.setTop(resultsTop);
    	
    	
    	resultsScene = new Scene(resultsLayout, 500, 250);
    	
    	
    	BorderPane optionsLayout = new BorderPane();
    	
    	HBox optionsBottom = new HBox();
    	optionsBottom.setPadding(new Insets(15, 12, 15, 12));
    	optionsBottom.setSpacing(10);
    	optionsBottom.setStyle("-fx-background-color: #336699;");
    	optionsBottom.setAlignment(Pos.BOTTOM_CENTER);
    	optionsBottom.getChildren().add(returnButton);
    	
    	HBox optionsTop = new HBox();
    	optionsTop.setPadding(new Insets(15, 12, 15, 12));
    	optionsTop.setSpacing(10);
    	optionsTop.setStyle("-fx-background-color: #336699;");
    	optionsTop.setAlignment(Pos.BOTTOM_CENTER);
    	optionsTop.getChildren().add(optionsLabel);
    	
    	
    	VBox optionsList = new VBox();
    	
    	optionsLayout.setBottom(optionsBottom);
    	optionsLayout.setLeft(optionsList);
    	optionsLayout.setTop(optionsTop);
    	
    	optionsScene = new Scene(optionsLayout, 500, 250);
    	
    	// The options will be presented as a group of radio buttons
    	ToggleGroup optionButtons = new ToggleGroup();
    	
    	RadioButton defaultSearch = new RadioButton("Default Search");
    	defaultSearch.setToggleGroup(optionButtons);
    	// Have the defaultSearch button be selected by default
    	defaultSearch.setSelected(true);
    	defaultSearch.setPadding(new Insets(10, 10, 10, 5));
    	defaultSearch.setOnAction(new EventHandler<ActionEvent>() {
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
			@Override
			public void handle(ActionEvent event) {
				alphabeticalSearch.setSelected(true);
				searchChoice = 3;
			}
    		
		});
    	
    	// Add the buttons to the optionsList
    	optionsList.getChildren().addAll(defaultSearch, artistSearch, alphabeticalSearch);
    	
    	if (defaultSearch.isSelected()) {
    		searchChoice = 1;
    	}
    	
    	
    	// Set the mainScene as the default scene to display on program start
    	primaryStage.setScene(mainScene);
    	primaryStage.show();
    }
}