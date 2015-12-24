package ca.charland.report;

public class Point {

    public final int x;
    public final int y;
    public final String value;
    public String description;

    public Point(int x, int y, int i) {
        this(x, y, String.valueOf(i));
    }

    public Point(int x, int y, String i) {
        this.x = x;
        this.y = y;
        this.value = i;
    }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }

    @Override
    public String toString() {
        String r = "x=" + x + ", y=" + y + ", " + value;
        return r;
    }

    public void highlight(String string) {
        this.description = string;
    }
    
    public boolean isHighlighted() {
        return description != null;
    }

}
