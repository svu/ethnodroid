package ie.udaltsoft.ethnodroid;

import android.os.Bundle;
import android.widget.ImageView;

public class LineageActivity extends EthnodroidActivity {

	public LineageActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.language_layout);
		setBrowserLink();

		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));

		resetFields();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void resetFields() {
	}
}
