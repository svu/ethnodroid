package ie.udaltsoft.ethnodroid;

import android.content.Intent;
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
				.setOnClickListener(mActivateLanguagesListener);
		((Button) findViewById(R.id.activateCountriesButton))
				.setOnClickListener(mActivateCountriesListener);

	}

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private final OnClickListener mActivateLanguagesListener = new OnClickListener() {
		public void onClick(View v) {
			final Intent i = new Intent(MainActivity.this, SearchActivity.class);
			i.putExtra(SearchActivity.SEARCH_TYPE,
					SearchActivity.SearchType.LANGUAGE);
			startActivity(i);
		}
	};

	private final OnClickListener mActivateCountriesListener = new OnClickListener() {
		public void onClick(View v) {
			final Intent i = new Intent(MainActivity.this, SearchActivity.class);
			i.putExtra(SearchActivity.SEARCH_TYPE,
					SearchActivity.SearchType.COUNTRY);
			startActivity(i);
		}
	};

	OnClickListener mBackListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
}
