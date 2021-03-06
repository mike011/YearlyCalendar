package ca.charland.report;

import java.util.ArrayList;
import java.util.List;

import ca.charland.report.Month.MonthName;

public class Point {

    public final int x;
    public final int y;
    public final String value;
    public final MonthName month;
    private List<Highlight> highlights;
    public final boolean old;

    public Point(int x, int y, MonthName month, int value, boolean old) {
        this(x, y, month, String.valueOf(value), old);
    }

    public Point(int x, int y, MonthName month, String value, boolean old) {
        this.x = x;
        this.y = y;
        this.month = month;
        this.value = value;
        this.old = old;
        this.highlights = new ArrayList<Highlight>();
    }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        return p.x == x && p.y == y && p.month == month;
    }

    @Override
    public String toString() {
        String r = "x=" + x + ", y=" + y + ", " + value;
        return r;
    }

    public void highlight(Highlight string) {
        this.highlights.add(string);
    }

    public List<Highlight> getHighlights() {
        return highlights;
    }

    public boolean isHighlighted() {
        return !highlights.isEmpty();
    }
}
