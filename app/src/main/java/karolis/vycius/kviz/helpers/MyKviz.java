package karolis.vycius.kviz.helpers;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MyKviz {

	public static final String API_GID_URL = "http://kviz.lt/api/getGID";
	public static final String API_SAVE_SCORE_URL = "http://kviz.lt/api/saveScore";

	public static final String TAG_USER_TOKEN = "Token";

	public static boolean isLoggedIn(Context context) {
		return (getUserToken(context) != null);
	}

	public static String getUserToken(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(TAG_USER_TOKEN, null);
	}

	public static void setUserToken(Context context, String token) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		Editor editor = prefs.edit();

		editor.putString(TAG_USER_TOKEN, token);
		editor.commit();
	}

	public static void deleteUserToken(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		if (prefs.contains(TAG_USER_TOKEN)) {
			Editor editor = prefs.edit();

			editor.remove(TAG_USER_TOKEN);
			editor.commit();

		}
	}

	public static String getGameId(String GID, int points) {
		Random generator = new Random();

		StringBuffer s = new StringBuffer(GID.length());

		for (int i = 0; i < GID.length(); ++i)
			if (i == 0 || i == 14)
				s.append(generator.nextInt(10));
			else if (i == 11)
				s.append(points / 1000);
			else if (i == 7 || i == 9)
				s.append((points / 100) % 10);
			else if (i == 1 || i == 3)
				s.append((points / 10) % 10);
			else if (i == 12)
				s.append(points % 10);
			else
				s.append(GID.charAt(i));

		return s.toString();
	}

	private MyKviz() {

	}

}
