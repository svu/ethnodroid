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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class LanguagesActivity extends Activity {

	public LanguagesActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.language_layout);

		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));

		final TextView languageMapText = (TextView) findViewById(R.id.languageMapText);
		languageMapText.setOnClickListener(mLanguageMapListener);

		resetFields();

		final LanguagePageParser.ParseResults results = (LanguagePageParser.ParseResults) getIntent()
				.getSerializableExtra("results");
		if (results != null) {
			displayCountry(results.getCountries().get(0));
		}
	}

	/**
	 * Called when the activity is about to start interacting with the user.
	 */
	@Override
	protected void onResume() {
		super.onResume();
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

	public void displayCountry(LanguagePageParser.ParseResults.CountryInfo ci) {
		((TextView) findViewById(R.id.isoCodesText)).setText(ci
				.getLanguageIsoCode());
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
