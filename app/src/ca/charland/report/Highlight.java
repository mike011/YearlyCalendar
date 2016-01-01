package ca.charland.report;

public class Highlight {

	public static enum Type {
		StatutoryHoliday, Birthday;
	}

	public final Type type;
	public final String description;

	public Highlight(String d, Type t) {
		description = d;
		type = t;
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
}
