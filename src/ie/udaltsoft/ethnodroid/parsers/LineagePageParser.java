package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class LineagePageParser {

	public LineagePageParser() {
	}

	public LineageParseResults parse(BufferedReader rdr) throws IOException {
		String inputLine;
		Matcher m;

		final LineageParseResults results = new LineageParseResults();
		FamilyInfo family = null;

		while ((inputLine = rdr.readLine()) != null) {
		}
		return results;
	}

}
