/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.udaltsoft.ethnodroid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class LanguagesActivity extends Activity {

	static final private int SEARCH_ID = Menu.FIRST;
	static final private int CLEAR_ID = Menu.FIRST + 1;

	private EditText mEditor;

	public LanguagesActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.language_layout);

		// Find the text editor view inside the layout, because we
		// want to do various programmatic things with it.
		mEditor = (EditText) findViewById(R.id.languagePattern);

		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));

		final TextView languageMapText = (TextView) findViewById(R.id.languageMapText);
		languageMapText.setOnClickListener(mLanguageMapListener);

		((Button) findViewById(R.id.search))
				.setOnClickListener(mSearchListener);
		((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
		mEditor.setOnKeyListener(mSearchKeyListener);
		mEditor.setText("");
		resetFields();
		hideErrorMessage();
	}

	/**
	 * Called when the activity is about to start interacting with the user.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		resetFields();
	}

	private void resetFields() {
		((TextView) findViewById(R.id.isoCodesText)).setText("");
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

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, SEARCH_ID, 0, R.string.search).setShortcut('0', 's');
		menu.add(0, CLEAR_ID, 0, R.string.clear).setShortcut('1', 'c');

		return true;
	}

	/**
	 * Called right before your activity's option menu is displayed.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		menu.findItem(CLEAR_ID)
				.setVisible(
						((TextView) findViewById(R.id.isoCodesText)).getText()
								.length() > 0);

		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SEARCH_ID:
			mSearchListener.onClick(null);
			return true;
		case CLEAR_ID:
			mClearListener.onClick(null);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void hideErrorMessage() {
		findViewById(R.id.errorMessage).setVisibility(View.GONE);
		findViewById(R.id.languateInfoTableLayout).setVisibility(View.VISIBLE);
	}

	private void displayErrorMessage(CharSequence message) {
		TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
		errorMessage.setVisibility(View.VISIBLE);
		errorMessage.setText(message);
		findViewById(R.id.languateInfoTableLayout).setVisibility(View.GONE);
	}

	OnKeyListener mSearchKeyListener = new OnKeyListener() {

		public boolean onKey(View view, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				mSearchListener.onClick(view);
				return true;
			}
			return false;
		}

	};

	OnClickListener mLanguageMapListener = new OnClickListener() {
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

	OnClickListener mSearchListener = new OnClickListener() {
		public void onClick(View v) {

			final AsyncTask<String, Integer, String> extractLanguageTask = new AsyncTask<String, Integer, String>() {
				private ProgressDialog dialog;
				private LanguagePageParser.ParseResults results;

				@Override
				protected void onPreExecute() {
					resetFields();
					hideErrorMessage();

					dialog = new ProgressDialog(LanguagesActivity.this);
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
						boolean isRemote = false;
						if (isRemote) {
							final URL url = new URL(
									"http://www.ethnologue.com/show_language.asp?code="
											+ params[0]);

							urlConnection = (HttpURLConnection) url
									.openConnection();
							is = urlConnection.getInputStream();
							rdr = new BufferedReader(new InputStreamReader(is));
						} else {
							final AssetManager am = getAssets();
							is = am.open("sample.eng.txt");
							rdr = new BufferedReader(new InputStreamReader(is,
									Charset.forName("windows-1252")));
						}

						final LanguagePageParser languagePageParser = new LanguagePageParser();
						results = languagePageParser.parse(rdr);

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

					final LanguagePageParser.ParseResults.CountryInfo ci = results
							.getCountries().get(0);

					if (ci.getLanguageIsoCode() == null) {
						displayErrorMessage(getText(R.string.no_language_info_found));
						return;
					}

					if (!ci.getLanguageIsoCode().equals(
							mEditor.getText().toString())) {
						displayErrorMessage(getText(R.string.no_language_info_found));
						return;
					}

					((TextView) findViewById(R.id.isoCodesText)).setText(ci
							.getLanguageIsoCode());
					((TextView) findViewById(R.id.countryNameText)).setText(ci
							.getCountryNameText());
					((TextView) findViewById(R.id.populationText)).setText(ci
							.getPopulationText());
					((TextView) findViewById(R.id.regionText)).setText(ci
							.getLocationText());
					((TextView) findViewById(R.id.languageMapText)).setText(ci
							.getLanguageMapText());
					((TextView) findViewById(R.id.languageMapText))
							.setTag(getText(R.string.ethnologue_url).toString()
									+ "/" + ci.getLanguageMapURL());
					((TextView) findViewById(R.id.alternateNamesText))
							.setText(ci.getAlternateNamesText());
					((TextView) findViewById(R.id.dialectsText)).setText(ci
							.getDialectsText());
					((TextView) findViewById(R.id.classificationText))
							.setText(ci.getClassificationText());
					((TextView) findViewById(R.id.languageUseText)).setText(ci
							.getLanguageUseText());
					((TextView) findViewById(R.id.languageDevelopmentText))
							.setText(ci.getLanguageDevelopmentText());
					((TextView) findViewById(R.id.writingSystemText))
							.setText(ci.getWritingSystemText());
					((TextView) findViewById(R.id.commentsText)).setText(ci
							.getCommentsText());
				}
			};

			extractLanguageTask.execute(mEditor.getText().toString());
		}
	};

	/**
	 * A call-back for when the user presses the clear button.
	 */
	OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View v) {
			mEditor.setText("");
			resetFields();
		}
	};
}
