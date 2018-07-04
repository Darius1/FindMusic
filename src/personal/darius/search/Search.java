package personal.darius.search;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import personal.darius.data.Song;
import personal.darius.dataStructures.ArrayList;
import personal.darius.database.SongStorage;
import personal.darius.sort.Sorter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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
    
    /** Reads the arguments supplied by the user on the command line */
    private CommandLine cmd;
    
    Scene mainScene;
    
    Scene resultsScene;
    
    Scene optionsScene;
    
    ListView<Song> songList;
    
    ObservableList<Song> searchedSongs = FXCollections.observableArrayList();

    /**
     * Searches a website using an artist's name and current date as parameters to find new music
     * released by the artist for that day
     * @param  artist the artist's name
     * @param  website the website url
     * 
     * @return true if the artist released music on that date, false otherwise
     */
    public boolean searchForRelease(String artist, String website) {
        try {
            Document doc = Jsoup.connect(website).get();
            Elements findSong = doc.select("div.cover-title");
            Elements findArtist = doc.select("div.dailySongChart-artist");
            int numberOfSongsToday = 0;

            for (int i = 0; i < findSong.size(); i++) {
            	if (findArtist.get(i).text().contains(artist)) {
            		System.out.println("Song: " + findSong.get(i).text());
            		System.out.println("Artist: " + findArtist.get(i).text());
            		numberOfSongsToday++;
            	}
            }

            System.out.println("\nNumber of songs " + artist + " released today: " + numberOfSongsToday);

            if (numberOfSongsToday == 0) {
        		System.out.println("The artist you selected has not released any music today.");
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

						if (showSongsFromYesterday()) {
							if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(yesterday())) {
								checkedYesterday = true;
							}
						} else {
							break;
						}
						
					}
				} else if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && checkedYesterday) {
					break;
				} else {
					
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
     * Fetches the songs released today and yesterday with no system output
     *
     * @param website the website the songs will be scraped from
     */
    public void findNewSongsNoPrompts(String website) {
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
					}
					if (findSong.get(i).getElementsByClass("dailySongChart-day-date").text().equals(yesterday())) {
						checkedYesterday = true;
					}
				} else if (findSong.get(i).getElementsByClass("dailySongChart-day-date").size() == 1 && checkedYesterday) {
					break;
				} else {
				}
				
				if (findSong.size() > 0) {
					if (checkedYesterday) {
						sortedSongs.insert(formattedSong(findSong.get(i), yesterday()));
					} else {
						sortedSongs.insert(formattedSong(findSong.get(i), currentDate));
					}
				}
				
			}
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
    	
    	for (int i = 0; i < sortedSongs.size(); i++) {
    		System.out.println(i + 1 + ".\t" + sortedSongs.get(i));
    	}
    }
    
    /**
     * Initializes the command line interface
     *
     * @param args the arguments needed
     * @return true if the interface is created without error
     */
    public boolean setupCLI(String[] args) {
    	Options findMusicOptions = new Options();
    	
    	findMusicOptions.addOption("s", "search", false, "search for songs released today");
    	findMusicOptions.addOption("h", "help", false, "displays usage information");
    	findMusicOptions.addOption("a", "artist", true, "searches for newly released music by the specified artist");
    	findMusicOptions.addOption("v", "version", false, "displays the version information");
    	findMusicOptions.addOption("o", "order", false, "orders the released songs alphabetically and displays them");
    	
    	CommandLineParser parser = new DefaultParser();
    	
    	try {
    		cmd = parser.parse(findMusicOptions, args);
    		
    		// Prints the help message
    		if (cmd.hasOption("h")) {
    			throw new ParseException("");
    		}
    		
    		// Prevents the user from running the program with no arguments
    		if (cmd.getOptions().length == 0) {
    			throw new ParseException("");
    		}
    	} catch (ParseException e) {
    		HelpFormatter formatter = new HelpFormatter();
    		String footer = "\nPlease report issues at https://github.com/Darius1/FindMusic/issues";
    		
    		formatter.printHelp("Search", null, findMusicOptions, footer, true);
    		return false;
    	}
    	
    	return true;
    	
    }
    
    /**
     * Runs the program based off of the arguments provided
     *
     * @param args the command line arguments entered by the user
     */
    public void parseInput(String[] args) {
    	if (cmd.hasOption("s")) {
    		findNewSongs("http://www.hotnewhiphop.com");
    	} else if (cmd.hasOption("h")) {
    		//print help information
    	} else if (cmd.hasOption("a")) {
    		searchForRelease(args[1], "http://www.hotnewhiphop.com");
    	} else if (cmd.hasOption("v")) {
    		System.out.println("FindMusic Version 1.0 Initial Release");
    	} else if (cmd.hasOption("o")) {
    		findNewSongsNoPrompts("http://www.hotnewhiphop.com");
    		printSongs();
    	}
    }
    
    /**
     * Creates a dialog box that gives the user the choice to display songs that released yesterday
     *
     * @return true if the user clicks yes, no otherwise
     */
    private boolean showSongsFromYesterday() {
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
    	primaryStage.setTitle("Find Music");
  	
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
				
				findNewSongs("http://www.hotnewhiphop.com");
				primaryStage.setScene(resultsScene);
				
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
    	
    	RadioButton artistSearch = new RadioButton("Search By Artist Name");
    	artistSearch.setToggleGroup(optionButtons);
    	artistSearch.setPadding(new Insets(10, 10, 10, 5));
    	
    	RadioButton alphabeticalSearch = new RadioButton("Sort Results Alphabetically");
    	alphabeticalSearch.setToggleGroup(optionButtons);
    	alphabeticalSearch.setPadding(new Insets(10, 10, 10, 5));
    	
    	// Add the buttons to the optionsList
    	optionsList.getChildren().addAll(defaultSearch, artistSearch, alphabeticalSearch);
    	
    	
    	// Set the mainScene as the default scene to display on program start
    	primaryStage.setScene(mainScene);
    	primaryStage.show();
    }
}