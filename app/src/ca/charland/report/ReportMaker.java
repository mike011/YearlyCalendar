package ca.charland.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.charland.calendar.Event;
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
        if (minutes < 10) {
            r += '0';
        }
        r += String.valueOf(minutes);
        return r + AMPM;
    }

    void addPointsToCalendar(ReportPrinter printer) throws Exception {
        List<Point> pts = calendar.getDays();
        int y = 0;
        int x = 7 * 3 + 2;
        printer.setWidth(x + 1, 7500);
        int MAX_Y = 55;
        for (int i = 0; i < pts.size(); i++) {
            Point p = pts.get(i);
            boolean isOld = p.old;
            if(isOld) {
                printer.setStrikeout(p.x, p.y);
            }
            if (p.isHighlighted()) {                
                List<Highlight> highlights = p.getHighlights();
                for (Highlight highlight : highlights) {
                    if (highlight.displayDescription()) {
                        addDate(printer, pts, y, x, i, highlight, isOld);
                        addHighlight(printer, highlight.description, x+1, y++, highlight, isOld);
                    }
                    addHighlight(printer, p.value, p.x, p.y, highlight, isOld);
                }
            } else {
                printer.setString(p.value, p.x, p.y);
            }
            if (y > MAX_Y) {
                y = 0;
                x += 2;
                printer.setWidth(x + 1, 7500);
            }
        }
        
        
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        Date today = calendar.getToday();
        printer.setString("Printed On: " + formatter.format(today), 0, 34);
    }

    private void addDate(ReportPrinter printer, List<Point> pts, int y, int x,
            int i, Highlight highlight, boolean isOld) throws Exception {
        if(isOld) {
            printer.setStrikeout(x, y);
        }
        printer.setDate(getDate(pts, i, highlight.description),
                x, y);
        printer.rightAlign(x, y);
    }

    private void addHighlight(ReportPrinter printer, String value, int x, int y,
            Highlight highlight, boolean isOld) throws Exception {
        if(isOld) {
            printer.setStrikeout(x, y);
        }
        if (Type.StatutoryHoliday.equals(highlight.type)) {
            printer.setItalizedString(value, x, y);

        } else if (Type.Birthday.equals(highlight.type)) {
            printer.setUnderlineString(value, x, y);
        } else {
            printer.setBoldedString(value, x, y);
        }
    }

    private String getDate(List<Point> pts, int x, String d) {
        Point p = pts.get(x);
        String end = "";
        while (++x < pts.size()) {
            Point n = pts.get(x);
            if (n.isHighlighted()) {
                for (Highlight h : n.getHighlights()) {
                    if (h.description.equals(d)) {
                        h.descriptionNotNeeded();
                        end = n.value;
                    }
                }
            } else {
                break;
            }
        }
        String string = p.month.toString().substring(0, 3) + " " + p.value;
        if (!end.isEmpty()) {
            string += "-" + end;
        }
        return string;
    }

    void setWidthsForCalendarDates(ReportPrinter r)
            throws WrappedTargetException, IndexOutOfBoundsException,
            UnknownPropertyException, PropertyVetoException,
            IllegalArgumentException {

        for (int x = 0; x <= 7 * 3 + 1; x++) {
            r.setWidth(x, 500);
        }
    }
}
