package ca.charland.report;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ReportMakerTest {

	@Test
	public void testGetEvents() throws Exception {
		ReportMaker rm = new ReportMaker();
		assertEquals(307, rm.getEvents("./test/sample.csv").size());
	}

}
