package ca.charland.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ca.charland.report.Month.MonthName;
import ca.charland.report.Month.WeekDay;

public class Calendar {

    private Map<MonthName, Month> days;

    public Calendar() {
        days = new TreeMap<MonthName, Month>();
        populateDays();
    }

    public List<Point> getDays() {
        List<Point> pts = new ArrayList<Point>();
        for (Month m : days.values()) {
            pts.addAll(m.getCalendarPoints());
        }
        return pts;
    }

    private Set<Point> populateDays() {
        Set<Point> days = new TreeSet<Point>();
        addFirstRow();
        int startY = 8;
        addRow(startY, MonthName.April);
        startY += 8;
        addRow(startY, MonthName.July);
        startY += 8;
        addRow(startY, MonthName.October);
        return days;
    }

    private WeekDay addMonth(int startX, int startY, MonthName month,
            WeekDay firstDay) {
        Month value = new Month(month, firstDay, startX, startY);
        days.put(month, value);
        WeekDay dayOfMonth = value.getWeekDay(month.getDays()).getNextDay();
        return dayOfMonth;
    }

    private void addFirstRow() {
        addRow(0, 0, MonthName.January, WeekDay.Friday);
    }

    private void addRow(int startY, MonthName month) {
        int startX = 0;
        MonthName lastMonth = MonthName.values()[month.ordinal() - 1];
        WeekDay firstDay = getDayOfMonth(lastMonth, lastMonth.getDays())
                .getNextDay();
        addRow(startX, startY, month, firstDay);
    }

    private void addRow(int startX, int startY, MonthName month,
            WeekDay firstDay) {
        for (int x = 0; x < 3; x++) {
            firstDay = addMonth(startX, startY,
                    MonthName.values()[month.ordinal() + x], firstDay);
            startX += 8;
        }
    }

    WeekDay getDayOfMonth(MonthName name, int i) {
        Month month = days.get(name);
        return month.getWeekDay(i);
    }

    public void highlight(MonthName name, int i, Highlight highlight) {
        Month month = days.get(name);
        month.highlight(i, highlight);
    }

    public boolean isHighlighted(MonthName name, int i) {
        Month month = days.get(name);
        return month.isDayHighlighted(i);
    }

    public Highlight getHighlighted(MonthName name, int i) {
        Month month = days.get(name);
        return month.getDayHighlighted(i);
    }

    @SuppressWarnings("deprecation")
    public void highlight(Date start, Highlight highlight) {
        if (start != null) {
            Month month = days.get(MonthName.values()[start.getMonth()]);
            month.highlight(start.getDate(), highlight);
        }
    }
}