package karolis.vycius.kviz.entities;

import java.util.Locale;

import karolis.vycius.kviz.database.UsersDB;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class RecordValue {
	// JSON OBJECT TAGS
	public static final String TAG_USER_NAME = "user";
	public static final String TAG_POINTS = "points";
	public static final String TAG_GRAVATAR = "gravatar";

	private String name, gravatar;
	private int points;
	private long id;

	public RecordValue(final JSONObject object) {
		try {
			this.name = object.getString(TAG_USER_NAME);
			this.points = object.getInt(TAG_POINTS);

			if (object.has(TAG_GRAVATAR))
				this.gravatar = object.getString(TAG_GRAVATAR);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public RecordValue(final Cursor cursor) {
		id = cursor.getLong(cursor.getColumnIndex(UsersDB.ID_COLUMN));
		name = cursor.getString(cursor.getColumnIndex(UsersDB.NAME_COLUMN));
		points = cursor.getInt(cursor.getColumnIndex(UsersDB.POINTS_COLUMN));
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @return the points
	 */
	public String getGravatar(int height) {
		if (height == 0)
			return gravatar;
		return String.format(Locale.US, "%s&s=%d", gravatar, height);
	}
}
