package ca.charland.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

    private enum Name {
        Title, Start, End, DayOfWeek, Duration, DayTotal, CreatedBy, OrganizedBy, Description, Where, Calendar
    }

    private String[] splits;
    private boolean allDay;

    public Event(String line) {
        splits = line.split(",");
    }

    public Event(String[] line) {
        splits = line;
    }

    private String removeQuotes(String split) {
        if (!split.isEmpty()) {
            if (split.charAt(0) == '\"') {
                split = split.substring(1);
            }
            if (!split.isEmpty() && split.charAt(split.length() - 1) == '\"') {
                split = split.substring(0, split.length() - 1);
            }
        }
        return split;
    }

    public String getTitle() {
        return getString(Name.Title);
    }

    public Date getStart() {
        return getDate(getString(Name.Start));
    }

    private Date getDate(String date) {
        if (!date.isEmpty()) {
            try {
                return new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Date getEnd() {
        return getDate(getString(Name.End));
    }

    public String getDayOfWeek() {
        return getString(Name.DayOfWeek);
    }

    public Time getDuration() {
        String d = getString(Name.Duration);
        if (d.isEmpty()) {
            return new Time(0, 0);
        }
        String[] splits = d.split(":");
        try {
            int hour = Integer.valueOf(splits[0]);
            if (hour >= 24) {
                allDay = true;
            }
            return new Time(hour, Integer.valueOf(splits[1]));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return null;
        }
    }

    private String getString(Name n) {
        if (n.ordinal() < splits.length) {
            return removeQuotes(splits[n.ordinal()]);
        }
        return "";
    }

    public boolean isAllDay() {
        return allDay;
    }

    public Time getDayTotal() {
        String d = getString(Name.DayTotal);
        if (!d.isEmpty()) {
            String[] splits = d.split(":");
            int hour = Integer.valueOf(splits[0]);
            return new Time(hour, Integer.valueOf(splits[1]));
        }
        return new Time(0, 0);
    }

    public String getCreatedBy() {
        return getString(Name.CreatedBy);
    }

    public String getOrganizedBy() {
        return getString(Name.OrganizedBy);
    }

    public String getDescription() {
        return getString(Name.Description);
    }

    public String getWhere() {
        return getString(Name.Where);
    }

    public String getCalendar() {
        return getString(Name.Calendar);
    }
}
