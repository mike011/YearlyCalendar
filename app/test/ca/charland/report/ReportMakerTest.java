package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.charland.calendar.Event;
import ca.charland.report.Month.MonthName;

public class ReportMakerTest {

    @Test
    public void testGetEvents() throws Exception {
        ReportMaker rm = new ReportMaker(ReportMaker.getEventsFromFile("./test/sample.csv"));
        assertEquals(48, rm.getEvents().size());
    }

    @Test
    public void testHighlightOnePoint() throws Exception {
        Calendar calendar = new Calendar();
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Test", "01/02/2016 00:00" }));
        rm.highlightPoints();
        assertTrue(calendar.isHighlighted(MonthName.January, 2));
    }
    
    @Test
    public void testHighlightMultipleDaysFromOneEvent() throws Exception {
        Calendar calendar = new Calendar();
        List<Event> events = new ArrayList<Event>();
        ReportMaker rm = new ReportMaker(calendar, events);
        events.add(new Event(new String[] { "Multi-Day","01/27/2016 12:00","01/29/2016 18:30","Wed","54:30","54:30"}));
        rm.highlightPoints();
        assertTrue("Jan 27th not highlighted", calendar.isHighlighted(MonthName.January, 27));
        assertTrue("Jan 28th not highlighted", calendar.isHighlighted(MonthName.January, 28));
        assertTrue("Jan 29th not highlighted", calendar.isHighlighted(MonthName.January, 29));
    }

}
