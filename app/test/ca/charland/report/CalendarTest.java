package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.charland.report.Month.MonthName;
import ca.charland.report.Month.WeekDay;

public class CalendarTest {
	
    private static final Date DATE = new Date(0);

    @Test
    public void getDaysForTheYear() {
        Calendar c = new Calendar(DATE);
        int monthTitles = 12;
        int dayTitles = 12 * 7;
        int daysInYear = 366;
        assertEquals(daysInYear + dayTitles + monthTitles, c.getDays().size());
    }

    @Test
    public void februaryIsInCorrectLocation() {
        Calendar c = new Calendar(DATE);
        List<Point> days = c.getDays();
        int monthTitle = 1;
        int dayTitles = 7;
        int daysInMonth = 31;
        Point feb = days.get(monthTitle + dayTitles + daysInMonth);
        assertEquals("February", feb.value);
        assertEquals(8, feb.x);
        assertEquals(0, feb.y);

        Month.WeekDay day = c.getDayOfMonth(MonthName.February, 1);
        assertEquals(Month.WeekDay.Monday, day);
    }

    @Test
    public void marchIsInCorrectLocation() {
        Calendar c = new Calendar(DATE);
        List<Point> days = c.getDays();
        int monthTitles = 1 * 2;
        int dayTitles = 7 * 2;
        int daysInMonth = 31 + 29;
        Point mar = days.get(monthTitles + dayTitles + daysInMonth);
        assertEquals("March", mar.value);
        assertEquals(8 + 8, mar.x);
        assertEquals(0, mar.y);
    }

    @Test
    public void aprilIsInCorrectLocation() {
        Calendar c = new Calendar(DATE);
        List<Point> days = c.getDays();
        int monthTitles = 1 * 3;
        int dayTitles = 7 * 3;
        int daysInMonth = 31 + 29 + 31;
        Point apr = days.get(monthTitles + dayTitles + daysInMonth);
        assertEquals("April", apr.value);
        assertEquals(0, apr.x);
        assertEquals(8, apr.y);
    }

    @Test
    public void mayIsInCorrectLocation() {
        Calendar c = new Calendar(DATE);
        List<Point> days = c.getDays();
        int monthTitles = 1 * 4;
        int dayTitles = 7 * 4;
        int daysInMonth = 31 + 29 + 31 + 30;
        Point may = days.get(monthTitles + dayTitles + daysInMonth);
        assertEquals("May", may.value);
        assertEquals(8, may.x);
        assertEquals(8, may.y);
    }

    @Test
    public void decemberIsInCorrectLocation() {
        Calendar c = new Calendar(DATE);
        List<Point> days = c.getDays();
        int monthTitles = 1 * 11;
        int dayTitles = 7 * 11;
        int daysInMonth = 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30;
        Point dec = days.get(monthTitles + dayTitles + daysInMonth);
        assertEquals("December", dec.value);
        assertEquals(8 + 8, dec.x);
        assertEquals(24, dec.y);
    }

    @Test
    public void firstDayOfMonth() {
        int x = 0;
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Friday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Monday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Tuesday);

        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Friday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Sunday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Wednesday);

        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Friday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Monday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Thursday);

        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Saturday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Tuesday);
        assertFirstDayOfMonth(MonthName.values()[x++], WeekDay.Thursday);
    }

    private void assertFirstDayOfMonth(MonthName name, WeekDay day) {
        assertEquals(name.toString(), day,
                new Calendar(DATE).getDayOfMonth(name, 1));
    }

    @Test
    public void highlight() {
        Calendar c = new Calendar(DATE);
        c.highlight(MonthName.January, 2, new Highlight("Jane"));
        assertTrue(c.isHighlighted(MonthName.January, 2));
    }

    @Test
    public void testHighlightDate() throws Exception {
        Calendar c = new Calendar(DATE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("02/01/2016");
        c.highlight(date, new Highlight("Jane"));
        assertTrue(c.isHighlighted(MonthName.January, 2));
    }
}
