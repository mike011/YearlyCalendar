package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.charland.calendar.Event;
import ca.charland.report.Month.MonthName;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.table.XCell;

public class ReportMakerTest {

    private static final Date DATE = new Date(0);

    @Test
    public void getEventsFromFile() throws Exception {
        ReportMaker rm = new ReportMaker(
                ReportMaker.getEventsFromFile("./test/sample.csv"));
        assertEquals(27, rm.getEvents().size());
    }

    @Test
    public void testHighlightOnePoint() throws Exception {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Test", "01/02/2016 00:00" }));
        rm.highlightPoints();
        assertTrue(calendar.isHighlighted(MonthName.January, 2));
    }

    @Test
    public void testHighlightMultipleDaysFromOneEvent() throws Exception {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Multi-Day", "01/27/2016 12:00",
                "01/29/2016 18:30", "Wed", "54:30", "54:30" }));
        rm.highlightPoints();
        assertTrue("Jan 27th not highlighted",
                calendar.isHighlighted(MonthName.January, 27));
        assertTrue("Jan 28th not highlighted",
                calendar.isHighlighted(MonthName.January, 28));
        assertTrue("Jan 29th not highlighted",
                calendar.isHighlighted(MonthName.January, 29));
    }

    @Test
    public void testHighlightPointsTwoOnSameDay() throws Exception {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Test", "01/02/2016 00:00" }));
        events.add(new Event(new String[] { "Test", "01/02/2016 00:00" }));
        rm.highlightPoints();
        assertTrue(calendar.isHighlighted(MonthName.January, 2));
    }

    @Test
    public void testHighlightStatHoliday() {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Test", "01/02/2016 00:00", "", "",
                "", "", "Holidays in Canada" }));
        rm.highlightPoints();
        List<Highlight> highlight = calendar.getHighlighted(MonthName.January,
                2);
        assertEquals(Highlight.Type.StatutoryHoliday, highlight.get(0).type);
    }

    @Test
    public void noEvents() throws Exception {
        List<Event> events = new ArrayList<Event>();
        List<String> ignoreList = new ArrayList<String>();
        ReportMaker.removeIgnoredEvents(events, ignoreList);
        assertTrue(events.isEmpty());
    }

    @Test
    public void oneEvent() throws Exception {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event("dog"));
        List<String> ignoreList = new ArrayList<String>();
        ReportMaker.removeIgnoredEvents(events, ignoreList);
        assertEquals(1, events.size());
    }

    @Test
    public void oneEventIgnored() throws Exception {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event("dog"));
        List<String> ignoreList = new ArrayList<String>();
        ignoreList.add("dog");
        ReportMaker.removeIgnoredEvents(events, ignoreList);
        assertTrue(events.isEmpty());
    }

    @Test
    public void oneEventIgnoredPartialMatch() throws Exception {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event("dog"));
        List<String> ignoreList = new ArrayList<String>();
        ignoreList.add("do");
        ReportMaker.removeIgnoredEvents(events, ignoreList);
        assertTrue(events.isEmpty());
    }

    @Test
    public void testHighlightPointsShowHoursForLessThenADay() throws Exception {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Test", "01/10/2016 15:00",
                "01/10/2016 18:00", "Sun", "03:00" }));
        rm.highlightPoints();
        List<Highlight> highlight = calendar.getHighlighted(MonthName.January,
                10);
        assertEquals("3:00PM-6:00PM Test", highlight.get(0).description);
    }

    private class MyReporter extends ReportPrinter {

        List<String> dates = new ArrayList<String>();
        List<String> pts = new ArrayList<String>();
        int strikes;

        @Override
        void init() {
        }

        @Override
        void setWidth(int x, int width) throws IndexOutOfBoundsException,
                WrappedTargetException, UnknownPropertyException,
                PropertyVetoException, IllegalArgumentException {
        }

        @Override
        XCell setString(String value, int x, int y) {
            return null;
        }

        @Override
        void setFormula(String value, int x, int y, int format) {
            dates.add(value);
        }

        @Override
        public void setBoldedString(String value, int x, int y)
                throws Exception {
            pts.add(value);
        }

        @Override
        public void setItalizedString(String value, int x, int y)
                throws Exception {
            pts.add(value);
        }

        @Override
        public void setUnderlineString(String value, int x, int y)
                throws Exception {
            pts.add(value);
        }

        @Override
        public void setStrikeout(int x, int y) throws Exception {
            ++strikes;
        }

        @Override
        public void rightAlign(int x, int y) throws Exception {

        }
    }

    @Test
    public void testAddPointsToCalendarThreeDayEvent() throws Exception {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Multi-Day", "01/27/2016 12:00",
                "01/29/2016 18:30", "Wed", "54:30", "54:30" }));
        rm.highlightPoints();
        MyReporter reporter = new MyReporter();
        rm.addPointsToCalendar(reporter);
        assertEquals("Jan 27-29", reporter.dates.get(0));
        assertEquals(1, reporter.dates.size());
        assertEquals("Multi-Day", reporter.pts.get(0));
        assertEquals("27", reporter.pts.get(1));
        assertEquals("28", reporter.pts.get(2));
        assertEquals("29", reporter.pts.get(3));
    }

    @Test
    public void testAddPointsToCalendarEventSeperatedByADay() throws Exception {
        Calendar calendar = new Calendar(DATE);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Single-Day", "01/27/2016 12:00",
                "01/27/2016 18:30", "Wed", "24:00", "24:00" }));
        events.add(new Event(new String[] { "Single-Day", "01/29/2016 12:00",
                "01/29/2016 18:30", "Fri", "24:00", "24:00" }));
        rm.highlightPoints();
        MyReporter reporter = new MyReporter();
        rm.addPointsToCalendar(reporter);
        assertEquals("Jan 27", reporter.dates.get(0));
        assertEquals("Jan 29", reporter.dates.get(1));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testOldCalendarPts() throws Exception {
        Date d = new Date(0);
        d.setMonth(0);
        d.setDate(2);
        Calendar calendar = new Calendar(d);
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        rm.highlightPoints();
        MyReporter reporter = new MyReporter();
        rm.addPointsToCalendar(reporter);
        assertEquals(2, reporter.strikes);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testOldCalendarEvents() throws Exception {
        Date d = new Date(0);
        d.setMonth(0);
        d.setDate(2);
        Calendar calendar = new Calendar(d);
        List<Event> events = new ArrayList<Event>();
        events.add(new Event(new String[] { "Single-Day", "01/01/2016 12:00",
                "01/01/2016 18:30", "Wed", "24:00", "24:00" }));
        ReportMaker rm = new ReportMaker(calendar, events);
        rm.highlightPoints();
        MyReporter reporter = new MyReporter();
        rm.addPointsToCalendar(reporter);
        assertEquals(4, reporter.strikes);
    }
}
