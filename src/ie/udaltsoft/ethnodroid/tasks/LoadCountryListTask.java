package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.SearchActivity;
import ie.udaltsoft.ethnodroid.parsers.CountryListPageParser;
import ie.udaltsoft.ethnodroid.parsers.GroupedCodes;
import android.content.Intent;

public final class LoadCountryListTask extends
		LoadingTask<GroupedCodes, CountryListPageParser> {

	public LoadCountryListTask(EthnodroidActivity activity) {
		super(activity, null, "country_index",
				SearchActivity.class);
	}

	@Override
	protected CountryListPageParser createParser() {
		return new CountryListPageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null || results.getGroups() == null
				|| results.getGroups().size() == 0) {
			activity.displayErrorMessage(R.string.no_country_info_found);
			return false;
		}

		return true;
	}

	@Override
	protected void customizeIntent(Intent i) {
		i.putExtra(SearchActivity.SEARCH_TYPE,
				SearchActivity.SearchType.COUNTRY);
	}
}