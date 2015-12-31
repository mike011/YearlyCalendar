package ca.charland.report;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.charland.report.Month.MonthName;

public class PointTest {

    @Test
    public void construct() {
        Point p = new Point(0, 1, MonthName.January, "value");
        assertEquals(0, p.x);
        assertEquals(1, p.y);
        assertEquals("value", p.value);
    }

    @Test
    public void testPoint() throws Exception {
        Point p = new Point(0, 0, MonthName.January, 2);
        assertEquals("2", p.value);
    }

    @Test
    public void equals() {
        Point p = new Point(0, 1, MonthName.January, "value");
        Point p2 = new Point(0, 1, MonthName.January, "value");
        assertEquals(p, p2);
    }

    @Test
    public void notEquals() {
        Point p = new Point(0, 2, MonthName.January, "value");
        Point p2 = new Point(0, 1, MonthName.January, "value");
        assertNotEquals(p, p2);
    }

    @Test
    public void toStringTest() {
        Point p = new Point(0, 2, MonthName.January, "value");
        assertEquals("x=0, y=2, value", p.toString());
    }

    @Test
    public void isHighlighted() {
        Point p = new Point(0, 2, MonthName.January, "value");
        assertFalse(p.isHighlighted());
        p.highlight("birthday");
        assertTrue(p.isHighlighted());
    }
}
