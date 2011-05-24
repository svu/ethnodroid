package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.CountryInfo;
import ie.udaltsoft.ethnodroid.parsers.FamilyInfo;
import ie.udaltsoft.ethnodroid.parsers.LanguageParseResults;
import ie.udaltsoft.ethnodroid.parsers.LineagePageParser;
import ie.udaltsoft.ethnodroid.parsers.LineageParseResults;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class LanguageActivity extends EthnodroidActivity {

	private CountryInfo displayedCountryInfo;

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

		resetFields();

		final LanguageParseResults results = (LanguageParseResults) getIntent()
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

		hideErrorMessage();
	}

	private void resetFields() {
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

	private OnClickListener mLanguageMapListener = new OnClickListener() {
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

	private OnClickListener mClassificationListener = new OnClickListener() {
		public void onClick(View v) {

			final AsyncTask<String, Integer, String> extractLanguageTask = new AsyncTask<String, Integer, String>() {
				private ProgressDialog dialog;
				private LineageParseResults results;

				@Override
				protected void onPreExecute() {
					hideErrorMessage();

					dialog = new ProgressDialog(LanguageActivity.this);
					dialog.setMessage(getText(R.string.loading));
					dialog.setIndeterminate(true);
					dialog.setCancelable(false);
					dialog.show();
				}

				@Override
				protected String doInBackground(String... params) {
					HttpURLConnection urlConnection = null;
					try {
						final InputStream is;
						final BufferedReader rdr;
						if (isRemoteLoading()) {
							final URL url = new URL(
									"http://www.ethnologue.com/show_lang_family.asp?code="
											+ params[0]);

							urlConnection = (HttpURLConnection) url
									.openConnection();
							is = urlConnection.getInputStream();
							rdr = new BufferedReader(new InputStreamReader(is));
						} else {
							final AssetManager am = getAssets();
							is = am.open("show_lang_family.eng.txt");
							rdr = new BufferedReader(new InputStreamReader(is,
									Charset.forName("windows-1252")));
						}

						final LineagePageParser lineagePageParser = new LineagePageParser();
						results = lineagePageParser.parse(rdr);

					} catch (Exception ex) {
						return ex.toString();
					} finally {
						if (urlConnection != null)
							urlConnection.disconnect();
					}
					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					dialog.hide();

					if (result != null) {
						displayErrorMessage(result);
						return;
					}

					if (results == null || results.getFamilies() == null
							|| results.getFamilies().size() == 0) {
						displayErrorMessage(getText(R.string.no_language_info_found));
						return;
					}

					final FamilyInfo fi = results.getFamilies().get(0);

					if (fi.getCode() == null) {
						displayErrorMessage(getText(R.string.no_language_info_found));
						return;
					}

					final Intent i = new Intent(LanguageActivity.this,
							LineageActivity.class);
					i.putExtra("results", results);
					startActivity(i);
				}
			};

			extractLanguageTask.execute(displayedCountryInfo
					.getLanguageIsoCode());
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

	private void setFormFieldText(int id, String value) {
		((TextView) findViewById(id)).setText(value);
	}

	private void populateRow(int rowId, int textId, String textValue) {
		if (textValue == null)
			findViewById(rowId).setVisibility(View.GONE);
		else
			setFormFieldText(textId, textValue);

	}

	public void displayCountry(CountryInfo ci) {
		displayedCountryInfo = ci;

		populateRow(R.id.languageIsoCodeRow, R.id.languageIsoCodeText,
				ci.getLanguageIsoCode());
		populateRow(R.id.countryIsoCodeRow, R.id.countryIsoCodeText,
				ci.getCountryIsoCode());
		populateRow(R.id.countryNameRow, R.id.countryNameText,
				ci.getCountryNameText());
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
