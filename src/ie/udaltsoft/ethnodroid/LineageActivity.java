package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.data.FamilyInfo;
import ie.udaltsoft.ethnodroid.parsers.data.LineageParseResults;
import ie.udaltsoft.ethnodroid.tasks.LanguageFamilyTreeTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LineageActivity extends EthnodroidActivity {

	private OnItemClickListener familySelectionListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			final FamilyInfo item = (FamilyInfo) parent.getItemAtPosition(pos);

			if (item != null) {
				new LanguageFamilyTreeTask(LineageActivity.this).execute(item
						.getCode());
			}
		}
	};

	public LineageActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lineage_layout);
		setBrowserLink();

		final LineageParseResults results = (LineageParseResults) getIntent()
				.getSerializableExtra(RESULTS_EXTRAS);

		final ListView lineageList = (ListView) findViewById(R.id.classificationStack);

		if (results == null) {
			lineageList.setVisibility(View.GONE);
			displayErrorMessage(R.string.no_language_family_info_found);
		} else {
			lineageList.setAdapter(new ArrayAdapter<FamilyInfo>(this,
					R.layout.list_item, results.getFamilies()));
			lineageList.setOnItemClickListener(familySelectionListener);
		}
	}
}
