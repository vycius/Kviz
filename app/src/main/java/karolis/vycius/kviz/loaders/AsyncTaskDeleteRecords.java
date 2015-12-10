package karolis.vycius.kviz.loaders;

import java.util.ArrayList;

import karolis.vycius.kviz.R;
import karolis.vycius.kviz.database.DBProvider;
import karolis.vycius.kviz.database.UsersDB;
import karolis.vycius.kviz.helpers.DialogsHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskDeleteRecords extends AsyncTask<Void, Void, Void> {

	private Context context;
	private ArrayList<Long> ids;
	private ProgressDialog progress;
	private OnRecordsDeleted listener;

	public AsyncTaskDeleteRecords(Context context, ArrayList<Long> ids,
			OnRecordsDeleted listener) {
		Log.d("Labas", "AsyncTaskDeleteRecords");

		this.context = context;
		this.ids = ids;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = DialogsHelper.getLoadingDialog(context, R.string.recordsRecordsErasing);
		progress.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (ids == null || ids.size() == 0)
			return null;

		String where = UsersDB.ID_COLUMN_ORIGINAL + " IN (";
		String[] whereArgs = new String[ids.size()];

		for (int i = 0; i < ids.size(); ++i) {
			if (i != 0)
				where += ",";
			where += "?";
			whereArgs[i] = String.valueOf(ids.get(i));
		}
		where += ")";

		context.getContentResolver().delete(DBProvider.USERS_TABLE_URI, where, whereArgs);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(progress != null && progress.isShowing())
			progress.dismiss();

		if (listener != null)
			listener.onRecordsDeleted();
	}

	public interface OnRecordsDeleted {
		public void onRecordsDeleted();
	}

}
