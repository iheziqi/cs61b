package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;

    /**
     * Constructor of ArrayDeque.
     * */
    public ArrayDeque() {
        size = 0;
        nextFirst = 7;
        nextLast = 8;
        items = (T []) new Object[15];
    }

    /**
     * Checks whether the given position in items is empty.
     * @param position: an integer of position
     * */
    private  boolean checkNullInArray(int position) {
        if (items[position] == null) {
            return true;
        }
        return false;
    }

    /**
     * Returns the length of array.
     * */
    private int getLength() {
        return items.length;
    }

    /**
     * Changes nextFirst index.
     * */
    private void setNextFirst() {
        nextFirst--;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
    }

    /**
     * Changes nextLast index.
     * */
    private void setNextLast() {
        nextLast++;
        if (nextLast > items.length - 1) {
            nextLast = 0;
        }
    }

    /**
     * Gets the index of the first element in list.
     * @return the integer index of current first element
     * */
    private int getFirstIndex() {
        int currentFirst = nextFirst + 1;
        if (currentFirst > items.length - 1) {
            currentFirst = 0;
        }
        return currentFirst;
    }

    /**
     * Gets the index of the last element in list.
     * @return the integer index of current last element
     * */
    private int getLastIndex() {
        int currentLast = nextLast - 1;
        if (currentLast < 0) {
            currentLast = items.length - 1;
        }
        return currentLast;
    }

    /***
     * Gets the next index in the loop through list.
     * Because array list is circular, when loop through this list, the next index will
     * sometimes be out of bound of array. This method checks this problem and get the right index.
     * @param index
     * @return the next index integer
     */
    private int getNewNextFirst(int index) {
        if (index + 1 > items.length - 1) {
            return 0;
        }
        return index + 1;
    }

    /**
     * Resizes the size of array proportional to the number of items.
     * This method resizes array to bigger array.
     * */
    private void resize() {
        T[] newItemsArray = (T []) new Object[items.length * 2];

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
     * Checks and resizes the size of array down if it is needed.
     * Because the usage factor should be always lower the 25%.
     */
    private void downsize() {
        double usage = (double) size / (double) items.length;
        if (usage < 0.25 && items.length >= 16) {
            T[] newItemsArray = (T[]) new Object[items.length / 2];

            int p = getNewNextFirst(nextFirst);
            for (int i = 0; i < size; i++) {
                newItemsArray[i] = items[p];
                p = getNewNextFirst(p);
            }
            nextFirst = newItemsArray.length - 1;
            nextLast = size;
            items = newItemsArray;
        }
    }

    /**
     * Adds an item of type T to the front of the deque. Item is never null.
     * @param item
     * */
    @Override
    public void addFirst(T item) {
        // check if resizing is needed
        if (!checkNullInArray(nextFirst)) {
            resize();
        }

        items[nextFirst] = item;
        setNextFirst();
        size++;
    }

    /**
     * Adds an item of type T to the back of deque. Item is never null.
     * @param item
     * */
    @Override
    public void addLast(T item) {
        // check if resizing is needed
        if (!checkNullInArray(nextLast)) {
            resize();
        }

        items[nextLast] = item;
        setNextLast();
        size++;
    }

    /**
     * Returns the number of items in the deque.
     * @return size
     * */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     * */
    @Override
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
    @Override
    public T removeFirst() {
        int currentFirst = getFirstIndex();

        T firstItem = items[currentFirst];
        if (firstItem == null) {
            return null;
        }

        items[currentFirst] = null;
        nextFirst = currentFirst;
        size--;
        downsize();

        return firstItem;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return T
     * */
    @Override
    public T removeLast() {
        int currentLast = getLastIndex();

        T lastItem = items[currentLast];
        if (lastItem == null) {
            return null;
        }

        items[currentLast] = null;
        nextLast = currentLast;
        size --;
        downsize();

        return lastItem;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item and so forth.
     * If no such item exists, returns null.
     * @return T or null
     * */
    @Override
    public T get(int index) {
        int currentFirstItemIndex = getFirstIndex();
        int returnItemIndex = currentFirstItemIndex + index;
        if (returnItemIndex > items.length - 1) {
            returnItemIndex = returnItemIndex - items.length;
        }
        return items[returnItemIndex];
    }

    /**
     * Returns an iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    /**
     * Implements array iterator
     */
    private class ArrayIterator implements Iterator<T> {
        private int wizPos;
        private int p;

        public ArrayIterator() {
            wizPos = getFirstIndex();
            p = 0;
        }

        public boolean hasNext() {
            return p < size;
        }

        public T next() {
            T returnItem = items[wizPos];
            wizPos = getNewNextFirst(wizPos);
            p++;
            return returnItem;
        }

    }

    /**
     * Returns whether the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents
     * (as goverened by the generic T’s equals method) in the same order.
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof ArrayDeque aArray) {
            // check arrays are of the same size
            if (aArray.size != this.size) {
                return false;
            }

            // check that all of My items are in the other array
            for (int i = 0; i < this.size; i++) {
                if (items[i] != aArray.items[i]) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
