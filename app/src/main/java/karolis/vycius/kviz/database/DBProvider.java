package karolis.vycius.kviz.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DBProvider extends ContentProvider {

	private MyDatabase questionsDB;
	private UsersDB userDB;

	private static final String AUTHORITY = "karolis.vycius.kviz.database.DBProvider";
	private static final String AUTHORITY_URI_STRING = "content://" + AUTHORITY + "/";

	public static final Uri QUESTIONS_TABLE_URI = Uri.parse(AUTHORITY_URI_STRING
			+ MyDatabase.QUESTIONS_TABLE);
	public static final Uri USERS_TABLE_URI = Uri.parse(AUTHORITY_URI_STRING
			+ UsersDB.RECORDS_TABLE);
	public static final Uri FACT_URI = Uri
			.parse(AUTHORITY_URI_STRING + MyDatabase.FACTS_TABLE);
	public static final Uri DISTINCT_NAMES_URI = Uri.parse(AUTHORITY_URI_STRING
			+ "DISTINCTNAMES");

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		URI_MATCHER.addURI(AUTHORITY, MyDatabase.QUESTIONS_TABLE, 0);
		URI_MATCHER.addURI(AUTHORITY, UsersDB.RECORDS_TABLE, 1);
		URI_MATCHER.addURI(AUTHORITY, MyDatabase.FACTS_TABLE, 2);
		URI_MATCHER.addURI(AUTHORITY, "DISTINCTNAMES", 3);

	}

	@Override
	public boolean onCreate() {
		questionsDB = new MyDatabase(getContext());
		userDB = new UsersDB(getContext());

		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (URI_MATCHER.match(uri)) {
			case 1:
				return userDB.delete(selection, selectionArgs);
		}

		return -1;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (URI_MATCHER.match(uri)) {
			case 1:
				userDB.insert(values);
				break;
		}
		return null;
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (URI_MATCHER.match(uri)) {
			case 0:
				return questionsDB.query(MyDatabase.QUESTIONS_TABLE, projection, selection,
						selectionArgs, sortOrder);
			case 1:
				return userDB.query(UsersDB.RECORDS_TABLE, projection, selection,
						selectionArgs, sortOrder);
			case 2:
				return questionsDB.query(MyDatabase.FACTS_TABLE, projection, selection,
						selectionArgs, sortOrder);
			case 3:
				return userDB.getDistinctNames();

		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
