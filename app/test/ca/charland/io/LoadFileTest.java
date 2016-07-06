package ca.charland.io;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class LoadFileTest {

    @Test
    public void testLoadCSVFile() throws Exception {
        assertFalse(LoadFile.loadCSVFile("./test/sample.csv").isEmpty());
    }

    @Test
    public void testMultiLineEvent() throws Exception {
        List<String> load = LoadFile.loadCSVFile("./test/sample2.csv");
        assertFalse(load.isEmpty());
        assertEquals(33, load.size());
    }

}
