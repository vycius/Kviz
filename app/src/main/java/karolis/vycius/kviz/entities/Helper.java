package karolis.vycius.kviz.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class Helper {

	public static SpannableStringBuilder getErrorText(String text) {
		int ecolor = Color.RED;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(text);
		ssbuilder.setSpan(fgcspan, 0, text.length(), 0);

		return ssbuilder;
	}

	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static String formatDate(long timestamp) {
		final Calendar calendar = Calendar.getInstance();

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		calendar.setTimeInMillis(timestamp * 1000l);

		return sdf.format(calendar.getTime());
	}

	public static class TimeStampHelper {
		public static long getCurrentTimeStamp() {
			Calendar calendar = getCalendarInstance(false);
			return calendar.getTimeInMillis();
		}

		public static long getYesterdayStart() {
			Calendar cal = getCalendarInstance(true);
			cal.add(Calendar.DATE, -1);

			return cal.getTimeInMillis();
		}

		public static long getYesterdayFinish() {
			return getTodayStart() - 1000l;
		}

		public static long getMonthStart() {
			Calendar calendar = getCalendarInstance(true);
			calendar.set(Calendar.DAY_OF_MONTH, 1);

			return calendar.getTimeInMillis();
		}

		public static long getWeekStart() {
			Calendar calendar = getCalendarInstance(true);
			
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				calendar.add(Calendar.DAY_OF_WEEK, -6);
			else
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			return calendar.getTimeInMillis();
		}

		public static long getTodayStart() {
			return getCalendarInstance(true).getTimeInMillis();
		}

		public static Calendar nullHoursMinSecs(Calendar calendar) {
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			return calendar;
		}

		public static Calendar getCalendarInstance(boolean setNulls) {
			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);

			if (setNulls)
				return nullHoursMinSecs(cal);

			return cal;

		}
	}

	private Helper() {

	}
}
