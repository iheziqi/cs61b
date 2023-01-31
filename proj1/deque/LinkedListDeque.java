package deque;


public class LinkedListDeque <T> {
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
    public void addFirst(T item) {
        sentinel.next.prev = new Node(item, sentinel, sentinel.next);
        sentinel.next = sentinel.next.prev;
        size += 1;
    }

    /**
     * Adds an item of type T to the back of deque. Item is never null.
     * @Params T item
     * */
    public void addLast(T item) {
    }

    /**
     * Returns true if deque is empty, false otherwise.
     * @return boolean
     * */
    public boolean isEmpty() {
        if (sentinel.next == sentinel.prev) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of items in the deque.
     * @return int
     * */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     * */
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
    public T removeFirst() {
        return null;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return T
     * */
    public T removeLast() {
       return null;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item and so forth.
     * If no such item exists, returns null.
     * @return T or null
     * */
    public T get(int index) {
        return null;
    }

    /**
     * Returns an iterator
     * @return T
     * */
    public Iterable<T> iterator() {
        return null;
    }

    /**
     * Returns whether the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents in the same order.
     * @params Object o
     * @return boolean
     * */
    public boolean equals(Object o) {
        return false;
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> deque = new LinkedListDeque<Integer>();
        deque.printDeque();
        deque.addFirst(99);
        deque.addFirst(98);
        deque.addFirst(97);
        deque.addFirst(96);
        deque.addFirst(95);
        deque.addFirst(94);
        deque.addFirst(93);
        deque.printDeque();
    }
}
