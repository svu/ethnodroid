package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.LanguageActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.CountryInfo;
import ie.udaltsoft.ethnodroid.parsers.LanguagePageParser;
import ie.udaltsoft.ethnodroid.parsers.LanguageParseResults;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.content.Intent;
import android.content.res.AssetManager;

public final class SearchTask extends LoadingTask {

	private LanguageParseResults results;
	private String languageIsoCode;

	public SearchTask(EthnodroidActivity activity) {
		super(activity);
	}

	@Override
	protected String doInBackground(String... params) {
		languageIsoCode = params[0];
		HttpURLConnection urlConnection = null;
		try {
			final InputStream is;
			final BufferedReader rdr;
			if (activity.isRemoteLoading()) {
				final URL url = new URL(
						"http://www.ethnologue.com/show_language.asp?code="
								+ languageIsoCode);

				urlConnection = (HttpURLConnection) url.openConnection();
				is = urlConnection.getInputStream();
				rdr = new BufferedReader(new InputStreamReader(is));
			} else {
				final AssetManager am = activity.getAssets();
				is = am.open("show_language.eng.txt");
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
			activity.displayErrorMessage(result);
			return;
		}

		if (results == null || results.getCountries() == null
				|| results.getCountries().size() == 0) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return;
		}

		final CountryInfo ci = results.getCountries().get(0);

		if (ci.getLanguageIsoCode() == null) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return;
		}

		if (!ci.getLanguageIsoCode().equals(languageIsoCode)) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return;
		}

		final Intent i = new Intent(activity, LanguageActivity.class);
		i.putExtra(EthnodroidActivity.RESULTS_EXTRAS, results);
		activity.startActivity(i);
	}
}