package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.CountryInfo;
import ie.udaltsoft.ethnodroid.parsers.LanguagePageParser;
import ie.udaltsoft.ethnodroid.parsers.ParseResults;

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

public class LanguagesSearchActivity extends Activity {

	static final private int SEARCH_ID = Menu.FIRST;
	static final private int CLEAR_ID = Menu.FIRST + 1;

	private EditText mEditor;

	private final static boolean isRemote = true;

	public LanguagesSearchActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.language_search_layout);

		// Find the text editor view inside the layout, because we
		// want to do various programmatic things with it.
		mEditor = (EditText) findViewById(R.id.languagePattern);

		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));

		((Button) findViewById(R.id.search))
				.setOnClickListener(mSearchListener);
		((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
		mEditor.setOnKeyListener(mSearchKeyListener);
		mEditor.setText("");
		hideErrorMessage();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, SEARCH_ID, 0, R.string.search).setShortcut('0', 's');
		menu.add(0, CLEAR_ID, 0, R.string.clear).setShortcut('1', 'c');

		return true;
	}

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
	}

	private void displayErrorMessage(CharSequence message) {
		TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
		errorMessage.setVisibility(View.VISIBLE);
		errorMessage.setText(message);
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

	OnClickListener mSearchListener = new OnClickListener() {
		public void onClick(View v) {

			final AsyncTask<String, Integer, String> extractLanguageTask = new AsyncTask<String, Integer, String>() {
				private ProgressDialog dialog;
				private ParseResults results;

				@Override
				protected void onPreExecute() {
					hideErrorMessage();

					dialog = new ProgressDialog(LanguagesSearchActivity.this);
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

					if (results == null || results.getCountries() == null
							|| results.getCountries().size() == 0) {
						displayErrorMessage(getText(R.string.no_language_info_found));
						return;
					}

					final CountryInfo ci = results
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

					final Intent i = new Intent(LanguagesSearchActivity.this,
							LanguagesActivity.class);
					i.putExtra("results", results);
					startActivity(i);
				}
			};

			extractLanguageTask.execute(mEditor.getText().toString());
		}
	};

	OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View v) {
			mEditor.setText("");
		}
	};
}
