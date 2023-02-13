package flik;

import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {
    @Test
    /* basic test*/
    public void basicTest() {
        int a = 1;
        int b = 1;
        assertTrue(Flik.isSameNumber(a, b));
    }

    @Test
    /* complex test*/
    public void complexTest() {
        int j = 0;
        for (int i = 0; i < 1000; i++, j++) {
           assertTrue("j is " + j + " and i is " + i, Flik.isSameNumber(i, j));
        }
    }
}
