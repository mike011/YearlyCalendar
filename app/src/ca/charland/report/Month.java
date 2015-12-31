package ca.charland.report;

import java.util.ArrayList;
import java.util.List;

public class Month {

    enum MonthName {
        January(31), February(29), March(31), April(30), May(31), June(30), July(
                31), August(31), September(30), October(31), November(30), December(
                31);

        private int days;

        MonthName(int days) {
            this.days = days;
        }

        public int getDays() {
            return days;
        }

    }

    enum WeekDay {
        Sunday("Su"), Monday("M"), Tuesday("Tu"), Wednesday("W"), Thursday("Th"), Friday(
                "F"), Saturday("Sa");

        private String string;

        WeekDay(String s) {
            string = s;
        }

        @Override
        public String toString() {
            return string;
        }

        public WeekDay getNextDay() {
            return values()[(ordinal() + 1) % 7];
        }
    }

    private final MonthName name;
    private final int startX;
    private final int startY;
    private final WeekDay firstDayOfMonth;
    private List<Point> pts;
    
    public Month(MonthName name, WeekDay firstDayOfMonth, int startX, int startY) {
        this.name = name;
        this.firstDayOfMonth = firstDayOfMonth;
        this.startX = startX;
        this.startY = startY;
        pts = populateCalendarPoints();
    }

    public String getName() {
        return name.toString();
    }

    public int getDays() {
        return name.getDays();
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public List<Point> getCalendarPoints() {
        return pts;
    }

    private List<Point> populateCalendarPoints() {
        List<Point> pts = new ArrayList<Point>();
        pts.add(new Point(startX, startY, name, name.toString()));
        addWeekDays(pts);
        addDays(pts);
        return pts;
    }

    private void addWeekDays(List<Point> pts) {
        int x = startX;
        for (WeekDay w : WeekDay.values()) {
            pts.add(new Point(x++, startY + 1, name, w.toString()));
        }
    }

    private void addDays(List<Point> pts) {
        int x = startX + firstDayOfMonth.ordinal();
        int y = startY + 2;
        int day = 1;
        while (day <= name.getDays()) {
            pts.add(new Point(x++, y, name, day++));
            if (x - startX == 7) {
                x = startX;
                y += 1;
            }
        }
    }

    public WeekDay getWeekDay(int day) {
        return WeekDay.values()[(firstDayOfMonth.ordinal() + day - 1) % 7];
    }

    public void highlight(int day, String description) {
        getPointForDay(day).highlight(description);
    }

    public boolean isDayHighlighted(int i) {
        return getPointForDay(i).isHighlighted();
    }
    
    Point getPointForDay(int i) {
        int title = 0;
        int weekDayStrings = 7;
        return pts.get(title + weekDayStrings + i);
    }
}
