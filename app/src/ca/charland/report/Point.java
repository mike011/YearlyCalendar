package ca.charland.report;

import ca.charland.report.Month.MonthName;

public class Point {

    public final int x;
    public final int y;
    public final String value;
    public MonthName month;
    public Highlight highlight;

    public Point(int x, int y, MonthName month, int i) {
        this(x, y, month, String.valueOf(i));
    }

    public Point(int x, int y, MonthName month, String i) {
        this.x = x;
        this.y = y;
        this.month = month;
        this.value = i;
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
        this.highlight = string;
    }
    
    public Highlight getHighlight() {
    	return highlight;
    }
    
    public boolean isHighlighted() {
        return highlight != null;
    }

}
