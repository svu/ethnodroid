package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.LineageActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.LineagePageParser;
import ie.udaltsoft.ethnodroid.parsers.data.FamilyInfo;
import ie.udaltsoft.ethnodroid.parsers.data.LineageParseResults;

public class ClassificationTask extends
		LoadingTask<LineageParseResults, LineagePageParser> {

	public ClassificationTask(EthnodroidActivity activity) {
		super(activity, "show_lang_family.asp?code=", "show_lang_family",
				LineageActivity.class);
	}

	@Override
	protected LineagePageParser createParser() {
		return new LineagePageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null || results.getFamilies().size() == 0) {
			activity.displayErrorMessage(R.string.no_language_family_info_found);
			return false;
		}

		final FamilyInfo fi = results.getFamilies().get(0);

		if (fi.getCode() == null) {
			activity.displayErrorMessage(R.string.no_language_family_info_found);
			return false;
		}
		return true;
	}

};
