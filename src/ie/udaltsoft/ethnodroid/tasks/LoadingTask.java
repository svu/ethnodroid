package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.R;
import ie.udaltsoft.ethnodroid.parsers.WebPageParser;
import ie.udaltsoft.ethnodroid.parsers.WebPageParserResults;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;

public abstract class LoadingTask<ResultsType extends WebPageParserResults, ParserType extends WebPageParser<ResultsType>>
		extends AsyncTask<String, Integer, String> {

	protected String queryParameter;
	protected ResultsType results;
	protected EthnodroidActivity activity;

	private ProgressDialog dialog;
	private String queryFormat;
	private String localPrefix;
	private Class<? extends EthnodroidActivity> nextActivityClass;

	public LoadingTask(EthnodroidActivity activity, String queryFormat,
			String localPrefix,
			Class<? extends EthnodroidActivity> nextActivityClass) {
		super();
		this.activity = activity;
		this.queryFormat = queryFormat;
		this.localPrefix = localPrefix;
		this.nextActivityClass = nextActivityClass;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getText(R.string.loading));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		queryParameter = params[0];
		HttpURLConnection urlConnection = null;
		try {
			final InputStream is;
			final BufferedReader rdr;
			if (activity.isRemoteLoading()) {
				final URL url = new URL("http://www.ethnologue.com/"
						+ queryFormat + queryParameter);

				urlConnection = (HttpURLConnection) url.openConnection();
				is = urlConnection.getInputStream();
				rdr = new BufferedReader(new InputStreamReader(is));
			} else {
				final AssetManager am = activity.getAssets();
				is = am.open(localPrefix + "." + queryParameter + ".txt");
				rdr = new BufferedReader(new InputStreamReader(is,
						Charset.forName("windows-1252")));
			}

			final ParserType parser = createParser();
			results = parser.parse(rdr);

		} catch (Exception ex) {
			return ex.toString();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return null;
	}

	protected abstract ParserType createParser();

	protected abstract boolean checkResults();

	@Override
	protected void onPostExecute(String result) {
		dialog.hide();

		if (result != null) {
			activity.displayErrorMessage(result);
			return;
		}

		if (!checkResults()) {
			return;
		}

		final Intent i = new Intent(activity, nextActivityClass);
		i.putExtra(EthnodroidActivity.RESULTS_EXTRAS, results);
		activity.startActivity(i);
	}

}