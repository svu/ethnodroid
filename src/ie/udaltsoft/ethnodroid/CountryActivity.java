package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.data.CountryParseResults;
import ie.udaltsoft.ethnodroid.parsers.data.NamedCode;
import ie.udaltsoft.ethnodroid.tasks.ExtractCountryTask;
import ie.udaltsoft.ethnodroid.tasks.ExtractLanguageTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CountryActivity extends EthnodroidActivity {

	private CountryParseResults countryParseResults;

	public CountryActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.country_layout);
		setBrowserLink();

		countryParseResults = (CountryParseResults) getIntent()
				.getSerializableExtra(RESULTS_EXTRAS);

		final ListView itemList = (ListView) findViewById(R.id.languageList);

		if (countryParseResults == null) {
			itemList.setVisibility(View.GONE);
			findViewById(R.id.countryNameRow).setVisibility(View.GONE);
			displayErrorMessage(R.string.no_country_info_found);
		} else {
			((TextView) findViewById(R.id.countryNameText))
					.setText(countryParseResults.getCountryName());

			itemList.setAdapter(new ArrayAdapter<NamedCode>(this,
					R.layout.list_item, isCountryList() ? countryParseResults
							.getSubcountries() : countryParseResults
							.getLanguages()));
			itemList.setOnItemClickListener(languageSelectionListener);
		}

	}

	private boolean isCountryList() {
		return countryParseResults.getLanguages().size() == 0;
	}

	private final OnItemClickListener languageSelectionListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			final NamedCode item = (NamedCode) parent.getItemAtPosition(pos);

			if (item != null) {
				if (isCountryList()) {
					new ExtractCountryTask(CountryActivity.this).execute(item
							.getCode());
				} else {
					new ExtractLanguageTask(CountryActivity.this).execute(item
							.getCode());
				}
			}
		}
	};
}
