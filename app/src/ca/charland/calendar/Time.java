package ca.charland.calendar;

public class Time {

    private final int hour;
    private final int minute;

    public Time(int hour, int minute) {
        if (minute >= 60) {
            hour += minute / 60;
            minute %= 60;
        }
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public boolean equals(Object o) {
        Time t = (Time) o;
        return t.getMinute() == getMinute() && t.getHour() == getHour();
    }

    @Override
    public String toString() {
        return getHour() + ":" + getMinuteString();
    }

    private String getMinuteString() {
        String r = "";
        if (minute < 10) {
            r += "0";
        }
        r += minute;
        return r;
    }

}
