package ie.udaltsoft.ethnodroid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;

public class LanguagePageParser extends WebPageParser<LanguageParseResults> {

	public static enum State {
		BLANK, POPULATION, LOCATION, LOCATION_MAP, ALTERNATE_NAMES, DIALECTS, CLASSIFICATION, LANGUAGE_USE, LANGUAGE_DEVELOPMENT, WRITING_SYSTEM, COMMENTS;
	};

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

	final private Pattern TD_MATCHER = Pattern.compile("\\s*<td>(.*)</td>\\s*");
	final private Pattern CODE_MATCHER = Pattern
			.compile("\\s*<p><a href=\"ethno_docs/introduction\\.asp#iso_code\".*<a href=\"http://www\\.sil\\.org/iso639-3/documentation\\.asp\\?id=.*\" target=\"_blank\">(.*)</a></p>\\s*");
	final private Pattern COUNTRY_NAME_MATCHER = Pattern
			.compile("\\s*<h2>.* language of <a HREF=\"show_country\\.asp\\?name=([A-Z]+)\">(.*)</a></h2>\\s*");
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
	final private Pattern OTHER_COUNTRY_MATCHER = Pattern
			.compile("\\s*<h4><a HREF=\"show_country.asp\\?name=([A-Z]+)\">(.*)</a></h4>\\s*");

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
			new PatternToStateTuple(COMMENTS_HDR_MATCHER, State.COMMENTS) };

	public LanguagePageParser() {
	}

	@Override
	public LanguageParseResults parse(BufferedReader rdr) throws IOException {
		State state = State.BLANK;
		String inputLine;
		Matcher m;

		final LanguageParseResults results = new LanguageParseResults();
		LanguageParseResults.CountryInfo country = null;

		while ((inputLine = rdr.readLine()) != null) {
			switch (state) {
			case BLANK:
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
					results.getCountries().add(country);
					country.setCountryIsoCode(m.group(1));
					country.setCountryNameText(m.group(2));
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
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setPopulationText(m.group(1));
					state = State.BLANK;
				}
				break;
			case LOCATION:
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLocationText(m.group(1));
					state = State.BLANK;
				}
				break;
			case LOCATION_MAP:
				m = LOCATION_MAP_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageMapURL(m.group(1));
					country.setLanguageMapText(m.group(2));
					state = State.BLANK;
				}
				break;
			case ALTERNATE_NAMES:
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setAlternateNamesText(m.group(1));
					state = State.BLANK;
				}
				break;
			case DIALECTS:
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setDialectsText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case CLASSIFICATION:
				// TODO - link to the family
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setClassificationText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case LANGUAGE_USE:
				// TODO - process links
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageUseText(Html.fromHtml(m.group(1))
							.toString());
					state = State.BLANK;
				}
				break;
			case LANGUAGE_DEVELOPMENT:
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setLanguageDevelopmentText(m.group(1));
					state = State.BLANK;
				}
				break;
			case WRITING_SYSTEM:
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setWritingSystemText(m.group(1));
					state = State.BLANK;
				}
				break;
			case COMMENTS:
				m = TD_MATCHER.matcher(inputLine);
				if (m.matches()) {
					country.setCommentsText(m.group(1));
					state = State.BLANK;
				}
				break;
			}
		}
		return results;
	}

}
