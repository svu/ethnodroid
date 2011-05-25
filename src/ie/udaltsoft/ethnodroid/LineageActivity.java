package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.LineageParseResults;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LineageActivity extends EthnodroidActivity {

	public LineageActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lineage_layout);
		setBrowserLink();

		final LineageParseResults results = (LineageParseResults) getIntent()
				.getSerializableExtra(RESULTS_EXTRAS);

		final View lineageList = findViewById(R.id.classificationStack);

		if (results == null) {
			lineageList.setVisibility(View.GONE);
			displayErrorMessage(R.string.no_language_family_info_found);
		} else {
			((ListView) lineageList)
					.setAdapter(new ArrayAdapter<LineageParseResults.FamilyInfo>(
							this, android.R.layout.simple_list_item_1, results
									.getFamilies()));
		}
	}
}
