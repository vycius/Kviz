package karolis.vycius.kviz.database;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "MyDB";
	private static final int DATABASE_VERSION = 1;

	// Tables
	public static final String QUESTIONS_TABLE = "questions";
	public static final String FACTS_TABLE = "facts";

	// Columns
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_QUESTION_TEXT = "question";
	public static final String COLUMN_QUESTION_ANSWER_NR = "answer";
	public static final String COLUMN_QUESTION_ANSWER_1 = "a";
	public static final String COLUMN_QUESTION_ANSWER_2 = "b";
	public static final String COLUMN_QUESTION_ANSWER_3 = "c";
	public static final String COLUMN_QUESTION_ANSWER_4 = "d";
	public static final String COLUMN_QUESTION_LEVEL = "level";

	public static final String COLUMN_FACT_TEXT = "text";

	public static final String COLUMN_QUESTION_DESCRIPTION = "description";

	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// super.onUpgrade(db, oldVersion, newVersion);
	}

	public Cursor query(String table, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		queryBuilder.setTables(table);

		return queryBuilder.query(getReadableDatabase(), projection, selection, selectionArgs,
				null, null, sortOrder);
	}

	public int countQuestions() {
		String sqlTables = "questions";

		int c = (int) DatabaseUtils.longForQuery(getReadableDatabase(),
				"SELECT COUNT(*) FROM " + sqlTables, null);

		return c;
	}

	public Cursor getQuestionByLevel(int level, Integer limit) {
		final String[] sqlSelect = { "id", "question", "answer", "level", "a", "b", "c", "d",
				"description" };
		final String sqlTables = "questions";
		final String where = String.format(Locale.US, "level = %d", level);
		final String order = "RANDOM()";

		final Cursor c = getReadableDatabase().query(sqlTables, sqlSelect, where, null, null,
				null, order, limit.toString());

		c.moveToFirst();

		return c;
	}

}
