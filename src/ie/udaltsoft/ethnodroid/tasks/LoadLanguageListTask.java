package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.SearchActivity;
import ie.udaltsoft.ethnodroid.parsers.LanguageListPageParser;
import ie.udaltsoft.ethnodroid.parsers.data.GroupedCodes;
import android.content.Intent;

public final class LoadLanguageListTask extends
		LoadingTask<GroupedCodes, LanguageListPageParser> {

	public LoadLanguageListTask(EthnodroidActivity activity) {
		super(activity, null, "language_index", SearchActivity.class);
	}

	@Override
	protected LanguageListPageParser createParser() {
		return new LanguageListPageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null || results.getGroups() == null
				|| results.getGroups().size() == 0) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}

		return true;
	}

	@Override
	protected void customizeIntent(Intent i) {
		i.putExtra(SearchActivity.SEARCH_TYPE,
				SearchActivity.SearchType.LANGUAGE);
	}
}