package ca.charland.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.charland.calendar.Event;
import ca.charland.io.LoadFile;

public class YearlyCalendar {

    static ArrayList<Event> getEventsFromFile(String eventsFilename) {
        ArrayList<Event> events = getEvents(eventsFilename);
        List<String> ignoreList = LoadFile.loadFile("./src/ignore.txt");
        removeIgnoredEvents(events, ignoreList);
        return events;
    }

    private static ArrayList<Event> getEvents(String eventsFilename) {
        List<String> eventsFileContents = LoadFile.loadCSVFile(eventsFilename);
        ArrayList<Event> events = new ArrayList<Event>();
        for (int i = 1; i < eventsFileContents.size() - 1; i++) {
            String s = eventsFileContents.get(i);
            events.add(new Event(s));
        }
        return events;
    }

    static void removeIgnoredEvents(List<Event> events, List<String> ignoreList) {
        Iterator<Event> i = events.iterator();
        while (i.hasNext()) {
            Event e = i.next();
            for (String ignore : ignoreList) {
                if (e.getTitle().contains(ignore)) {
                    i.remove();
                }
            }
        }
    }
    
    public static void main(String args[]) throws Exception {
        ReportMaker maker = new ReportMaker(getEventsFromFile(args[0]));
        maker.highlightPoints();
        ReportPrinter report = new ReportPrinter();
        maker.setWidthsForCalendarDates(report);
        maker.addPointsToCalendar(report);
    }
}
