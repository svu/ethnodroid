package ie.udaltsoft.ethnodroid.parsers;

import ie.udaltsoft.ethnodroid.parsers.data.FamilyInfo;
import ie.udaltsoft.ethnodroid.parsers.data.LanguageFamilyTreeParseResults;
import ie.udaltsoft.ethnodroid.parsers.data.NamedCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageFamilyTreePageParser extends
		WebPageParser<LanguageFamilyTreeParseResults> {

	private LanguageFamilyTreeParseResults parseResults;
	private ArrayList<FamilyInfo> currentFamilies;

	private final static Pattern CURRENT_POINT_MATCHER = Pattern
			.compile("\\s*<p class=\"level(\\d+)\">(.*)&nbsp;\\((\\d*)\\)</p>\\s*");

	private final static Pattern ONELINE_FAMILY_MATCHER = Pattern
			.compile("\\s*<p class=\"level(\\d+)\"><a HREF=\"show_family.asp\\?subid=(.+)\">(.+)</a>\\s*&nbsp;\\((\\d+)\\)\\s*</p>\\s*");

	private final static Pattern SECTION_START_MATCHER = Pattern
			.compile("\\s*<p class=\"level(\\d+)\">\\s*");

	private final static Pattern LANGUAGE_MATCHER = Pattern
			.compile("\\s*&nbsp;\\[<a href=\"show_language.asp\\?code=(\\w+)\">(\\w+)</a>\\]\\s*");

	private final static Pattern FAMILY_MATCHER = Pattern
			.compile("\\s*<a HREF=\"show_family.asp\\?subid=(.*)\">(.*)</a>\\s*&nbsp;\\((\\d+)\\)\\s*");

	public LanguageFamilyTreePageParser() {
	}

	public static enum SectionState {
		NONE, NAME, DETAILS
	};

	@Override
	public LanguageFamilyTreeParseResults parse(BufferedReader rdr)
			throws IOException {

		parseResults = new LanguageFamilyTreeParseResults();
		String inputLine = null;
		Matcher m = null;
		int currentLevel = -1; // 0-based
		String currentName = null;

		currentFamilies = new ArrayList<FamilyInfo>();

		SectionState state = SectionState.NONE;

		while ((inputLine = rdr.readLine()) != null) {

			switch (state) {
			case NONE:
				m = CURRENT_POINT_MATCHER.matcher(inputLine);
				if (m.matches()) {
					createFamilyInfo(Integer.parseInt(m.group(1)), null,
							m.group(2), Integer.parseInt(m.group(3)));
					break;
				}

				m = ONELINE_FAMILY_MATCHER.matcher(inputLine);
				if (m.matches()) {
					createFamilyInfo(Integer.parseInt(m.group(1)), m.group(2),
							m.group(3), Integer.parseInt(m.group(4)));
					break;
				}

				m = SECTION_START_MATCHER.matcher(inputLine);
				if (m.matches()) {
					currentLevel = Integer.parseInt(m.group(1));
					state = SectionState.NAME;
					break;
				}
				break;
			case NAME:
				currentName = inputLine.trim();
				state = SectionState.DETAILS;
				break;
			case DETAILS:
				m = LANGUAGE_MATCHER.matcher(inputLine);
				if (m.matches()) {
					createLanguageInfo(currentLevel, m.group(1), currentName);
					state = SectionState.NONE;
					break;
				}
				m = FAMILY_MATCHER.matcher(inputLine);
				if (m.matches()) {
					createFamilyInfo(currentLevel, m.group(1), m.group(2),
							Integer.parseInt(m.group(3)));
					state = SectionState.NONE;
					break;
				}
			}
		}
		return parseResults;
	}

	private void createLanguageInfo(int level, String code, String name) {
		level = level - 1; // in html it is 1-based
		if (level > 0) {
			final NamedCode newLanguage = new NamedCode(code, name);
			ensureFamiliesSize(level);
			currentFamilies.get(level - 1).getLanguages().add(newLanguage);
		}
	}

	private void createFamilyInfo(int level, String code, String name,
			int numberOfLanguages) {
		final FamilyInfo newFamily = new FamilyInfo(code, name,
				numberOfLanguages);
		level = level - 1;
		ensureFamiliesSize(level);

		if (currentFamilies.size() > level) {
			currentFamilies.set(level, newFamily);
			// reset the "lower levels"
			for (int l = level + 1; l < currentFamilies.size(); l++)
				currentFamilies.set(l, newFamily);
		} else
			currentFamilies.add(newFamily);

		if (level > 0) {
			currentFamilies.get(level - 1).getSubfamilies().add(newFamily);
		} else
			parseResults.setTopFamily(newFamily);
	}

	private void ensureFamiliesSize(int newSize) {
		if (currentFamilies.size() > 0) {
			final FamilyInfo lastElem = currentFamilies.get(currentFamilies
					.size() - 1);
			// Propagate the last one
			while (currentFamilies.size() < newSize)
				currentFamilies.add(lastElem);
		}
	}

}
