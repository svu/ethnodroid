package ie.udaltsoft.ethnodroid;



import ie.udaltsoft.ethnodroid.tasks.SearchTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

public class LanguagesSearchActivity extends EthnodroidActivity {

	static final private int SEARCH_ID = Menu.FIRST;
	static final private int CLEAR_ID = Menu.FIRST + 1;

	private EditText mEditor;

	public LanguagesSearchActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.language_search_layout);
		setBrowserLink();

		// Find the text editor view inside the layout, because we
		// want to do various programmatic things with it.
		mEditor = (EditText) findViewById(R.id.languagePattern);

		((Button) findViewById(R.id.search))
				.setOnClickListener(mSearchListener);
		((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
		mEditor.setOnKeyListener(mSearchKeyListener);
		mEditor.setText("");
		hideErrorMessage();
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

			final SearchTask extractLanguageTask = new SearchTask(
					LanguagesSearchActivity.this);

			extractLanguageTask.execute(mEditor.getText().toString());
		}
	};

	OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View v) {
			mEditor.setText("");
		}
	};
}
