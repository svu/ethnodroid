package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.CountryInfo;
import ie.udaltsoft.ethnodroid.parsers.ParseResults;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class LanguagesActivity extends Activity {

	public LanguagesActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.language_layout);

		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));

		final TextView languageMapText = (TextView) findViewById(R.id.languageMapText);
		languageMapText.setOnClickListener(mLanguageMapListener);

		resetFields();

		final ParseResults results = (ParseResults) getIntent()
				.getSerializableExtra("results");
		if (results != null) {
			displayCountry(results.getCountries().get(0));
		}

		final Spinner languageCountriesList = ((Spinner) findViewById(R.id.languageCountriesList));
		if (results.getCountries().size() > 1) {
			final ArrayAdapter<CountryInfo> lcla = new ArrayAdapter<CountryInfo>(
					this, android.R.layout.simple_spinner_dropdown_item,
					results.getCountries());
			languageCountriesList.setAdapter(lcla);
			languageCountriesList
					.setOnItemSelectedListener(mCountryListListener);
		} else {
			languageCountriesList.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void resetFields() {
		((TextView) findViewById(R.id.languageIsoCodesText)).setText("");
		((TextView) findViewById(R.id.countryIsoCodeText)).setText("");
		((TextView) findViewById(R.id.countryNameText)).setText("");
		((TextView) findViewById(R.id.populationText)).setText("");
		((TextView) findViewById(R.id.regionText)).setText("");
		((TextView) findViewById(R.id.languageMapText)).setText("");
		((TextView) findViewById(R.id.languageMapText)).setTag("");
		((TextView) findViewById(R.id.alternateNamesText)).setText("");
		((TextView) findViewById(R.id.dialectsText)).setText("");
		((TextView) findViewById(R.id.classificationText)).setText("");
		((TextView) findViewById(R.id.languageUseText)).setText("");
		((TextView) findViewById(R.id.languageDevelopmentText)).setText("");
		((TextView) findViewById(R.id.writingSystemText)).setText("");
		((TextView) findViewById(R.id.commentsText)).setText("");
	}

	private OnClickListener mLanguageMapListener = new OnClickListener() {
		public void onClick(View v) {
			final Object url = ((TextView) findViewById(R.id.languageMapText))
					.getTag();
			if (url == null)
				return;
			final Intent myIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(url.toString()));
			startActivity(myIntent);
		}
	};

	private OnItemSelectedListener mCountryListListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			final CountryInfo country = (CountryInfo) parent
					.getItemAtPosition(pos);
			if (country != null)
				displayCountry(country);
		}

		public void onNothingSelected(AdapterView<?> parent) {
			resetFields();
		}
	};

	public void displayCountry(CountryInfo ci) {
		((TextView) findViewById(R.id.languageIsoCodesText)).setText(ci
				.getLanguageIsoCode());
		((TextView) findViewById(R.id.countryIsoCodeText)).setText(ci
				.getCountryIsoCode());
		((TextView) findViewById(R.id.countryNameText)).setText(ci
				.getCountryNameText());
		((TextView) findViewById(R.id.populationText)).setText(ci
				.getPopulationText());
		((TextView) findViewById(R.id.regionText))
				.setText(ci.getLocationText());
		((TextView) findViewById(R.id.languageMapText)).setText(ci
				.getLanguageMapText());
		((TextView) findViewById(R.id.languageMapText)).setTag(ci
				.getLanguageMapURL() == null ? null : getText(
				R.string.ethnologue_url).toString()
				+ "/" + ci.getLanguageMapURL());
		((TextView) findViewById(R.id.alternateNamesText)).setText(ci
				.getAlternateNamesText());
		((TextView) findViewById(R.id.dialectsText)).setText(ci
				.getDialectsText());
		((TextView) findViewById(R.id.classificationText)).setText(ci
				.getClassificationText());
		((TextView) findViewById(R.id.languageUseText)).setText(ci
				.getLanguageUseText());
		((TextView) findViewById(R.id.languageDevelopmentText)).setText(ci
				.getLanguageDevelopmentText());
		((TextView) findViewById(R.id.writingSystemText)).setText(ci
				.getWritingSystemText());
		((TextView) findViewById(R.id.commentsText)).setText(ci
				.getCommentsText());
	}
}
