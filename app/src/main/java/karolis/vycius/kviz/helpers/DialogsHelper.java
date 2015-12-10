package karolis.vycius.kviz.helpers;

import java.util.ArrayList;

import karolis.vycius.kviz.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class DialogsHelper {

	public static ProgressDialog getInterestingFactLoadingDialog(Activity activity) {
		String fact = TextHelper.getInterestingFact(activity);

		return getLoadingDialog(activity, activity.getString(R.string.kraunama), fact);
	}

	public static ProgressDialog getLoadingDialog(Context context, String title) {
		return getLoadingDialog(context, title, null);
	}

	public static ProgressDialog getLoadingDialog(Context context, int title) {
		return getLoadingDialog(context, context.getString(title), null);
	}

	public static ProgressDialog getLoadingDialog(Context context, int title, int message) {
		return getLoadingDialog(context, context.getString(title), context.getString(message));
	}

	public static ProgressDialog getLoadingDialog(Context context, String title, String message) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(title);
		dialog.setMessage(message);

		return dialog;
	}

	public static void showSaveRecordDialog(Context context, int points,
			ArrayList<String> distinctNames, DialogInterface.OnClickListener listener,
			String defaultName, AutoCompleteTextView autoCompleteTV, View view) {

		try {
			if (defaultName == null)
				defaultName = "";

			String pointsText = TextHelper.accusativeEnding(points,
					context.getString(R.string.gameFinishedPointsNotFullWord));

			String title = context.getString(R.string.gameFinishedDialogText, points,
					pointsText);

			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			builder.setTitle(title);
			builder.setCancelable(true);

			builder.setPositiveButton(R.string.gameFinishedSaveDialogSave, listener);
			builder.setNegativeButton(R.string.gameFinishedSaveDialogNotSave, null);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_dropdown_item_1line, distinctNames);

			autoCompleteTV.append(defaultName);
			autoCompleteTV.setAdapter(adapter);

			builder.setView(view);

			AlertDialog alertDialog = builder.create();

			alertDialog.setCanceledOnTouchOutside(false);

			alertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showGameInfoDialog(Context context, int points, int level, int qNr) {
		try {
			String pointsText = TextHelper.accusativeEnding(points,
					context.getString(R.string.pointsWordNotFull));

			String text = context.getString(R.string.gameInfoDialogMessage, points, level,
					qNr, pointsText);

			AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(
					context, R.style.Theme_Riestainis));

			builder.setTitle(R.string.gameInfoDialogTitle);

			builder.setMessage(text);
			builder.setNeutralButton(R.string.gameInfoDialogButtonText, null);

			AlertDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);

			dialog.show();

			TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
			messageView.setGravity(Gravity.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DialogsHelper() {

	}
}
