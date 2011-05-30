package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.LanguageActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.LanguagePageParser;
import ie.udaltsoft.ethnodroid.parsers.data.LanguageParseResults;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.content.res.AssetManager;

public final class ExtractLanguageTask extends
		LoadingTask<LanguageParseResults, LanguagePageParser> {

	private LanguagePageParser languagePageParser;

	public ExtractLanguageTask(EthnodroidActivity activity) {
		super(activity, "show_language.asp?code=", "show_language",
				LanguageActivity.class);
	}

	@Override
	protected String doInBackground(String... params) {
		final String errMsg = super.doInBackground(params);

		if (errMsg != null)
			return null;

		// extra ISO639-1
		HttpURLConnection urlConnection = null;
		try {
			final InputStream is;
			final BufferedReader rdr;
			if (activity.isRemoteLoading()) {
				final URL url = new URL(
						"http://www.sil.org/iso639-3/documentation.asp?id="
								+ queryParameter);

				urlConnection = (HttpURLConnection) url.openConnection();
				is = urlConnection.getInputStream();
				rdr = new BufferedReader(new InputStreamReader(is));
			} else {
				final AssetManager am = activity.getAssets();
				is = am.open("iso639." + queryParameter + ".txt");
				rdr = new BufferedReader(new InputStreamReader(is,
						Charset.forName("windows-1252")));
			}

			languagePageParser.parseExtra(rdr, results);

		} catch (Exception ex) {
			return ex.toString();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return null;
	}

	@Override
	protected LanguagePageParser createParser() {
		return languagePageParser = new LanguagePageParser();
	}

	@Override
	protected boolean checkResults() {
		if (results == null || results.getCountries() == null
				|| results.getCountries().size() == 0) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}

		final LanguageParseResults.CountryInfo ci = results.getCountries().get(
				0);

		if (ci.getLanguageIsoCode() == null) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}

		if (!ci.getLanguageIsoCode().equals(queryParameter)) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return false;
		}
		return true;
	}

}