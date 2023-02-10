package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /* Tests addFirst */
    public void addFirstTest() {
        ArrayDeque<Integer> Al1 = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            Al1.addFirst(i);
        }
        for (int i = 999; i >= 0; i--) {
            assertEquals(i, (int) Al1.removeFirst());
        }
    }

    @Test
    /* Tests removeFirst */
    public void removeFirstTest() {
        ArrayDeque<Integer> Al1 = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            Al1.addFirst(i);
        }

        for (int i = 999; i >= 0; i--) {
            assertEquals((int) Al1.removeFirst(), i);
        }
    }

    @Test
    /* Tests removeLast */
    public void removeLastTest() {

        ArrayDeque<Integer> Al1 = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            Al1.addLast(i);
        }

        for (int i = 999; i >0; i--) {
            assertEquals((int) Al1.removeLast(), i);
        }

    }

    @Test
    /* Tests get */
    public void getTest() {

        ArrayDeque<Integer> Al1 = new ArrayDeque<>();
        for (int i = 0; i < 1000; i++) {
            Al1.addLast(i);
        }
        assertEquals(999, (int) Al1.get(999));
        assertEquals(0, (int) Al1.get(0));
        assertEquals(500, (int) Al1.get(500));
    }

    @Test
    /* randomized test.*/
    public void randomizedTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);

                lld1.addLast(randVal);
                lld2.addLast(randVal);

                lld1.addFirst(randVal);
                lld2.addFirst(randVal);

            } else if (operationNumber == 1) {
                // size
                int sizeOfLld1 = lld1.size();
                int sizeOfLld2 = lld2.size();

                assertEquals(sizeOfLld1, sizeOfLld2);
            } else if (operationNumber == 2) {
                // getLast
                if (lld2.size() == 0) {
                    continue;
                }

                int lastOfLld11 = lld1.get(lld1.size() - 1);
                int lastOfLld21 = lld2.get(lld2.size() - 1);
                int firstOfLld1 = lld1.get(0);
                int firstOfLld2 = lld2.get(0);

                assertEquals(lastOfLld11, lastOfLld21);
                assertEquals(firstOfLld1, firstOfLld2);
            } else if (operationNumber == 3) {
                // removeLast
                if (lld1.size() == 0) {
                    continue;
                }
                int removedItemOfLld1 = lld1.removeLast();
                int removedItemOfLld2 = lld2.removeLast();

                assertEquals(removedItemOfLld1, removedItemOfLld2);
            } else if (operationNumber == 4) {
                // removeFirst
                if (lld1.size() == 0) {
                    continue;
                }
                int removedItemOfLld1 = lld1.removeFirst();
                int removedItemOfLld2 = lld2.removeFirst();

                assertEquals(removedItemOfLld1, removedItemOfLld2);
                assertTrue(lld2.equals(lld1));
            }
        }
        assertTrue(lld1.equals(lld2));
    }
}
