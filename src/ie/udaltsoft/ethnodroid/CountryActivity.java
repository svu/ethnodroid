package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.data.CountryParseResults;
import ie.udaltsoft.ethnodroid.parsers.data.NamedCode;
import ie.udaltsoft.ethnodroid.tasks.ExtractLanguageTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CountryActivity extends EthnodroidActivity {

	public CountryActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.country_layout);
		setBrowserLink();

		final CountryParseResults results = (CountryParseResults) getIntent()
				.getSerializableExtra(RESULTS_EXTRAS);

		final ListView languageList = (ListView) findViewById(R.id.languageList);

		if (results == null) {
			languageList.setVisibility(View.GONE);
			findViewById(R.id.countryNameRow).setVisibility(View.GONE);
			displayErrorMessage(R.string.no_country_info_found);
		} else {
			((TextView) findViewById(R.id.countryNameText)).setText(results
					.getCountryName());
			languageList
					.setAdapter(new ArrayAdapter<NamedCode>(
							this, R.layout.list_item, results.getLanguages()));
			languageList.setOnItemClickListener(languageSelectionListener);
		}

	}

	private final OnItemClickListener languageSelectionListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			final NamedCode language = (NamedCode) parent
					.getItemAtPosition(pos);

			if (language != null) {
				final ExtractLanguageTask extractLanguageTask = new ExtractLanguageTask(
						CountryActivity.this);

				extractLanguageTask.execute(language.getCode());
			}
		}
	};
}
