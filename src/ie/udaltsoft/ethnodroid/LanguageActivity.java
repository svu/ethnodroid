package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.LanguageParseResults;
import ie.udaltsoft.ethnodroid.tasks.ClassificationTask;
import ie.udaltsoft.ethnodroid.tasks.ExtractCountryTask;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class LanguageActivity extends EthnodroidActivity {

	private LanguageParseResults displayedLanguageInfo;
	private LanguageParseResults.CountryInfo displayedCountryInfo;

	public LanguageActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.language_layout);

		setBrowserLink();

		findViewById(R.id.languageMapText).setOnClickListener(
				mLanguageMapListener);

		findViewById(R.id.classificationText).setOnClickListener(
				mClassificationListener);

		findViewById(R.id.countryNameText).setOnClickListener(
				mCountryNameListener);

		resetFields();

		displayedLanguageInfo = (LanguageParseResults) getIntent()
				.getSerializableExtra("results");
		if (displayedLanguageInfo != null) {
			displayCountry(displayedLanguageInfo, displayedLanguageInfo
					.getCountries().get(0));
		}

		final Spinner languageCountriesList = ((Spinner) findViewById(R.id.languageCountriesList));
		if (displayedLanguageInfo.getCountries().size() > 1) {
			final ArrayAdapter<LanguageParseResults.CountryInfo> lcla = new ArrayAdapter<LanguageParseResults.CountryInfo>(
					this, android.R.layout.simple_spinner_dropdown_item,
					displayedLanguageInfo.getCountries());
			languageCountriesList.setAdapter(lcla);
			languageCountriesList
					.setOnItemSelectedListener(mCountryListListener);
		} else {
			languageCountriesList.setVisibility(View.GONE);
		}
	}

	private void resetFields() {
		((TextView) findViewById(R.id.languageNameText)).setText("");
		((TextView) findViewById(R.id.languageIsoCodeText)).setText("");
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

	private final OnClickListener mLanguageMapListener = new OnClickListener() {
		public void onClick(View v) {
			final Object url = findViewById(R.id.languageMapText).getTag();
			if (url == null)
				return;

			final Intent myIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getText(R.string.ethnologue_url).toString() + "/"
							+ url.toString()));
			startActivity(myIntent);
		}
	};

	private final OnClickListener mClassificationListener = new OnClickListener() {
		public void onClick(View v) {

			final ClassificationTask extractClassificationTask = new ClassificationTask(
					LanguageActivity.this);

			extractClassificationTask.execute(displayedCountryInfo
					.getLanguageIsoCode());
		}
	};

	private final OnItemSelectedListener mCountryListListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			final LanguageParseResults.CountryInfo country = (LanguageParseResults.CountryInfo) parent
					.getItemAtPosition(pos);
			if (country != null)
				displayCountry(displayedLanguageInfo, country);
		}

		public void onNothingSelected(AdapterView<?> parent) {
			resetFields();
		}
	};

	private final OnClickListener mCountryNameListener = new OnClickListener() {

		public void onClick(View view) {
			final String countryIsoCode = (String) view.getTag();

			if (countryIsoCode != null) {
				final ExtractCountryTask extractCountryTask = new ExtractCountryTask(
						LanguageActivity.this);

				extractCountryTask.execute(countryIsoCode);
			}
		}
	};

	private void setFormFieldText(int id, String value) {
		((TextView) findViewById(id)).setText(value);
	}

	private void populateRow(int rowId, int textId, String textValue) {
		if (textValue == null) {
			findViewById(rowId).setVisibility(View.GONE);
			findViewById(textId).setVisibility(View.GONE);
		} else {
			findViewById(rowId).setVisibility(View.VISIBLE);
			findViewById(textId).setVisibility(View.VISIBLE);
			setFormFieldText(textId, textValue);
		}
	}

	public void displayCountry(LanguageParseResults results,
			LanguageParseResults.CountryInfo ci) {
		displayedCountryInfo = ci;

		populateRow(R.id.languageNameRow, R.id.languageNameText,
				results.getLanguageName());
		populateRow(R.id.languageIsoCodeRow, R.id.languageIsoCodeText,
				ci.getLanguageIsoCode());
		populateRow(R.id.languageIso6391CodeRow, R.id.languageIso6391CodeText,
				results.getIso639_1Code());
		populateRow(R.id.countryIsoCodeRow, R.id.countryIsoCodeText,
				ci.getCountryIsoCode());
		populateRow(R.id.countryNameRow, R.id.countryNameText,
				ci.getCountryNameText());

		findViewById(R.id.countryNameText).setTag(ci.getCountryIsoCode());

		populateRow(R.id.populationRow, R.id.populationText,
				ci.getPopulationText());
		populateRow(R.id.regionRow, R.id.regionText, ci.getLocationText());
		populateRow(R.id.languageMapRow, R.id.languageMapText,
				ci.getLanguageMapText());

		findViewById(R.id.languageMapText).setTag(ci.getLanguageMapURLParams());

		populateRow(R.id.alternateNamesRow, R.id.alternateNamesText,
				ci.getAlternateNamesText());
		populateRow(R.id.dialectsRow, R.id.dialectsText, ci.getDialectsText());
		populateRow(R.id.classificationRow, R.id.classificationText,
				ci.getClassificationText());
		populateRow(R.id.languageUseRow, R.id.languageUseText,
				ci.getLanguageUseText());
		populateRow(R.id.languageDevelopmentRow, R.id.languageDevelopmentText,
				ci.getLanguageDevelopmentText());
		populateRow(R.id.writingSystemRow, R.id.writingSystemText,
				ci.getWritingSystemText());
		populateRow(R.id.commentsRow, R.id.commentsText, ci.getCommentsText());
	}
}
