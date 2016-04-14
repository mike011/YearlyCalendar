package ca.charland.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.charland.calendar.Event;


public class YearlyCalendarTest {

    @Test
    public void getEventsFromFile() throws Exception {
        ReportMaker rm = new ReportMaker(
                YearlyCalendar.getEventsFromFile("./test/sample.csv"));
        assertEquals(22, rm.getEvents().size());
    }
    
    @Test
    public void noEvents() throws Exception {
        List<Event> events = new ArrayList<Event>();
        List<String> ignoreList = new ArrayList<String>();
        YearlyCalendar.removeIgnoredEvents(events, ignoreList);
        assertTrue(events.isEmpty());
    }

    @Test
    public void oneEvent() throws Exception {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event("dog"));
        List<String> ignoreList = new ArrayList<String>();
        YearlyCalendar.removeIgnoredEvents(events, ignoreList);
        assertEquals(1, events.size());
    }

    @Test
    public void oneEventIgnored() throws Exception {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event("dog"));
        List<String> ignoreList = new ArrayList<String>();
        ignoreList.add("dog");
        YearlyCalendar.removeIgnoredEvents(events, ignoreList);
        assertTrue(events.isEmpty());
    }

    @Test
    public void oneEventIgnoredPartialMatch() throws Exception {
        List<Event> events = new ArrayList<Event>();
        events.add(new Event("dog"));
        List<String> ignoreList = new ArrayList<String>();
        ignoreList.add("do");
        YearlyCalendar.removeIgnoredEvents(events, ignoreList);
        assertTrue(events.isEmpty());
    }
}
