package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.LineageActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.FamilyInfo;
import ie.udaltsoft.ethnodroid.parsers.LineagePageParser;
import ie.udaltsoft.ethnodroid.parsers.LineageParseResults;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.content.Intent;
import android.content.res.AssetManager;

public class ClassificationTask extends LoadingTask {
	private LineageParseResults results;

	public ClassificationTask(EthnodroidActivity activity) {
		super(activity);
	}

	@Override
	protected String doInBackground(String... params) {
		HttpURLConnection urlConnection = null;
		try {
			final InputStream is;
			final BufferedReader rdr;
			if (activity.isRemoteLoading()) {
				final URL url = new URL(
						"http://www.ethnologue.com/show_lang_family.asp?code="
								+ params[0]);

				urlConnection = (HttpURLConnection) url.openConnection();
				is = urlConnection.getInputStream();
				rdr = new BufferedReader(new InputStreamReader(is));
			} else {
				final AssetManager am = activity.getAssets();
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
			activity.displayErrorMessage(result);
			return;
		}

		if (results == null || results.getFamilies() == null
				|| results.getFamilies().size() == 0) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return;
		}

		final FamilyInfo fi = results.getFamilies().get(0);

		if (fi.getCode() == null) {
			activity.displayErrorMessage(R.string.no_language_info_found);
			return;
		}

		final Intent i = new Intent(activity, LineageActivity.class);
		i.putExtra(EthnodroidActivity.RESULTS_EXTRAS, results);
		activity.startActivity(i);
	}
};
