package ie.udaltsoft.ethnodroid;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EthnodroidActivity extends Activity {

	public final static String RESULTS_EXTRAS = "results";

	public EthnodroidActivity() {
		super();
	}

	protected void setBrowserLink() {
		((ImageView) findViewById(R.id.topImage))
				.setOnClickListener(new BrowseEthnologueClickListener(this));
	}

	public void hideErrorMessage() {
		findViewById(R.id.errorMessage).setVisibility(View.GONE);
	}

	public void displayErrorMessage(CharSequence message) {
		TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
		errorMessage.setVisibility(View.VISIBLE);
		errorMessage.setText(message);
	}

	public void displayErrorMessage(int msgId) {
		this.displayErrorMessage(getText(msgId));
	}

	public boolean isRemoteLoading() {
		return true;
	}

}