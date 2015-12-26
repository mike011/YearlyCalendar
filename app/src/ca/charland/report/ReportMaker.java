package ca.charland.report;

import java.util.ArrayList;
import java.util.List;

import ca.charland.calendar.Event;
import ca.charland.io.LoadFile;

public class ReportMaker {
	
	private List<Event> events;
	private static Calendar calendar;

	public static void main(String args[]) throws Exception {
		ReportMaker maker = new ReportMaker();
		maker.getEvents("./test/sample.csv");
		maker.highlightPoints();
		
		Reporter report = new Reporter();
		report.setWidth();
		maker.addPoints(report);
	}

	List<Event> getEvents(String eventsFilename) {
		List<String> eventsFileContents = LoadFile.load(eventsFilename);
		List<String> ignoreList = LoadFile.load("./src/ignore.txt");
		List<Event> events = new ArrayList<Event>();
		for (int i = 1; i < eventsFileContents.size(); i++) {
			String s = eventsFileContents.get(i);
			Event e = new Event(s);
			if(!ignoreList.contains(e.getTitle())) {
				events.add(e);
			}
		}
		return events;
	}

	private void highlightPoints() {
		calendar = new Calendar();
		for (Event e : events) {
			calendar.highlight(e.getStart(), e.getTitle());
		}
	}

	private void addPoints(Reporter reporter)
			throws Exception {
		List<Point> pts = calendar.getDays();
		int y = 0;
		for (Point p : pts) {
			if (p.isHighlighted()) {
				reporter.setBoldedString(p.value, p.x, p.y);
				reporter.setString(p.value, 7 * 3 + 2 + 2, y);
				reporter.setString(p.description, 7 * 3 + 2 + 3, y++);
			} else {
				reporter.setString(p.value, p.x, p.y);
			}
		}
	}

}
