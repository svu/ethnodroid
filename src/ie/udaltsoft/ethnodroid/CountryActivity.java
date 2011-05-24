package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.CountryParseResults;
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
			hideErrorMessage();
			((TextView) findViewById(R.id.countryNameText)).setText(results
					.getCountryName());
			languageList
					.setAdapter(new ArrayAdapter<CountryParseResults.LanguageInfo>(
							this, android.R.layout.simple_list_item_1, results
									.getLanguages()));
			languageList.setOnItemClickListener(languageSelectionListener);
		}

	}

	private final OnItemClickListener languageSelectionListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			final CountryParseResults.LanguageInfo language = (CountryParseResults.LanguageInfo) parent
					.getItemAtPosition(pos);

			if (language != null) {
				final ExtractLanguageTask extractLanguageTask = new ExtractLanguageTask(
						CountryActivity.this);

				extractLanguageTask.execute(language.getIsoCode());
			}
		}
	};
}
