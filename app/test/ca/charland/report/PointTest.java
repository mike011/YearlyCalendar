package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.charland.report.Month.MonthName;

public class PointTest {

    @Test
    public void construct() {
        Point p = new Point(0, 1, MonthName.January, "value", false);;
        assertEquals(0, p.x);
        assertEquals(1, p.y);
        assertEquals("value", p.value);
    }

    @Test
    public void testPoint() throws Exception {
        Point p = new Point(0, 0, MonthName.January, 2, false);
        assertEquals("2", p.value);
    }

    @Test
    public void equals() {
        Point p = new Point(0, 1, MonthName.January, "value", false);;
        Point p2 = new Point(0, 1, MonthName.January, "value", false);;
        assertEquals(p, p2);
    }

    @Test
    public void notEquals() {
        Point p = new Point(0, 2, MonthName.January, "value", false);;
        Point p2 = new Point(0, 1, MonthName.January, "value", false);;
        assertNotEquals(p, p2);
    }

    @Test
    public void toStringTest() {
        Point p = new Point(0, 2, MonthName.January, "value", false);;
        assertEquals("x=0, y=2, value", p.toString());
    }

    @Test
    public void isHighlighted() {
        Point p = new Point(0, 2, MonthName.January, "value", false);
        assertFalse(p.isHighlighted());
        p.highlight(new Highlight("Jane"));
        assertTrue(p.isHighlighted());
    }

    @Test
    public void testHighlightMultipleTimes() throws Exception {
        Point p = new Point(0, 2, MonthName.January, "one", false);
        p.highlight(new Highlight("one"));
        p.highlight(new Highlight("two"));
        assertEquals(2, p.getHighlights().size());
    }

	@Test
	public void testIsOld() throws Exception {
		Point p = new Point(0, 2, MonthName.January, "one", false);
		assertFalse(p.old);
		p = new Point(0, 2, MonthName.January, "one", true);
		assertTrue(p.old);
	}
}
