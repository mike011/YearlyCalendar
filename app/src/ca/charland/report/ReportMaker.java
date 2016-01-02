package ca.charland.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ca.charland.calendar.Event;
import ca.charland.io.LoadFile;
import ca.charland.report.Highlight.Type;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;

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

    List<Event> getEvents() {
        return events;
    }

    void highlightPoints() {
        for (Event e : events) {
            String title = e.getTitle();
            if (e.getDuration().getHour() < 24 && e.getStart() != null
                    && e.getEnd() != null) {
                String time = getTime(e.getStart());
                time += '-';
                time += getTime(e.getEnd());
                title = time + ' ' + title;
            }
            Highlight highlight = new Highlight(title);
            if (e.getCreatedBy().equals("Holidays in Canada")) {
                highlight = new Highlight(e.getTitle(), Type.StatutoryHoliday);
            }
            calendar.highlight(e.getStart(), highlight);
            int days = 1;
            while (e.getDuration().getHour() > 24 * days) {
                java.util.Calendar tom = java.util.Calendar.getInstance();
                tom.setTime(e.getStart());
                tom.add(java.util.Calendar.DATE, days++);
                calendar.highlight(tom.getTime(), highlight);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private String getTime(Date d) {
        String r = "";
        int hours = d.getHours();
        String AMPM = "AM";
        if (hours > 12) {
            hours -= 12;
            AMPM = "PM";
        }
        r += String.valueOf(hours) + ':';
        
        int minutes = d.getMinutes();
        if(minutes < 10) {
            r += '0';
        }
        r += String.valueOf(minutes);
        return r + AMPM;
    }

    private void addPointsToCalendar(ReportPrinter reporter) throws Exception {
        List<Point> pts = calendar.getDays();
        int y = 0;
        int x = 7 * 3 + 2;
        reporter.setWidth(x + 1, 7500);
        int MAX_Y = 40;
        for (Point p : pts) {
        	if(p.old) {
          		reporter.setStikeout(p.x, p.y);
            }
            if (p.isHighlighted()) {
                List<Highlight> highlights = p.getHighlights();
                for (Highlight highlight : highlights) {
                	if(p.old) {
                		reporter.setStikeout(x, y);
                		reporter.setStikeout(x+1, y);
                	}
                    reporter.setDate(p.month.toString() + " " + p.value, x, y);
                    if (Type.StatutoryHoliday.equals(highlight.type)) {
                        reporter.setItalizedString(p.value, p.x, p.y);
                        reporter.setItalizedString(highlight.description,
                                x + 1, y++);

                    } else if (Type.Birthday.equals(highlight.type)) {
                        reporter.setUnderlineString(p.value, p.x, p.y);
                        reporter.setUnderlineString(highlight.description,
                                x + 1, y++);
                    } else {
                        reporter.setBoldedString(p.value, p.x, p.y);
                        reporter.setBoldedString(highlight.description, x + 1,
                                y++);
                    }
                }

            } else {
                reporter.setString(p.value, p.x, p.y);
            }
            if (y > MAX_Y) {
                y = 0;
                x += 2;
                reporter.setWidth(x + 1, 7500);
            }
        }
    }

    void setWidthsForCalendarDates(ReportPrinter r) throws WrappedTargetException,
            IndexOutOfBoundsException, UnknownPropertyException,
            PropertyVetoException, IllegalArgumentException {

        for (int x = 0; x <= 7 * 3 + 1; x++) {
            r.setWidth(x, 500);
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
