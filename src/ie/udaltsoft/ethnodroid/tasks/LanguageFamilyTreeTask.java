package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.LanguageFamilyTreePageParser;
import ie.udaltsoft.ethnodroid.parsers.data.LanguageFamilyTreeParseResults;

public final class LanguageFamilyTreeTask
		extends
		LoadingTask<LanguageFamilyTreeParseResults, LanguageFamilyTreePageParser> {

	public LanguageFamilyTreeTask(EthnodroidActivity activity) {
		super(activity, "show_family.asp?subid=", "show_family", null);
	}

	@Override
	protected LanguageFamilyTreePageParser createParser() {
		return new LanguageFamilyTreePageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}

		return true;
	}
}