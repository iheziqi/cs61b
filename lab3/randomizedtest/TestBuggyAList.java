package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> Alist = new AListNoResizing<>();
        BuggyAList<Integer> Blist = new BuggyAList<>();

        Alist.addLast(4);
        Blist.addLast(4);

        Alist.addLast(5);
        Blist.addLast(5);

        Alist.addLast(6);
        Blist.addLast(6);

        assertEquals(Alist.size(), Blist.size());

        assertEquals(Alist.removeLast(), Blist.removeLast());
        assertEquals(Alist.removeLast(), Blist.removeLast());
        assertEquals(Alist.removeLast(), Blist.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> Buggy = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);

                L.addLast(randVal);
                Buggy.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int sizeOfL = L.size();
                int sizeOfBuggy = Buggy.size();

                assertEquals(sizeOfBuggy, sizeOfL);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() == 0) {
                    continue;
                }

                int lastOfL = L.getLast();
                int lastOfBuggy = L.getLast();

                assertEquals(lastOfL, lastOfBuggy);
            } else if (operationNumber == 3) {
                // removeLast
                if (L.size() == 0) {
                    continue;
                }
                int removedItemOfL = L.removeLast();
                int removedItemOfBuggy = Buggy.removeLast();

                assertEquals(removedItemOfL, removedItemOfBuggy);
            }
        }
    }
}
