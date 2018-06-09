package personal.darius.sort;

import java.util.Comparator;

import personal.darius.data.Song;

/**
 * The AlphaComparator class compares two entries by their action and resource
 * 
 * This class is currently unnecessary, but it gave me a chance to use a new way of comparing
 * objects in Java
 * 
 * @author Darius McFarland
 *
 */
public class AlphaComparator implements Comparator<Song> {

	/**
	 * Overrides the default Song compareTo method to compare the Songs based on multiple criteria
	 *
	 * @return -1 if thisOne comes before comparedOne alphabetically. 1 if thisOne comes after
	 * comparedOne alphabetically, and 0 if thisOne and comparedOne are equal
	 */
	@Override
	public int compare(Song thisOne, Song comparedOne) {
		String thisSong = thisOne.toString();
		String comparedSong = comparedOne.toString();
		
		if (thisSong.compareTo(comparedSong) < 0) {
			return -1;
		} else if (thisSong.compareTo(comparedSong) > 0 ) {
			return 1;
		}
		return 0;
	}

}
