package karolis.vycius.kviz.helpers;

import java.util.StringTokenizer;

import karolis.vycius.kviz.database.DBProvider;
import karolis.vycius.kviz.database.MyDatabase;
import karolis.vycius.kviz.widget.AutoFitButton;
import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

public class TextHelper {

	public static int getMaxTextSize(TextView textView, int maxHeight, int maxWidth, int low,
			int high, CharSequence longestWord) {

		if (low >= high)
			return low;

		int avg = (low + high + 1) / 2;

		textView.setTextSize(avg);

		textView.measure(0, 0);

		if (textView.getMeasuredHeight() > maxHeight
				|| !isOneLine(textView, longestWord, maxWidth))
			return getMaxTextSize(textView, maxHeight, maxWidth, low, avg - 1, longestWord);
		else
			return getMaxTextSize(textView, maxHeight, maxWidth, avg, high, longestWord);
	}

	public static boolean isOneLine(TextView textView, CharSequence longestWord, int maxWidth) {
		CharSequence originalText = textView.getText();

		textView.setMaxWidth(maxWidth + 1000);
		textView.setText(longestWord + AutoFitButton.DOUBLE_BYTE_SPACE);

		textView.measure(0, 0);

		boolean oneLine = (maxWidth >= textView.getMeasuredWidth());

		textView.setMaxWidth(maxWidth);
		textView.setText(originalText + AutoFitButton.DOUBLE_BYTE_SPACE);

		return oneLine;
	}

	public static String getInterestingFact(Context context) {
		try {
			String[] columns = { MyDatabase.COLUMN_FACT_TEXT };
			String orderBy = "RANDOM() LIMIT 1";

			Cursor cursor = context.getContentResolver().query(DBProvider.FACT_URI, columns,
					null, null, orderBy);

			cursor.moveToFirst();
			String fact = cursor.getString(cursor
					.getColumnIndexOrThrow(MyDatabase.COLUMN_FACT_TEXT));
			cursor.close();

			return fact;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getLongestWord(String sentence) {
		if (sentence == null || sentence.length() == 0)
			return "";

		final StringTokenizer st = new StringTokenizer(sentence, " \t\n\r\f!;:'\",.?");
		String longestWord = "";
		while (st.hasMoreTokens()) {
			final String currentWord = st.nextToken();

			if (currentWord.length() > longestWord.length())
				longestWord = currentWord;
		}

		return longestWord;
	}

	public static String accusativeEnding(int number, String word) {
		if (number % 10 == 0 || (number >= 11 && number <= 19))
			return word + "ų";
		else if (number % 10 == 1)
			return word + "ų";
		else
			return word + "us";
	}

	public static String nominativeEnding(int number, String word) {
		if (number % 10 == 0 || (number >= 11 && number <= 19))
			return word + "ų";
		else if (number % 10 == 1)
			return word + "as";
		else
			return word + "ai";
	}

	private TextHelper() {

	}

}
