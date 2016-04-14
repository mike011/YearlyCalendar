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
        List<String> load = LoadFile.loadCSVFile("E:/downloads/6c12bf61e114bbebb5b61c3d7dcbf464.csv");
        assertFalse(load.isEmpty());
        assertEquals(2, load.size());
    }

}
