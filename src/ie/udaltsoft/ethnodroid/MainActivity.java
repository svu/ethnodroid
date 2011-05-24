package ie.udaltsoft.ethnodroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends EthnodroidActivity {

	static final private int BACK_ID = Menu.FIRST;

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

	}

	/**
	 * Called when your activity's options menu needs to be created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, BACK_ID, 0, R.string.back).setShortcut('1', 'b');

		return true;
	}

	/**
	 * Called when a menu item is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BACK_ID:
			mBackListener.onClick(null);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	OnClickListener mActivateLanguagesListener = new OnClickListener() {
		public void onClick(View v) {
			final Intent i = new Intent(MainActivity.this,
					LanguagesSearchActivity.class);
			startActivity(i);
		}
	};

	/**
	 * A call-back for when the user presses the back button.
	 */
	OnClickListener mBackListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
}
