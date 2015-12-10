package karolis.vycius.kviz.loaders;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

public class OfflineRecordsQueryHandler extends AsyncQueryHandler {

	private OnRecordsLoaded listener;
	
	public OfflineRecordsQueryHandler(ContentResolver cr, OnRecordsLoaded listener) {
		super(cr);
		this.listener = listener;
	}

	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		super.onQueryComplete(token, cookie, cursor);

		if(listener != null)
			listener.onQueryComplete(cursor);

		if(cursor != null && !cursor.isClosed())
			cursor.close();
	}

	public interface OnRecordsLoaded {
		public void onQueryComplete(Cursor cursor);
	}
}
