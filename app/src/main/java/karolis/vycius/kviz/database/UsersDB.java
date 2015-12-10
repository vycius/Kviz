package karolis.vycius.kviz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class UsersDB extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "User";
	private static final int DATABASE_VERSION = 1;

	// Tables
	public static final String RECORDS_TABLE = "Records";

	// Columns
	public static final String ID_COLUMN = "_id";
	public static final String ID_COLUMN_ORIGINAL = "ID";
	public static final String ID_COLUMN_WITH_AS = ID_COLUMN_ORIGINAL + " as " + ID_COLUMN;
	public static final String NAME_COLUMN = "Name";
	public static final String POINTS_COLUMN = "Points";
	public static final String TIME_COLUMN = "Time";

	public UsersDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public Cursor query(String table, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		queryBuilder.setTables(table);

		return queryBuilder.query(getReadableDatabase(), projection, selection, selectionArgs,
				null, null, sortOrder);
	}

	public long insert(ContentValues values) {
		return getWritableDatabase().insert(RECORDS_TABLE, null, values);
	}

	public int delete(String where, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();

		int rowsAffected = db.delete(RECORDS_TABLE, where, whereArgs);

		db.close();
		
		return rowsAffected;
	}

	public Cursor getDistinctNames() {
		String[] columns = { UsersDB.NAME_COLUMN };
		String orderBy = UsersDB.NAME_COLUMN + " ASC";

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		queryBuilder.setTables(RECORDS_TABLE);

		return queryBuilder.query(getReadableDatabase(), columns, null, null, NAME_COLUMN,
				null, orderBy);
	}

}
