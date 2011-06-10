package ie.udaltsoft.ethnodroid.parsers;

import ie.udaltsoft.ethnodroid.parsers.data.GroupedCodes;
import ie.udaltsoft.ethnodroid.parsers.data.NamedCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class LanguageListPageParser extends WebPageParser<GroupedCodes> {

	private static final Pattern REGION_START_MATCHER = Pattern
			.compile("\\s*/([A-Z])/\\s*");
	private static final Pattern LANGUAGE_MATCHER = Pattern
			.compile("\\s*<a HREF=\"show_language.asp\\?code=([a-z]+)\">(.*)</a>(.*)<br>\\s*");

	public LanguageListPageParser() {
	}

	@Override
	public GroupedCodes parse(BufferedReader rdr) throws IOException {
		String inputLine;
		Matcher m;
		int regionIdx = -1;
		ArrayList<NamedCode> currentRegion = null;

		final GroupedCodes results = new GroupedCodes();

		while ((inputLine = rdr.readLine()) != null) {
			m = REGION_START_MATCHER.matcher(inputLine);
			if (m.matches()) {
				final String letter = m.group(1);
				regionIdx++;
				currentRegion = new ArrayList<NamedCode>();
				results.getGroups().put(letter, currentRegion);
				continue;
			}
			m = LANGUAGE_MATCHER.matcher(inputLine);
			if (m.matches()) {
				final NamedCode newLang = new NamedCode(m.group(1), Html
						.fromHtml(m.group(2) + " " + m.group(3)).toString());
				currentRegion.add(newLang);
				continue;
			}
		}
		return results;
	}
}
