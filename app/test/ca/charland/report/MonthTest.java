package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.charland.report.Month.MonthName;
import ca.charland.report.Month.WeekDay;

public class MonthTest {

    private static final Date DATE = new Date(0);

    @Test
    public void name() {
        assertEquals(MonthName.January.toString(), new Month(DATE,
                MonthName.January, WeekDay.Friday, 0, 0).getName());
    }

    @Test
    public void days() {
        assertEquals(31, new Month(DATE, MonthName.January, WeekDay.Friday, 0,
                0).getDays());
    }

    @Test
    public void startX() {
        assertEquals(1,
                new Month(DATE, MonthName.January, WeekDay.Friday, 1, 0)
                        .getStartX());
    }

    @Test
    public void startY() {
        assertEquals(2,
                new Month(DATE, MonthName.January, WeekDay.Friday, 0, 2)
                        .getStartY());
    }

    @Test
    public void getCalendarPoints() {
        List<Point> pts = new Month(DATE, MonthName.January, WeekDay.Friday, 0,
                0).getCalendarPoints();
        assertFalse(pts.isEmpty());
        int p = 0;
        assertEquals(new Point(0, 0, MonthName.January, "January", false),
                pts.get(p++));
        assertEquals(new Point(0, 1, MonthName.January, "Su", false),
                pts.get(p++));
        assertEquals(new Point(1, 1, MonthName.January, "M", false),
                pts.get(p++));
        assertEquals(new Point(2, 1, MonthName.January, "Tu", false),
                pts.get(p++));
        assertEquals(new Point(3, 1, MonthName.January, "W", false),
                pts.get(p++));
        assertEquals(new Point(4, 1, MonthName.January, "Th", false),
                pts.get(p++));
        assertEquals(new Point(5, 1, MonthName.January, "F", false),
                pts.get(p++));
        assertEquals(new Point(6, 1, MonthName.January, "Sa", false),
                pts.get(p++));
        assertEquals(new Point(5, 2, MonthName.January, 1, false), pts.get(p++));
        assertEquals(new Point(6, 2, MonthName.January, 2, false), pts.get(p++));
        assertEquals(new Point(0, 3, MonthName.January, 3, false), pts.get(p++));
        assertEquals(new Point(0, 7, MonthName.January, 31, false),
                pts.get(p += 27));
    }

    @Test
    public void getCalendarPointsOffSet() {
        List<Point> pts = new Month(DATE, MonthName.January, WeekDay.Friday, 1,
                1).getCalendarPoints();
        assertFalse(pts.isEmpty());
        int p = 0;
        assertEquals(new Point(1, 1, MonthName.January, "January", false),
                pts.get(p++));
        assertEquals(new Point(1, 2, MonthName.January, "Su", false),
                pts.get(p++));
        assertEquals(new Point(2, 2, MonthName.January, "M", false),
                pts.get(p++));
        assertEquals(new Point(3, 2, MonthName.January, "Tu", false),
                pts.get(p++));
        assertEquals(new Point(4, 2, MonthName.January, "W", false),
                pts.get(p++));
        assertEquals(new Point(5, 2, MonthName.January, "Th", false),
                pts.get(p++));
        assertEquals(new Point(6, 2, MonthName.January, "F", false),
                pts.get(p++));
        assertEquals(new Point(7, 2, MonthName.January, "Sa", false),
                pts.get(p++));
        assertEquals(new Point(6, 3, MonthName.January, 1, false), pts.get(p++));
        assertEquals(new Point(7, 3, MonthName.January, 2, false), pts.get(p++));
        assertEquals(new Point(1, 4, MonthName.January, 3, false), pts.get(p++));
        assertEquals(new Point(1, 8, MonthName.January, 31, false),
                pts.get(p += 27));
    }

    @Test
    public void testGetPointForDayWeekDay() throws Exception {
        Month m = new Month(DATE, MonthName.January, WeekDay.Friday, 0, 0);
        assertEquals(Month.WeekDay.Friday, m.getWeekDay(1));
        assertEquals(Month.WeekDay.Saturday, m.getWeekDay(2));
        assertEquals(Month.WeekDay.Sunday, m.getWeekDay(3));
    }

    @Test
    public void testGetPointForDayNextDay() throws Exception {
        assertEquals(WeekDay.Monday, WeekDay.Sunday.getNextDay());
        assertEquals(WeekDay.Tuesday, WeekDay.Monday.getNextDay());
        assertEquals(WeekDay.Sunday, WeekDay.Saturday.getNextDay());
    }

    @Test
    public void testHighlight() throws Exception {
        Month m = new Month(DATE, MonthName.January, WeekDay.Friday, 0, 0);
        m.highlight(2, new Highlight("birthday"));
        assertTrue(m.isDayHighlighted(2));
    }

    @Test
    public void testGetPointForDay() throws Exception {
        Month m = new Month(DATE, MonthName.January, WeekDay.Sunday, 0, 0);
        assertEquals(new Point(0, 2, MonthName.January, 1, false),
                m.getPointForDay(1));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void getOldPoints() throws Exception {
        Date d = new Date(0);
        d.setMonth(0);
        d.setDate(2);
        Month m = new Month(d, MonthName.January, WeekDay.Sunday, 0, 0);
        List<Point> pts = m.getCalendarPoints();
        int x = 0;
        assertFalse("Title", pts.get(x++).old);
        for (int w = 0; w < 7; w++) {
            assertFalse("Week Name", pts.get(x++).old);
        }
        assertTrue("1st", pts.get(x++).old);
        assertTrue("2nd", pts.get(x++).old);
        assertFalse("3rd", pts.get(x++).old);
    }
}
