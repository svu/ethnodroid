package ie.udaltsoft.ethnodroid;

import ie.udaltsoft.ethnodroid.parsers.GroupedCodes;
import ie.udaltsoft.ethnodroid.tasks.ExtractCountryTask;
import ie.udaltsoft.ethnodroid.tasks.ExtractLanguageTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends EthnodroidActivity {

	public static final String SEARCH_TYPE = "searchType";

	private EditText mEditor;

	public enum SearchType implements Serializable {
		LANGUAGE, COUNTRY
	};

	private SearchType searchType;

	private OnItemSelectedListener primaryListListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			final Spinner searchList2 = (Spinner) findViewById(R.id.searchList2);

			final String region = (String) parent.getItemAtPosition(pos);
			ArrayList<GroupedCodes.CodeRef> countryRefs = groupedCodes
					.getGroups().get(region);
			final ArrayAdapter<GroupedCodes.CodeRef> lcla = new ArrayAdapter<GroupedCodes.CodeRef>(
					SearchActivity.this,
					android.R.layout.simple_spinner_dropdown_item, countryRefs);
			searchList2.setAdapter(lcla);
			searchList2.setSelection(-1);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			mClearListener.onClick(arg0);
		}
	};

	private GroupedCodes groupedCodes;

	private OnItemSelectedListener secondaryListListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			final GroupedCodes.CodeRef code = (GroupedCodes.CodeRef) parent
					.getItemAtPosition(pos);
			mEditor.setText(code.getCode());
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			mClearListener.onClick(arg0);
		}
	};

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

		final Spinner searchList1 = (Spinner) findViewById(R.id.searchList1);
		final Spinner searchList2 = (Spinner) findViewById(R.id.searchList2);
		groupedCodes = (GroupedCodes) getIntent().getSerializableExtra(
				RESULTS_EXTRAS);
		final String[] regions = groupedCodes.getGroups().keySet()
				.toArray(new String[0]);
		Arrays.sort(regions);

		final ArrayAdapter<String> lcla = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, regions);
		searchList1.setAdapter(lcla);
		searchList1.setSelection(-1);
		searchList1.setOnItemSelectedListener(primaryListListener);
		searchList2.setOnItemSelectedListener(secondaryListListener);
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
