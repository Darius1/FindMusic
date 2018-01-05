package edu.ncsu.csc216.get_outdoors.util;

import java.util.Arrays;

/**
 * A Sorted Array List is a data structure that can hold objects and sort them
 * 
 * @author Darius McFarland
 * @author Bryce Martin
 *
 * @param <E> Generic Java placeholder object 
 */
public class SortedArrayList<E extends Comparable<E>> implements SortedList<E> {

	private static final int RESIZE = 10;
	
	private E[] list;
	
	private int size;
	
	/**
	 * Creates a new sortedArrayList
	 */
	@SuppressWarnings("unchecked")
	public SortedArrayList() {
		list = (E[]) new Comparable[RESIZE];
		size = 0;
	}
	
	/**
	 * Creates a new sortedArrayList with a specific size
	 *
	 * @param capacity the size of the list
	 */
	@SuppressWarnings("unchecked")
	public SortedArrayList(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Capacity cannot be less than 0.");
		}
		list = (E[]) new Comparable[capacity];
		size = 0;
	}
	
	/**
	 * Gets the size of the list
	 *
	 * @return the size of the list
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Determines if the list is empty
	 *
	 * @return true if the list is empty
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if the list contains the item specified by the parameter
	 *
	 * @param item the item being searched for
	 * @return true if the list contains the item
	 */
	public boolean contains(E item) {
		for (int i = 0; i < size(); i++) {
			if (item.compareTo(list[i]) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Adds the item to the list
	 *
	 * @param item the item being added
	 * @return true if the item can be added
	 */
	public boolean add(E item) {
//		if (size + 1 <= list.length) {
//        	growArray();
//        } 
		
		//System.out.println("Attempting to add: " + item.toString());
		if (size != 0) {
    		for (int i = 0; i < size; i++) {
	       		 if (contains(item)) {
	       			 throw new IllegalArgumentException("Cannot add duplicate objects");
	       		 }
    		}
    	}
		
		if (size == 0) {
			list[0] = item;
			size++;
			return true;
		}
		
		
		// Sorts the items in the list by alphabetical order by name
		
/*        for (int i = size - 1; i >= index; i--) {
        	if (list[i].compareTo(item) > 0) {
        		list[i + 1] = list[i];
        		System.out.println("i + 1 is: " + list[i + 1].toString());
        		list[i] = item;
        		System.out.println("i is: " + list[i].toString());
        	} else if (list[i].compareTo(item) < 0) {
        		list[i + 1] = item;
        		System.out.println("i + 1 when the item is after the compared is: " + list[i + 1].toString());
        	}
        }*/
		@SuppressWarnings("unchecked")
		E[] temp = (E[]) new Comparable[size() + 1];
		for (int i = 0; i < size(); i++) {
			temp[i] = list[i];
		}
		
		for (int i = size() - 1; i >= 0; i--) {
			if (item.compareTo(list[i]) < 0) {
				temp[i + 1] = temp[i];
				if (i == 0) {
					temp[i] = item;
				}
			} else {
				temp[i + 1] = item;
				break;
			}
		}
		list = temp;
		
        size++;
        
       /* for (int i = 0; i < size; i++) {
			System.out.println("After adding, the list currently contains: " + list[i].toString());
		}*/
        
        return true;
	}
	
	
//	private void growArray() {
//		int newCapacity = list.length * 2;
//        list = Arrays.copyOf(list, newCapacity);
//	}

	/**
	 * Gets the item at the specified index
	 *
	 * @param index the location in the list where the object is
	 * @return the item at the index
	 */
	public E get(int index) {
		if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        return list[index];
	}
	
	/**
	 * Removes the item at the specified index
	 *
	 * @param index the location in the list where the object is
	 * @return the item that was removed
	 */
	public E remove(int index) {
		if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
		
        E theRemovedElement = (E) list[index];
        
        for (int i = index; i < size() - 1; i++) {
            list[i] = list[i + 1];
        }
/*        System.out.println("Removed:" + theRemovedElement.toString());
        for (int i = 0; i < size; i++) {
			System.out.println("After removing, the list currently contains: " + list[i].toString());
		}*/
        size--;
        return theRemovedElement;
	}
	
	/**
	 * Gets the index in the list where the item is
	 * 
	 * @param item the item being searched for
	 * @return the location in the list where the item is
	 */
	public int indexOf(E item) {
		for (int i = 0; i < this.size; i++) {
			if (this.get(i).equals(item)) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * Creates an integer representing an object in memory
	 * 
	 * @return the integer representing the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(list);
		result = prime * result + size;
		return result;
	}

	/**
	 * Tests if two objects are equal
	 * 
	 * @return true if the objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		SortedArrayList other = (SortedArrayList) obj;
		if (!Arrays.equals(list, other.list))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	/**
	 * Creates a string representation of the array
	 * 
	 * @return the string containing information about items within the array;
	 */
	@Override
	public String toString() {
		String x = "";
		
		if (size > 0) {
			x += list[0].toString();
		}
		
		for (int i = 1; i < size; i++) {
			x += ", " + list[i].toString();
		}
		
		return  "[" + x + "]";
	}
}
