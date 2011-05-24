package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.LanguageActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.LanguagePageParser;
import ie.udaltsoft.ethnodroid.parsers.LanguageParseResults;

public final class ExtractLanguageTask extends
		LoadingTask<LanguageParseResults, LanguagePageParser> {

	public ExtractLanguageTask(EthnodroidActivity activity) {
		super(activity, "show_language.asp?code=", "show_language",
				LanguageActivity.class);
	}

	@Override
	protected LanguagePageParser createParser() {
		return new LanguagePageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null || results.getCountries() == null
				|| results.getCountries().size() == 0) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}

		final LanguageParseResults.CountryInfo ci = results.getCountries().get(
				0);

		if (ci.getLanguageIsoCode() == null) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}

		if (!ci.getLanguageIsoCode().equals(queryParameter)) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}
		return true;
	}

}