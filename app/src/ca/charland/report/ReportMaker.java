package ca.charland.report;

import java.util.ArrayList;
import java.util.List;

import ca.charland.calendar.Event;
import ca.charland.io.LoadFile;

public class ReportMaker {

    private List<Event> events;
    private Calendar calendar;

    public ReportMaker() {
        this(new Calendar(), new ArrayList<Event>());
    }
    
    public ReportMaker(Calendar calendar) {
        this(calendar, new ArrayList<Event>());
    }
    
    public ReportMaker(List<Event> events) {
        this(new Calendar(), events);
    }
    
    public ReportMaker(Calendar calendar, List<Event> events) {
        this.calendar = calendar;
        this.events = events;
    }

    static ArrayList<Event> getEventsFromFile(String eventsFilename) {
        List<String> eventsFileContents = LoadFile.load(eventsFilename);
        List<String> ignoreList = LoadFile.load("./src/ignore.txt");
        ArrayList<Event> events = new ArrayList<Event>();
        for (int i = 1; i < eventsFileContents.size()-1; i++) {
            String s = eventsFileContents.get(i);
            Event e = new Event(s);
            if (!ignoreList.contains(e.getTitle())) {
                events.add(e);
            }
        }
        return events;
    }

    List<Event> getEvents() {
        return events;
    }

    void highlightPoints() {
        for (Event e : events) {
            calendar.highlight(e.getStart(), e.getTitle());
            int days = 1;
            while(e.getDuration().getHour() > 24 * days) {
                java.util.Calendar tom = java.util.Calendar.getInstance();
                if(e.getStart() == null) {
                    int x =3;
                    x = 5;
                    
                }
                tom.setTime(e.getStart());
                tom.add(java.util.Calendar.DATE, days++);
                calendar.highlight(tom.getTime(), e.getTitle());
            }
        }
    }

    private void addPoints(Reporter reporter) throws Exception {
        List<Point> pts = calendar.getDays();
        int y = 0;
        int x = 7 * 3 + 2 + 2;
        reporter.setWidth(x + 1, 7500);
        int MAX_Y = 40;
        for (Point p : pts) {
            if (p.isHighlighted()) {
                reporter.setBoldedString(p.value, p.x, p.y);
                reporter.setDate(p.month.toString() + " " + p.value, x, y);
                reporter.setString(p.description, x + 1, y++);
            } else {
                reporter.setString(p.value, p.x, p.y);
            }
            if (y > MAX_Y) {
                y = 0;
                x += 3;
                reporter.setWidth(x + 1, 7500);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        ReportMaker maker = new ReportMaker(getEventsFromFile("./test/sample.csv"));
        maker.highlightPoints();

        Reporter report = new Reporter();
        report.setWidthsForCalendarDates();
        maker.addPoints(report);
    }
}
