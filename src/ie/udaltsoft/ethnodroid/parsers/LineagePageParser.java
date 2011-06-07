package ie.udaltsoft.ethnodroid.parsers;

import ie.udaltsoft.ethnodroid.parsers.data.FamilyInfo;
import ie.udaltsoft.ethnodroid.parsers.data.LineageParseResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineagePageParser extends WebPageParser<LineageParseResults> {

	final private Pattern DATA_MATCHER = Pattern
			.compile("\\s*<a HREF=\"show_family\\.asp\\?subid=(.+)\">(.+)</a>&nbsp;\\((\\d+)\\)\\s*");

	public LineagePageParser() {
	}

	@Override
	public LineageParseResults parse(BufferedReader rdr) throws IOException {
		String inputLine;
		Matcher m;

		final LineageParseResults results = new LineageParseResults();
		FamilyInfo family = null;

		while ((inputLine = rdr.readLine()) != null) {
			m = DATA_MATCHER.matcher(inputLine);
			if (m.matches()) {
				family = new FamilyInfo(m.group(1), m.group(2),
						Integer.parseInt(m.group(3)));
				results.getFamilies().add(family);
				continue;
			}
		}
		return results;
	}

}
