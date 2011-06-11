package ie.udaltsoft.ethnodroid.parsers;

import ie.udaltsoft.ethnodroid.parsers.data.GroupedCodes;
import ie.udaltsoft.ethnodroid.parsers.data.NamedCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class CountryListPageParser extends WebPageParser<GroupedCodes> {

	private static GroupedCodes results = null;

	private final static String[] regions = { "Africa", "Americas", "Asia",
			"Europe", "Pacific" };

	private static final Pattern REGION_START_MATCHER = Pattern
			.compile("\\s*<td>\\s*");
	private static final Pattern COUNTRY_MATCHER = Pattern
			.compile("\\s*<a href=\"show_country.asp\\?name=([A-Z]+)\">\\s*");

	public CountryListPageParser() {
	}

	@Override
	public GroupedCodes parse(BufferedReader rdr) throws IOException {

		if (results != null)
			return results;

		String inputLine;
		int regionIdx = -1;
		ArrayList<NamedCode> currentRegion = null;

		results = new GroupedCodes();

		while ((inputLine = rdr.readLine()) != null) {
			Matcher m = REGION_START_MATCHER.matcher(inputLine);
			if (m.matches()) {
				regionIdx++;
				currentRegion = new ArrayList<NamedCode>();
				results.getGroups().put(regions[regionIdx], currentRegion);
				continue;
			}
			m = COUNTRY_MATCHER.matcher(inputLine);
			if (m.matches()) {
				final NamedCode newCountry = new NamedCode(m.group(1), null);
				inputLine = rdr.readLine();
				if (inputLine != null)
					newCountry.setName(Html.fromHtml(inputLine.trim())
							.toString());
				currentRegion.add(newCountry);
				continue;
			}
		}
		return results;
	}
}
