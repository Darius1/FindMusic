<h1 align = "center"> FindMusic </h1>    
<div align = "center">
 
![Crates.io](https://img.shields.io/crates/l/rustc-serialize.svg)
![GitHub release](https://img.shields.io/badge/release-v1.0-red.svg)
[![Build Status](https://travis-ci.org/Darius1/FindMusic.svg?branch=master)](https://travis-ci.org/Darius1/FindMusic)
</div>
 
The Find Music program allows a user to quickly find music that has been released during the past 48 hours. There are 3 search options: default, search by artist name, and a search that orders the results alphabetically. Currently the only website supported is http://www.hotnewhiphop.com/.

The program uses the JSoup Java web scraping library to gather the song information from the website. The GUI for the program was created using JavaFX, and I plan on using MySQL to store the songs the user searches for in a database.

## Search Options
#### Default
![Default Search Gif](https://github.com/Darius1/FindMusic/raw/master/defaultsearch.gif)

The Default Search option allows the user to search for songs that were only released today or search for songs that were released both yesterday and today.

#### Artist Search
![Artist Search Gif](https://github.com/Darius1/FindMusic/raw/master/artistsearch.gif)

The Artist Search option allows the user to specify an artist to look for and the program returns any songs that artist has appeared on in the past 48 hours.

#### Sort Search Results Alphabetically
![Alphabetical Search Gif](https://github.com/Darius1/FindMusic/raw/master/abcearch.gif)

The Alphabetical Search option performs a full default search and orders the results alphabetically.

## Themes
![Program Themes Gif](https://github.com/Darius1/FindMusic/raw/master/themes.gif)

There are 9 program colors to choose from. Blue, Light Blue, Red (Go Pack!), Green, Orange, Pink, Black, Purple, and Gray.

 
## Future Ideas
* Display additional song info when the song is clicked on the results screen
* Add links to the music that the user can use to go directly to the song to stream it
* Add links to download locations for the songs that the user can directly access
* Add the ability to search for multiple artists at once
* Support for more websites
* Ability to search for songs by date
* Predictive Search/Autocomplete
* Previous search history storage
* Export to txt or csv
