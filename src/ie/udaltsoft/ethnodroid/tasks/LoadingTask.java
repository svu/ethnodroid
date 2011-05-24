package ie.udaltsoft.ethnodroid.tasks;

import ie.udaltsoft.ethnodroid.EthnodroidActivity;
import ie.udaltsoft.ethnodroid.R;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class LoadingTask extends AsyncTask<String, Integer, String> {

	protected EthnodroidActivity activity;
	protected ProgressDialog dialog;

	public LoadingTask(EthnodroidActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		activity.hideErrorMessage();

		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getText(R.string.loading));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}

}