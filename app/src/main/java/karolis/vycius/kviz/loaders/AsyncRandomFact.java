package karolis.vycius.kviz.loaders;

import karolis.vycius.kviz.database.DBProvider;
import karolis.vycius.kviz.database.MyDatabase;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;

public class AsyncRandomFact extends AsyncQueryHandler {

	private OnFactLoaded listener;
	private boolean loading = false;

	public AsyncRandomFact(Context context, OnFactLoaded listener) {
		super(context.getContentResolver());

		this.listener = listener;
	}

	public void loadFact() {
		if (isLoading())
			return;

		loading = true;

		String[] columns = { MyDatabase.COLUMN_FACT_TEXT };
		String orderBy = "RANDOM() LIMIT 1";

		startQuery(0, null, DBProvider.FACT_URI, columns, null, null, orderBy);
	}

	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		super.onQueryComplete(token, cookie, cursor);
		String fact = null;

		try {
			cursor.moveToFirst();
			fact = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_FACT_TEXT));
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (fact != null && listener != null)
			listener.onFactLoaded(fact);

		loading = false;
	}

	public boolean isLoading() {
		return loading;
	}

	public interface OnFactLoaded {
		public void onFactLoaded(String fact);
	}

}
