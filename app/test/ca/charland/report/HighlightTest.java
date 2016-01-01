package ca.charland.report;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class HighlightTest {

	@Test
	public void isNotBirthday() throws Exception {
		Highlight h = new Highlight("dog");
		assertFalse(Highlight.Type.Birthday.equals(h.type));
	}
	
	@Test
	public void isBirthday() throws Exception {
		Highlight h = new Highlight("Birthday");
		assertTrue(Highlight.Type.Birthday.equals(h.type));
	}

}
