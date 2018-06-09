package personal.darius.sort;

import java.util.Comparator;

import personal.darius.dataStructures.ArrayList;

/**
 * The Sorter Class handles all of the sorting for the program
 * 
 * 
 * @author Darius McFarland
 *
 * @param <E> Generic Java object placeholder
 */
public class Sorter<E> {
	
	private AlphaComparator a;
	
	/**
	 * Creates a Sorter object
	 */
	public Sorter() {
		new ArrayList<Song>();
		a = new AlphaComparator();
	}
	
	/** 
	   * Sorts the list alphabetically 
	   * 
	   * The code for this method is based on the mergeSort code on slide 33 of the Dictionaries  
	   * Powerpoint 
	   * 
	   * @param list the arraylist that will be sorted 
	   * @return the alphabetically sorted list 
	   */ 
	  public ArrayList<Song> alphabeticalSort(ArrayList<Song> list) { 
	    ArrayList<Song> left = new ArrayList<Song>(); 
	    ArrayList<Song> right = new ArrayList<Song>(); 
	     
	    if (list.size() > 1) { 
	      int middle = list.size() / 2; 
	
	       
	      for (int i = 0; i < list.size(); i++) { 
	        if (i < middle) { 
	          left.insert(list.get(i)); 
	        } else { 
	          right.insert(list.get(i)); 
	        } 
	      } 
	       
	      alphabeticalSort(left); 
	      alphabeticalSort(right); 
	       
	      list = merge(list, left, right, a); 
	    } 
	     
	     
	    return list; 
	  } 
	
	/**
	 * Handles merging the lists back into one
	 * 
	 *
	 * @param list the entire list
	 * @param left the left sublist
	 * @param right the right sublist
	 * @param c the Comparator object
	 * 
	 * @return the merged lists in sorted order
	 */
	private ArrayList<Song> merge(ArrayList<Song> list, ArrayList<Song> left, ArrayList<Song> right, Comparator<Song> c) {
		int leftIndex = 0;
		int rightIndex = 0;
		
		for (int i = 0; i < list.size(); i++) {
			if (rightIndex >= right.size() || (leftIndex < left.size() && c.compare(left.get(leftIndex), right.get(rightIndex)) <= 0)) {
				list.set(i, left.get(leftIndex));
				leftIndex++;
			} else {
				list.set(i, right.get(rightIndex));
				rightIndex++;
			}
		}
		return list;
	}
	
}
