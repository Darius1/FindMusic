package personal.darius.dataStructures;

/**
 * The ArrayList class holds all of the items used throughout this program
 * 
 * @author Darius McFarland
 *
 * @param <E> Generic Object placeholder
 */
public class ArrayList<E extends Comparable<E>> {

	private E[] list;
	
	private static final int INITIAL_SIZE = 30;
	
	private int size;
	
	private int capacity;
	
	/**
	 * Constructs a new arrayList with an initial size of 30
	 */
	@SuppressWarnings("unchecked")
	public ArrayList() {
		list = (E[]) new Comparable[INITIAL_SIZE];
		capacity = INITIAL_SIZE;
		size = 0;
	}
	
	/**
	 * Adds an item to the list
	 * 
	 * @param item the item that will be added to the list
	 */
	public void insert(E item) {
		if (size == capacity) {
			growArray();
		}
		
		list[size] = item;
		size++;
		
	}

	/**
	 * Getter method for the size instance variable
	 *
	 * @return the size of the list
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Doubles the capacity of the array
	 * 
	 * @author Darius McFarland
	 */
	@SuppressWarnings("unchecked")
	private void growArray() {
		capacity *= 2;
		E[] temp = list;
		list = (E[]) new Comparable[capacity];
		
		for (int i = 0; i < temp.length; i++) {
			list[i] = temp[i];
		}
	}

	/**
	 * Gets the item at the specified index
	 * 
	 * @param index the location the item will be retrieved from
	 * 
	 * @return the item at the index
	 */
	public E get(int index) {
		return list[index];
	}

	/**
	 * Searches the list for the specified item
	 *
	 * @param item the item being looked for
	 * 
	 * @return true if the list has the item
	 */
	public boolean contains(E item) {
		for (int i = 0; i < size; i++) {
			if (list[i].compareTo(item) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the list is empty
	 *
	 * @return true if the size of the list is 0
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Replaces the item at the index with the item passed in as a parameter
	 *
	 * @param index the index in the list that will have its item replaced
	 * @param item the item being inserted into the list
	 */
	public void set(int index, E item) {
		list[index] = item;
		
	}

}