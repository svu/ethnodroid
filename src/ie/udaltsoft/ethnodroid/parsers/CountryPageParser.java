package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryPageParser extends WebPageParser<CountryParseResults> {

	final private Pattern TITLE_MATCHER = Pattern
			.compile("\\s*<h1>Languages of (.*)</h1>\\s*");
	final private Pattern ROW_MATCHER = Pattern
			.compile("\\s*<tr VALIGN=\"TOP\"><td WIDTH=\"25%\">(.+)</td>\\s*");
	final private Pattern INFO_MATCHER = Pattern
			.compile("\\s*<br><a HREF=\"show_language\\.asp\\?code=([a-z]+)\">More information\\.</a>\\s*");

	public CountryPageParser() {
	}

	@Override
	public CountryParseResults parse(BufferedReader rdr) throws IOException {
		String inputLine;
		Matcher m;
		String languageName = null;

		final CountryParseResults results = new CountryParseResults();
		CountryParseResults.LanguageInfo language = null;

		while ((inputLine = rdr.readLine()) != null) {
			m = TITLE_MATCHER.matcher(inputLine);
			if (m.matches()) {
				results.setCountryName(m.group(1));
				continue;
			}

			m = ROW_MATCHER.matcher(inputLine);
			if (m.matches()) {
				languageName = m.group(1);
				continue;
			}

			m = INFO_MATCHER.matcher(inputLine);
			if (m.matches()) {
				language = new CountryParseResults.LanguageInfo();
				language.setIsoCode(m.group(1));
				language.setName(languageName);
				results.getLanguages().add(language);
				continue;
			}
		}
		return results;
	}

}
