package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.tasks.LoadCountryListTask;
import ie.udaltsoft.ethnodroid.tasks.LoadLanguageListTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class EthnodroidActivity extends Activity {

	public final static String RESULTS_EXTRAS = "results";

	static final private int SEARCH_LANGUAGES_ID = Menu.FIRST;
	static final private int SEARCH_COUNTRIES_ID = Menu.FIRST + 1;

	public OnClickListener browseEthnologueClickListener = new OnClickListener() {

		public void onClick(View v) {
			final Intent myIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getText(R.string.ethnologue_url).toString()));
			startActivity(myIntent);
		}
	};

	public EthnodroidActivity() {
		super();
	}

	protected void setBrowserLink() {
		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(browseEthnologueClickListener);
	}

	public void displayErrorMessage(CharSequence message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setNeutralButton(R.string.close,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}

				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void displayErrorMessage(int msgId) {
		this.displayErrorMessage(getText(msgId));
	}

	public boolean isRemoteLoading() {
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, SEARCH_LANGUAGES_ID, 0, R.string.search_languages)
				.setShortcut('1', 'l');
		menu.add(0, SEARCH_COUNTRIES_ID, 0, R.string.search_countries)
				.setShortcut('2', 'c');

		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SEARCH_LANGUAGES_ID:
			new LoadLanguageListTask(EthnodroidActivity.this).execute("all");
			return true;
		case SEARCH_COUNTRIES_ID:
			new LoadCountryListTask(EthnodroidActivity.this).execute("all");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}