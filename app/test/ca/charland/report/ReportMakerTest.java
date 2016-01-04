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
        List<Highlight> highlight = calendar.getHighlighted(MonthName.January, 2);
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
        Calendar calendar = new Calendar(DATE );
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Test", "01/10/2016 15:00","01/10/2016 18:00","Sun","03:00"}));
        rm.highlightPoints();
        List<Highlight> highlight = calendar.getHighlighted(MonthName.January, 10);
        assertEquals("3:00PM-6:00PM Test", highlight.get(0).description);
    }
    
    private class MyReporter extends ReportPrinter {
        
        List<String> points = new ArrayList<String>();
        
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
            points.add(value);
        }
        
        @Override
        public void setBoldedString(String value, int x, int y) throws Exception {
        }
        
        @Override
        public void setItalizedString(String value, int x, int y) throws Exception {
        }

        @Override
        public void setUnderlineString(String value, int x, int y) throws Exception {
        }
        
        @Override
        public void setStikeout(int x, int y) throws Exception {
        }
        
        @Override
        public void rightAlign(int x, int y) throws Exception {
            
        }
    }

    @Test
    public void testAddPointsToCalendarThreeDayEvent() throws Exception {
        Calendar calendar = new Calendar();
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Multi-Day", "01/27/2016 12:00",
                "01/29/2016 18:30", "Wed", "54:30", "54:30" }));
        rm.highlightPoints();
        MyReporter reporter = new MyReporter();
        rm.addPointsToCalendar(reporter);
        assertEquals("Jan 27-29", reporter.points.get(0));
        assertEquals(1, reporter.points.size());
    }
    
    @Test
    public void testAddPointsToCalendarEventSeperatedByADay() throws Exception {
        Calendar calendar = new Calendar();
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Single-Day", "01/27/2016 12:00",
                "01/27/2016 18:30", "Wed", "24:00", "24:00" }));
        events.add(new Event(new String[] { "Single-Day", "01/29/2016 12:00",
                "01/29/2016 18:30", "Fri", "24:00", "24:00" }));
        rm.highlightPoints();
        MyReporter reporter = new MyReporter();
        rm.addPointsToCalendar(reporter);
        assertEquals("Jan 27", reporter.points.get(0));
        assertEquals("Jan 29", reporter.points.get(1));
    }
}
