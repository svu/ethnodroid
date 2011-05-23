package ie.udaltsoft.ethnodroid;

import android.app.Activity;
import android.widget.ImageView;

public class EthnodroidActivity extends Activity {

	protected final static boolean isRemote = true;

	public EthnodroidActivity() {
		super();
	}

	protected void setBrowserLink() {
		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));
	}

}