package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.tasks.ExtractCountryTask;
import ie.udaltsoft.ethnodroid.tasks.ExtractLanguageTask;

import java.io.Serializable;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends EthnodroidActivity {

	public static final String SEARCH_TYPE = "searchType";

	private EditText mEditor;

	public enum SearchType implements Serializable {
		LANGUAGE, COUNTRY
	};

	private SearchType searchType;

	public SearchActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.search_layout);
		setBrowserLink();

		searchType = (SearchType) getIntent().getSerializableExtra(SEARCH_TYPE);

		// Find the text editor view inside the layout, because we
		// want to do various programmatic things with it.
		mEditor = (EditText) findViewById(R.id.searchPattern);

		((Button) findViewById(R.id.search))
				.setOnClickListener(mSearchListener);
		((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
		mEditor.setOnKeyListener(mSearchKeyListener);
		mEditor.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private final OnKeyListener mSearchKeyListener = new OnKeyListener() {
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				mSearchListener.onClick(view);
				return true;
			}
			return false;
		}
	};

	private final OnClickListener mSearchListener = new OnClickListener() {
		public void onClick(View v) {

			final String searchString = (mEditor.getText().toString());
			switch (searchType) {
			case LANGUAGE:
				new ExtractLanguageTask(SearchActivity.this)
						.execute(searchString);
				break;
			case COUNTRY:
				new ExtractCountryTask(SearchActivity.this)
						.execute(searchString);
				break;
			}
		}
	};

	private final OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View v) {
			mEditor.setText("");
		}
	};
}