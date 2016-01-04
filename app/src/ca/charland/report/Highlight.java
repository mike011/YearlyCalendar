package ca.charland.report;

public class Highlight {

    public static enum Type {
        StatutoryHoliday, Birthday;
    }

    public final Type type;
    public final String description;
    private boolean displayDescription;

    public Highlight(String d, Type t) {
        description = d;
        type = t;
        displayDescription = true;
    }

    public Highlight(String d) {
        this(d, getType(d));
    }

    private static Type getType(String d) {
        Type type = null;
        if (d.toLowerCase().contains("birthday")) {
            type = Type.Birthday;
        }
        return type;
    }
    
    public boolean displayDescription() {
        return displayDescription;
    }
    
    public void descriptionNotNeeded() {
        displayDescription = false;
    }
}
