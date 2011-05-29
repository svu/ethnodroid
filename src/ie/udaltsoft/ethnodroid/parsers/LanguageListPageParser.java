package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class LanguageListPageParser extends WebPageParser<GroupedCodes> {

	private static final Pattern REGION_START_MATCHER = Pattern
			.compile("\\s*/([A-Z])/\\s*");
	private static final Pattern LANGUAGE_MATCHER = Pattern
			.compile("\\s*<a HREF=\"show_language.asp\\?code=([a-z]+)\">(.*)</a><br>\\s*");

	public LanguageListPageParser() {
	}

	@Override
	public GroupedCodes parse(BufferedReader rdr) throws IOException {
		String inputLine;
		Matcher m;
		int regionIdx = -1;
		ArrayList<GroupedCodes.CodeRef> currentRegion = null;

		final GroupedCodes results = new GroupedCodes();
		results.setGroups(new HashMap<String, ArrayList<GroupedCodes.CodeRef>>());

		while ((inputLine = rdr.readLine()) != null) {
			m = REGION_START_MATCHER.matcher(inputLine);
			if (m.matches()) {
				final String letter = m.group(1);
				regionIdx++;
				currentRegion = new ArrayList<GroupedCodes.CodeRef>();
				results.getGroups().put(letter, currentRegion);
				continue;
			}
			m = LANGUAGE_MATCHER.matcher(inputLine);
			if (m.matches()) {
				final GroupedCodes.CodeRef newLang = new GroupedCodes.CodeRef();
				newLang.setCode(m.group(1));
				newLang.setName(Html.fromHtml(m.group(2)).toString());
				currentRegion.add(newLang);
				continue;
			}
		}
		return results;
	}
}
