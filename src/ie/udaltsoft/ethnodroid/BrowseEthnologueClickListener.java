package ie.udaltsoft.ethnodroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

public class BrowseEthnologueClickListener implements OnClickListener {
	private Context context;

	public BrowseEthnologueClickListener(Context ctxt) {
		this.context = ctxt;
	}

	public void onClick(View v) {
		final Intent myIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(context.getText(R.string.ethnologue_url).toString()));
		context.startActivity(myIntent);
	}
}
