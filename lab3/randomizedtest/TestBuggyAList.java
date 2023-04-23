package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();

        expected.addLast(4);
        expected.addLast(5);
        expected.addLast(6);

        actual.addLast(4);
        actual.addLast(5);
        actual.addLast(6);

        assertEquals(expected.size(), actual.size());

        assertEquals(expected.removeLast(), actual.removeLast());
        assertEquals(expected.removeLast(), actual.removeLast());
        assertEquals(expected.removeLast(), actual.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniformInt(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniformInt(0, 100);
                expected.addLast(randVal);
                actual.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                assertEquals(expected.size(), actual.size());
            } else if (operationNumber == 2) {
                // getLast
                if (expected.size() > 0) {
                    assertEquals(expected.getLast(), actual.getLast());
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (expected.size() > 0) {
                    assertEquals(expected.removeLast(), actual.removeLast());
                }
            }
        }
    }
}
