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
        return removeQuotes(splits[Name.Title.ordinal()]);
    }

    public Date getStart() {
        return getDate(removeQuotes(splits[Name.Start.ordinal()]));
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
        return getDate(removeQuotes(splits[Name.End.ordinal()]));
    }

    public String getDayOfWeek() {
        return removeQuotes(splits[Name.DayOfWeek.ordinal()]);
    }

    public Time getDuration() {
        String d = removeQuotes(splits[Name.Duration.ordinal()]);
        String[] splits = d.split(":");
        int hour = Integer.valueOf(splits[0]);
        if (hour >= 24) {
            allDay = true;
        }
        return new Time(hour, Integer.valueOf(splits[1]));
    }

    public boolean isAllDay() {
        return allDay;
    }

    public Time getDayTotal() {
        String d = removeQuotes(splits[Name.DayTotal.ordinal()]);
        if (!d.isEmpty()) {
            String[] splits = d.split(":");
            int hour = Integer.valueOf(splits[0]);
            return new Time(hour, Integer.valueOf(splits[1]));
        }
        return new Time(0, 0);
    }

    public String getCreatedBy() {
        return removeQuotes(splits[Name.CreatedBy.ordinal()]);
    }

    public String getOrganizedBy() {
        return removeQuotes(splits[Name.OrganizedBy.ordinal()]);
    }

    public String getDescription() {
        return removeQuotes(splits[Name.Description.ordinal()]);
    }

    public String getWhere() {
        return removeQuotes(splits[Name.Where.ordinal()]);
    }

    public String getCalendar() {
        return removeQuotes(splits[Name.Calendar.ordinal()]);
    }
}
