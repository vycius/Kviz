package karolis.vycius.kviz.game;

import java.util.ArrayList;
import java.util.LinkedList;

import karolis.vycius.kviz.database.DBProvider;
import karolis.vycius.kviz.database.MyDatabase;
import karolis.vycius.kviz.entities.Question;
import karolis.vycius.kviz.game.helps.OnHelpUsed;
import android.app.Activity;
import android.database.Cursor;

public class ClassicGameMode extends BaseGameMode {

	private ArrayList<String> usedQuestionsIds;

	public ClassicGameMode(Activity activity, BaseGameListener listener,
			OnHelpUsed helpListener) {
		super(activity, listener, helpListener);

		usedQuestionsIds = new ArrayList<String>();
	}

	@Override
	protected LinkedList<Question> generateQuestions() {
		String where = MyDatabase.COLUMN_QUESTION_LEVEL + " = ? ";
		String sortOrder = "RANDOM()";
		LinkedList<Question> questions = new LinkedList<Question>();

		for (int i = 1; i <= 6; ++i) {
			Cursor cursor = activity.getContentResolver().query(
					DBProvider.QUESTIONS_TABLE_URI, null, where,
					new String[] { String.valueOf(i) },
					sortOrder + " LIMIT " + String.valueOf(i + 1));

			try {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					Question mQuestion = new Question(cursor);

					questions.add(mQuestion);
					usedQuestionsIds.add(String.valueOf(mQuestion.getId()));
				}

				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return questions;
	}

	@Override
	protected Question changeQuestion() {
		String where = MyDatabase.COLUMN_QUESTION_LEVEL + " = " + getCurrentLevel() + " AND "
				+ MyDatabase.COLUMN_ID + " NOT IN(";

		String sortOrder = "RANDOM() LIMIT 1";

		for (int i = 0; i < usedQuestionsIds.size(); ++i) {
			if (i != 0)
				where += ",";

			where += "?";
		}

		where += ")";

		String[] selectionArgs = usedQuestionsIds.toArray(new String[usedQuestionsIds.size()]);

		Cursor cursor = activity.getContentResolver().query(DBProvider.QUESTIONS_TABLE_URI,
				null, where, selectionArgs, sortOrder);

		cursor.moveToFirst();

		Question question = new Question(cursor);
		usedQuestionsIds.add(String.valueOf(cursor.getInt(cursor
				.getColumnIndex(MyDatabase.COLUMN_ID))));

		cursor.close();

		return question;
	}
}
