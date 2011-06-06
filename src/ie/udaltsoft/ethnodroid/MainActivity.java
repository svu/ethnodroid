package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.tasks.LoadCountryListTask;
import ie.udaltsoft.ethnodroid.tasks.LoadLanguageListTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends EthnodroidActivity {

	public MainActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.main_layout);
		setBrowserLink();

		((Button) findViewById(R.id.activateLanguagesButton))
				.setOnClickListener(activateLanguagesListener);
		((Button) findViewById(R.id.activateCountriesButton))
				.setOnClickListener(activateCountriesListener);

	}

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private final OnClickListener activateLanguagesListener = new OnClickListener() {
		public void onClick(View v) {
			new LoadLanguageListTask(MainActivity.this).execute("all");
		}
	};

	private final OnClickListener activateCountriesListener = new OnClickListener() {
		public void onClick(View v) {
			new LoadCountryListTask(MainActivity.this).execute("all");
		}
	};
}
