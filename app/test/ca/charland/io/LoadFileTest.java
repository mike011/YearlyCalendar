package ca.charland.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoadFileTest {

    @Test
    public void testLoad() throws Exception {
        assertFalse(LoadFile.load("./test/sample.csv").isEmpty());
    }

}
