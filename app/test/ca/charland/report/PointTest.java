package ca.charland.report;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest {

    @Test
    public void construct() {
        Point p = new Point(0, 1, "value");
        assertEquals(0, p.x);
        assertEquals(1, p.y);
        assertEquals("value", p.value);
    }

    @Test
    public void testPoint() throws Exception {
        Point p = new Point(0, 0, 2);
        assertEquals("2", p.value);
    }

    @Test
    public void equals() {
        Point p = new Point(0, 1, "value");
        Point p2 = new Point(0, 1, "value");
        assertEquals(p, p2);
    }

    @Test
    public void notEquals() {
        Point p = new Point(0, 2, "value");
        Point p2 = new Point(0, 1, "value");
        assertNotEquals(p, p2);
    }

    @Test
    public void toStringTest() {
        Point p = new Point(0, 2, "value");
        assertEquals("x=0, y=2, value", p.toString());
    }

    @Test
    public void isHighlighted() {
        Point p = new Point(0, 2, "value");
        assertFalse(p.isHighlighted());
        p.highlight("birthday");
        assertTrue(p.isHighlighted());
    }
}
