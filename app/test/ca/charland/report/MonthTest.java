package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ca.charland.report.Month.MonthName;
import ca.charland.report.Month.WeekDay;

public class MonthTest {

    @Test
    public void name() {
        assertEquals(MonthName.January.toString(), new Month(MonthName.January,
                WeekDay.Friday, 0, 0).getName());
    }

    @Test
    public void days() {
        assertEquals(31,
                new Month(MonthName.January, WeekDay.Friday, 0, 0).getDays());
    }

    @Test
    public void startX() {
        assertEquals(1,
                new Month(MonthName.January, WeekDay.Friday, 1, 0).getStartX());
    }

    @Test
    public void startY() {
        assertEquals(2,
                new Month(MonthName.January, WeekDay.Friday, 0, 2).getStartY());
    }

    @Test
    public void getCalendarPoints() {
        List<Point> pts = new Month(MonthName.January, WeekDay.Friday, 0, 0)
                .getCalendarPoints();
        assertFalse(pts.isEmpty());
        int p = 0;
        assertEquals(new Point(0, 0, MonthName.January, "January"),
                pts.get(p++));
        assertEquals(new Point(0, 1, MonthName.January, "Su"), pts.get(p++));
        assertEquals(new Point(1, 1, MonthName.January, "M"), pts.get(p++));
        assertEquals(new Point(2, 1, MonthName.January, "Tu"), pts.get(p++));
        assertEquals(new Point(3, 1, MonthName.January, "W"), pts.get(p++));
        assertEquals(new Point(4, 1, MonthName.January, "Th"), pts.get(p++));
        assertEquals(new Point(5, 1, MonthName.January, "F"), pts.get(p++));
        assertEquals(new Point(6, 1, MonthName.January, "Sa"), pts.get(p++));
        assertEquals(new Point(5, 2, MonthName.January, 1), pts.get(p++));
        assertEquals(new Point(6, 2, MonthName.January, 2), pts.get(p++));
        assertEquals(new Point(0, 3, MonthName.January, 3), pts.get(p++));
        assertEquals(new Point(0, 7, MonthName.January, 31), pts.get(p += 27));
    }

    @Test
    public void getCalendarPointsOffSet() {
        List<Point> pts = new Month(MonthName.January, WeekDay.Friday, 1, 1)
                .getCalendarPoints();
        assertFalse(pts.isEmpty());
        int p = 0;
        assertEquals(new Point(1, 1, MonthName.January, "January"),
                pts.get(p++));
        assertEquals(new Point(1, 2, MonthName.January, "Su"), pts.get(p++));
        assertEquals(new Point(2, 2, MonthName.January, "M"), pts.get(p++));
        assertEquals(new Point(3, 2, MonthName.January, "Tu"), pts.get(p++));
        assertEquals(new Point(4, 2, MonthName.January, "W"), pts.get(p++));
        assertEquals(new Point(5, 2, MonthName.January, "Th"), pts.get(p++));
        assertEquals(new Point(6, 2, MonthName.January, "F"), pts.get(p++));
        assertEquals(new Point(7, 2, MonthName.January, "Sa"), pts.get(p++));
        assertEquals(new Point(6, 3, MonthName.January, 1), pts.get(p++));
        assertEquals(new Point(7, 3, MonthName.January, 2), pts.get(p++));
        assertEquals(new Point(1, 4, MonthName.January, 3), pts.get(p++));
        assertEquals(new Point(1, 8, MonthName.January, 31), pts.get(p += 27));
    }

    @Test
    public void testGetPointForDayWeekDay() throws Exception {
        Month m = new Month(MonthName.January, WeekDay.Friday, 0, 0);
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
        Month m = new Month(MonthName.January, WeekDay.Friday, 0, 0);
        m.highlight(2, "birthday");
        assertTrue(m.isDayHighlighted(2));
    }

    @Test
    public void testGetPointForDay() throws Exception {
        Month m = new Month(MonthName.January, WeekDay.Sunday, 0, 0);
        assertEquals(new Point(0, 2, MonthName.January, 1), m.getPointForDay(1));
    }
}
