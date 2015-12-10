package karolis.vycius.kviz.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import karolis.vycius.kviz.database.MyDatabase;
import android.database.Cursor;

public class Question implements Serializable {
	private static final long serialVersionUID = 4417650799076197661L;

	private int id, answerNr, level;

	private String questionText, description;
	private final ArrayList<String> answers;
	private boolean[] available;

	public Question() {
		answers = new ArrayList<String>();

		available = new boolean[4];

		for (int i = 0; i < available.length; ++i)
			available[i] = true;
	}

	public Question(Cursor cursor) {
		this();

		id = cursor.getInt(cursor.getColumnIndex(MyDatabase.COLUMN_ID));
		questionText = cursor
				.getString(cursor.getColumnIndex(MyDatabase.COLUMN_QUESTION_TEXT));

		answerNr = cursor.getInt(cursor.getColumnIndex(MyDatabase.COLUMN_QUESTION_ANSWER_NR));

		description = cursor.getString(cursor
				.getColumnIndex(MyDatabase.COLUMN_QUESTION_DESCRIPTION));

		level = cursor.getInt(cursor.getColumnIndex(MyDatabase.COLUMN_QUESTION_LEVEL));

		for (char j = 'a'; j <= 'd'; ++j)
			answers.add(cursor.getString(cursor.getColumnIndex(String.valueOf(j))));
		this.shuffle();
	}

	private void shuffle() {
		String goodAnswer = answers.get(answerNr - 1);
		Collections.shuffle(answers);

		answerNr = answers.indexOf(goodAnswer);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public String getAnswerText(int pos) {
		if (pos >= 0 && pos < answers.size())
			return answers.get(pos);

		return null;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	public boolean isAnswerGood(int answerNr) {
		return (this.answerNr == answerNr);
	}

	public boolean isAvailable(int pos) {
		if (pos >= 0 && pos < available.length)
			return available[pos];

		return true;
	}

	public int countAvailable() {
		int c = 0;

		for (boolean b : available)
			if (b)
				++c;
		return c;
	}

	public void fiftyFiftyHelp() {
		LinkedList<Integer> list = new LinkedList<Integer>();

		for (int i = 0; i < answers.size(); ++i)
			if (!isAnswerGood(i))
				list.add(i);

		Collections.shuffle(list);

		for (int i = 0; i < 2 && i < list.size(); ++i)
			available[list.get(i)] = false;
	}

	public void rightWayHelp() {
		for (int i = 0; i < answers.size() && i < available.length; ++i)
			available[i] = isAnswerGood(i);
	}

}
