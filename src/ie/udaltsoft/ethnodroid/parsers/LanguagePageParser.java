package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class LanguagePageParser extends WebPageParser<LanguageParseResults> {

	public static enum State {
		BLANK, POPULATION, LOCATION, LOCATION_MAP, ALTERNATE_NAMES, DIALECTS, CLASSIFICATION, LANGUAGE_USE, LANGUAGE_DEVELOPMENT, WRITING_SYSTEM, COMMENTS, MEMBER_LANGUAGES;
	};

	private State state;

	public static class PatternToStateTuple {
		Pattern pattern;
		State state;

		public PatternToStateTuple(Pattern p, State s) {
			pattern = p;
			state = s;
		}
	};

	private final static String getPatternFromLabel(String label) {
		return "\\s*<td>(<i>)?<a href=\"ethno_docs/introduction\\.asp#" + label
				+ "\".+";
	}

	final private Pattern FULL_TD_MATCHER = Pattern
			.compile("\\s*<td>(.*)</td>\\s*");
	final private Pattern TD_END_MATCHER = Pattern.compile("\\s*</td>\\s*");

	final private Pattern NAME_MATCHER = Pattern
			.compile("\\s*<h1>(.*)</h1>\\s*");
	final private Pattern CODE_MATCHER = Pattern
			.compile("\\s*<p><a href=\"ethno_docs/introduction\\.asp#iso_code\".*<a href=\"http://www\\.sil\\.org/iso639-3/documentation\\.asp\\?id=.*\" target=\"_blank\">(.*)</a></p>\\s*");
	final private Pattern COUNTRY_NAME_MATCHER = Pattern
			.compile("\\s*<h2>.*language of <a HREF=\"show_country\\.asp\\?name=([A-Z]+)\">(.*)</a></h2>\\s*");
	final private Pattern POPULATION_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("population"));
	final private Pattern LOCATION_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("location"));
	final private Pattern LOCATION_MAP_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("language_maps"));
	final private Pattern LOCATION_MAP_MATCHER = Pattern
			.compile("\\s*<a HREF=\"(.*)\">(.*)</a><br>\\s*");
	final private Pattern ALTERNATE_NAMES_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("alt_names"));
	final private Pattern DIALECTS_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("dialect"));
	final private Pattern CLASSIFICATION_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("affiliation"));
	final private Pattern LANGUAGE_USE_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("lguse"));
	final private Pattern LANGUAGE_DEVELOPMENT_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("lgdev"));
	final private Pattern WRITING_SYSTEM_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("writing"));
	final private Pattern COMMENTS_HDR_MATCHER = Pattern
			.compile(getPatternFromLabel("other"));
	final private Pattern MEMBER_LANGUAGES_HDR_MATCHER = Pattern
			.compile("\\s*<td><i>Member languages</i>&nbsp;</td>\\s*");
	final private Pattern OTHER_COUNTRY_MATCHER = Pattern
			.compile("\\s*<h4><a HREF=\"show_country.asp\\?name=([A-Z]+)\">(.*)</a></h4>\\s*");
	final private Pattern SECTION_RESET_MATCHER = Pattern
			.compile("\\s*<tr VALIGN=\"TOP\">\\s*");

	final private Pattern ISO639_MATCHER = Pattern
			.compile("\\s*639-1: ([a-z]+)<br/>\\s*");

	final private PatternToStateTuple[] patternsStateMap = new PatternToStateTuple[] {
			new PatternToStateTuple(POPULATION_HDR_MATCHER, State.POPULATION),
			new PatternToStateTuple(LOCATION_HDR_MATCHER, State.LOCATION),
			new PatternToStateTuple(LOCATION_MAP_HDR_MATCHER,
					State.LOCATION_MAP),
			new PatternToStateTuple(ALTERNATE_NAMES_HDR_MATCHER,
					State.ALTERNATE_NAMES),
			new PatternToStateTuple(DIALECTS_HDR_MATCHER, State.DIALECTS),
			new PatternToStateTuple(CLASSIFICATION_HDR_MATCHER,
					State.CLASSIFICATION),
			new PatternToStateTuple(LANGUAGE_USE_HDR_MATCHER,
					State.LANGUAGE_USE),
			new PatternToStateTuple(LANGUAGE_DEVELOPMENT_HDR_MATCHER,
					State.LANGUAGE_DEVELOPMENT),
			new PatternToStateTuple(WRITING_SYSTEM_HDR_MATCHER,
					State.WRITING_SYSTEM),
			new PatternToStateTuple(COMMENTS_HDR_MATCHER, State.COMMENTS),
			new PatternToStateTuple(MEMBER_LANGUAGES_HDR_MATCHER,
					State.MEMBER_LANGUAGES) };

	public LanguagePageParser() {
	}

	private boolean checkSectionEnd(String inputLine) {
		final Matcher m = SECTION_RESET_MATCHER.matcher(inputLine);
		if (m.matches()) {
			state = State.BLANK;
			return true;
		}
		return false;
	}

	@Override
	public LanguageParseResults parse(BufferedReader rdr) throws IOException {
		state = State.BLANK;
		String inputLine;
		Matcher m;

		final LanguageParseResults results = new LanguageParseResults();
		LanguageParseResults.CountryInfo country = null;

		while ((inputLine = rdr.readLine()) != null) {
			switch (state) {
			case BLANK:
				m = NAME_MATCHER.matcher(inputLine);
				if (m.matches()) {
					results.setLanguageName(m.group(1));
					break;
				}
				m = COUNTRY_NAME_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country = new LanguageParseResults.CountryInfo();
					results.getCountries().add(country);
					country.setCountryIsoCode(m.group(1));
					country.setCountryNameText(m.group(2));
					break;
				}
				m = CODE_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageIsoCode(m.group(1));
					break;
				}
				m = OTHER_COUNTRY_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country = new LanguageParseResults.CountryInfo();
					country.setCountryIsoCode(m.group(1));
					country.setCountryNameText(m.group(2));
					results.getCountries().add(country);
					break;
				}
				for (PatternToStateTuple p2s : patternsStateMap) {
					if (p2s.pattern.matcher(inputLine).matches()) {
						state = p2s.state;
						break;
					}
				}
				break;
			case POPULATION:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setPopulationText(m.group(1));
					state = State.BLANK;
				}
				break;
			case LOCATION:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLocationText(m.group(1));
					state = State.BLANK;
				}
				break;
			case LOCATION_MAP:
				if (checkSectionEnd(inputLine))
					break;
				m = LOCATION_MAP_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageMapURL(m.group(1));
					country.setLanguageMapText(m.group(2));
					state = State.BLANK;
				}
				break;
			case ALTERNATE_NAMES:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setAlternateNamesText(m.group(1));
					state = State.BLANK;
				}
				break;
			case DIALECTS:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setDialectsText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case CLASSIFICATION:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setClassificationText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case LANGUAGE_USE:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageUseText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case LANGUAGE_DEVELOPMENT:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageDevelopmentText(m.group(1));
					state = State.BLANK;
				}
				break;
			case WRITING_SYSTEM:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setWritingSystemText(m.group(1));
					state = State.BLANK;
				}
				break;
			case COMMENTS:
				if (checkSectionEnd(inputLine))
					break;
				m = FULL_TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setCommentsText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case MEMBER_LANGUAGES:
				if (checkSectionEnd(inputLine))
					break;
				m = TD_END_MATCHER.matcher(inputLine);
				if (m.matches()) {
					state = State.BLANK;
				} else {
					final String s = country.getMemberLanguagesText();
					final String d = Html.fromHtml(inputLine.trim()).toString();
					country.setMemberLanguagesText(s == null ? d
							: (s + "\n" + d));
				}
				break;
			}
		}
		return results;
	}

	public void parseExtra(BufferedReader rdr, LanguageParseResults results)
			throws IOException {
		Matcher m = null;
		String inputLine;
		while ((inputLine = rdr.readLine()) != null) {
			m = ISO639_MATCHER.matcher(inputLine);
			if (m.matches()) {
				results.setIso639_1Code(m.group(1));
				break;
			}
		}
	}

}
