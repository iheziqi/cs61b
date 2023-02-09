package deque;

public class ArrayDeque <T> {
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;

    /**
     * Constructor of ArrayDeque
     * */
    public ArrayDeque() {
        size = 0;
        nextFirst = 7;
        nextLast = 8;
        items = (T []) new Object[15];
    }

    /**
     * Checks whether the given position in items is empty
     * @param position: an integer of position
     * */
    private  boolean checkNullInArray(int position) {
        if (items[position] == null) {
            return true;
        }
        return false;
    }

    /**
     * Returns the length of array
     * */
    private int getLength() {
        return items.length;
    }

    /**
     * Changes nextFirst index
     * */
    private void setNextFirst() {
        nextFirst --;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
    }

    /**
     * Changes nextLast index
     * */
    private void setNextLast() {
        nextLast ++;
        if (nextLast > items.length - 1) {
            nextLast = 0;
        }
    }

    private int getFirstIndex() {
        int currentFirst = nextFirst + 1;
        if (currentFirst > items.length - 1) {
            currentFirst = 0;
        }
        return currentFirst;
    }

    private int getLastIndex() {
        int currentLast = nextLast - 1;
        if (currentLast < 0) {
            currentLast = items.length - 1;
        }
        return currentLast;
    }

    private int getNewNextFirst(int first) {
        if (first + 1 > items.length - 1) {
            return 0;
        }
        return first + 1;
    }

    /**
     * Resizes the size of array proportional to the number of items
     * */
    private void resize() {
        T[] newItemsArray = (T []) new Object[getLength() * 2];

        int p = getNewNextFirst(nextFirst);
        for (int i = 0; i < items.length; i++) {
            newItemsArray[i] = items[p];
            p = getNewNextFirst(p);
        }

        nextFirst = newItemsArray.length - 1;
        nextLast = size;
        items = newItemsArray;
    }

    /**
     * Adds an item of type T to the front of the deque. Item is never null.
     * @param item
     * */
    public void addFirst(T item) {
        // check if resizing is needed
        if (!checkNullInArray(nextFirst)) {
            resize();
        }

        items[nextFirst] = item;
        setNextFirst();
        size ++;
    }

    /**
     * Adds an item of type T to the back of deque. Item is never null.
     * @param item
     * */
    public void addLast(T item) {
        // check if resizing is needed
        if (!checkNullInArray(nextLast)) {
            resize();
        }

        items[nextLast] = item;
        setNextLast();
        size ++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     * @return boolean
     * */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of items in the deque.
     * @return size
     * */
    public int size() { return size; }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     * */
    public void printDeque() {
        int p = getNewNextFirst(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[p] + " ");
            p = getNewNextFirst(p);
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return T
     * */
    public T removeFirst() {
        int currentFirst = getFirstIndex();

        T firstItem = items[currentFirst];
        if (firstItem == null) {
            return null;
        }

        items[currentFirst] = null;
        nextFirst = currentFirst;
        size --;

        return firstItem;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return T
     * */
    public T removeLast() {
        int currentLast = getLastIndex();

        T lastItem = items[currentLast];
        if (lastItem == null) {
            return null;
        }

        items[currentLast] = null;
        nextLast = currentLast;
        size --;

        return lastItem;
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> myAlist = new ArrayDeque<Integer>();

        int size = myAlist.getLength();
        for (int i = 0; i < 1000; i++) {
            myAlist.addLast(i);
        }
        myAlist.printDeque();

        for (int i = 0; i < 50; i++) {
            myAlist.removeLast();
        }
        myAlist.printDeque();
    }
}
