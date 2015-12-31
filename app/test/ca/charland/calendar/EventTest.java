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
        assertDate("01/01/16 12:00 AM", e.getStart());
    }

    private void assertDate(String string, Date d) {
        assertNotNull("Date is null", d);
        assertEquals(string, new SimpleDateFormat().format(d));
    }
    
    @Test
    public void getEnd() {
        assertDate("02/01/16 12:00 AM", e.getEnd());
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
        assertEquals(new Time(0,0), e.getDayTotal());
    }
    
    @Test
    public void dayTotalNonZero() {
        Event e = new Event(",,,,,48:00");
        assertEquals(new Time(48,00), e.getDayTotal());
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
    
}
