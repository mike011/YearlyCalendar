package ca.charland.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class EventTest {

    private String line = "Graeme's Daycare Payment\",\"01/01/2016 00:00\",\"01/02/2016 00:00\",\"Fri\",\"24:00\",\",\"Michael Charland\",\"Michael Charland\",\",\",\"michael.charland@gmail.com";
    private Event e = new Event(line);

    @Test
    public void getTitle() {
        assertEquals("Graeme's Daycare Payment", e.getTitle());
    }

    @Test
    public void getStart() {
        assertDate(1, 1, 2016, 0, 00, e.getStart());
    }

    @SuppressWarnings("deprecation")
    private void assertDate(int day, int month, int year, int hour, int minute,
            Date d) {
        assertNotNull("Date is null", d);
        assertEquals(day, d.getDate());
        assertEquals(month-1, d.getMonth());
        assertEquals(year, 1900 + d.getYear());
        assertEquals(hour, d.getHours());
        assertEquals(minute, d.getMinutes());

    }

    @Test
    public void getEnd() {
        assertDate(2, 1, 2016, 0, 00, e.getEnd());
    }

    @Test
    public void dayOfWeek() {
        assertEquals("Fri", e.getDayOfWeek());
    }

    @Test
    public void duration() {
        Event e = new Event(",,,,23:59");
        assertEquals(new Time(23, 59), e.getDuration());
        assertFalse(e.isAllDay());
    }

    @Test
    public void durationFullDay() {
        assertEquals(new Time(24, 00), e.getDuration());
        assertTrue(e.isAllDay());
    }

    @Test
    public void dayTotalZero() {
        assertEquals(new Time(0, 0), e.getDayTotal());
    }

    @Test
    public void dayTotalNonZero() {
        Event e = new Event(",,,,,48:00");
        assertEquals(new Time(48, 00), e.getDayTotal());
    }

    @Test
    public void createdBy() {
        assertEquals("Michael Charland", e.getCreatedBy());
    }

    @Test
    public void organizedBy() {
        assertEquals("Michael Charland", e.getOrganizedBy());
    }

    @Test
    public void descriptionEmpty() {
        assertEquals("", e.getDescription());
    }

    @Test
    public void descriptionNonEmpty() {
        Event e = new Event(",,,,,,,,xmas");
        assertEquals("xmas", e.getDescription());
    }

    @Test
    public void where() {
        assertEquals("", e.getWhere());
    }

    @Test
    public void whereNonEmpty() {
        Event e = new Event(",,,,,,,,,North Pole");
        assertEquals("North Pole", e.getWhere());
    }

    @Test
    public void calendar() {
        assertEquals("michael.charland@gmail.com", e.getCalendar());
    }

    @Test
    public void longDescription() {
        Event e = new Event(
                new String[] {
                        "2016 Raceteam \"Get-Together\"",
                        "01/10/2016 15:30",
                        "01/10/2016 16:30",
                        "Sun",
                        "01:00",
                        "04:00",
                        "Michael Charland",
                        "Michael Charland",
                        "Hopefully you have all survived Christmas festivities and are looking forward to an amazing New Year! We are very excited about the WCC Raceteam for 2016 and really look forward to a strong season of training, racing and striving for goals. We have an amazing group of athletes ready to bring their A game for WCC and wear the green with pride!\n We would like to have our first team gathering so that everyone can meet and share their season goals. I have attached a race selection form for everyone to complete so that we have an idea of who is racing what races and how important each race is. Please complete it and either return it by email to myself or bring it to our gathering so that we can start to build our race support plan!" });
        assertEquals("2016 Raceteam \"Get-Together", e.getTitle());
        assertDate(10, 1, 2016, 15, 30, e.getStart());
    }

}
