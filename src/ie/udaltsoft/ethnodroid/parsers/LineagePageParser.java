package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineagePageParser {

	final private Pattern DATA_MATCHER = Pattern
			.compile("\\s*<a HREF=\"show_family.asp\\?subid=(.+)\">(.+)</a>&nbsp;\\((\\d+)\\)\\s*");

	public LineagePageParser() {
	}

	public LineageParseResults parse(BufferedReader rdr) throws IOException {
		String inputLine;
		Matcher m;

		final LineageParseResults results = new LineageParseResults();
		FamilyInfo family = null;

		while ((inputLine = rdr.readLine()) != null) {
			m = DATA_MATCHER.matcher(inputLine);
			if (m.matches()) {
				family = new FamilyInfo();
				family.setCode(m.group(1));
				family.setName(m.group(2));
				family.setNumberOfLanguages(Integer.parseInt(m.group(3)));
				results.getFamilies().add(family);
				continue;
			}
		}
		return results;
	}

}