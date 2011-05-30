package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.CountryActivity;
import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.CountryPageParser;
import ie.udaltsoft.ethnodroid.parsers.data.CountryParseResults;

public final class ExtractCountryTask extends
		LoadingTask<CountryParseResults, CountryPageParser> {

	public ExtractCountryTask(EthnodroidActivity activity) {
		super(activity, "show_country.asp?name=", "show_country",
				CountryActivity.class);
	}

	@Override
	protected CountryPageParser createParser() {
		return new CountryPageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null
				|| (results.getLanguages().size() == 0 && results
						.getSubcountries().size() == 0)) {
			activity.displayErrorMessage(R.string.no_country_info_found);
			return false;
		}

		return true;
	}

}