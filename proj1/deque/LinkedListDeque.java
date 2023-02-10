package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class Node {
        public Node prev;
        public T item;
        public Node next;
        public Node(T t, Node p, Node n) {
            prev = p;
            item = t;
            next = n;
        }

    }

    /* The first item (if exists) is at sentinel.next */
    private Node sentinel;
    private int size;

    /**
     * Initialize the deque.
     * */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        // the first node points to itself
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
     * Adds an item of type T to the front of the deque. Item is never null.
     * @params T item
     * */
    @Override
    public void addFirst(T item) {
        sentinel.next.prev = new Node(item, sentinel, sentinel.next);
        sentinel.next = sentinel.next.prev;
        size += 1;
    }

    /**
     * Adds an item of type T to the back of deque. Item is never null.
     * @Params T item
     * */
    @Override
    public void addLast(T item) {
        sentinel.prev.next = new Node(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    /**
     * Returns the number of items in the deque.
     * @return int
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
        if (isEmpty()) {
            System.out.println("The deque is empty!");
        }
        Node p = sentinel.next;
        for (int i = 0; i < size(); i++) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.print("\n");
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return T
     * */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T firstItem = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return firstItem;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return T
     * */
    @Override
    public T removeLast() {
        if(isEmpty()) {
            return null;
        }
        T lastItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return lastItem;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item and so forth.
     * If no such item exists, returns null.
     * @return T or null
     * */
    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }

        if (index > size - 1) {
            return null;
        }

        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    /**
     * Using recursive to get the item at the given index, where 0 is the front, 1 is the next item and so forth.
     * If no such item exists, returns null.
     * @return T or null
     * */
    public  T getRecursive(int index) {
        if (isEmpty()) {
            return null;
        }

        if (index > size - 1) {
            return null;
        }

        return getRecursiveHelper(sentinel.next, index);
    }

    /**
     * Helper method for getRecursive(int index)
     * */
    private T getRecursiveHelper(Node node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getRecursiveHelper(node.next, index - 1);
    }

    /**
     * Returns an iterator
     * @return Iterator<T>
     * */
    @Override
    public Iterator<T> iterator() {
        return  new dequeSetIterator();
    }

    /**
     * Sets the deque iterator.
     * It checks the existence of next item in list and return this item (if exists).
     * @return T item (if exists)
     * */
    private class dequeSetIterator implements Iterator<T> {
        private int wizPos;
        private Node p;
        public dequeSetIterator() {
            wizPos = 0;
            p = sentinel;
        }
        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            p = p.next;
            T returnItem = p.item;
            wizPos += 1;
            return returnItem;
        }
    }

    /**
     * Returns whether the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents in the same order.
     * @param Object o
     * @return boolean
     * */
    public boolean equals(Object o) {
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }

        int oSize = ((LinkedListDeque<?>) o).size;
        if (oSize != size) {
            return false;
        }

        Node p = sentinel;
        Node op = (Node) ((LinkedListDeque<?>) o).sentinel;
        for (int i = 0; i < size; i++) {
           p = p.next;
           op = op.next;
           if (p.item != op.item) {
               return false;
           }
        }

        return true;
    }
}
