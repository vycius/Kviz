package karolis.vycius.kviz.game;

import java.util.ArrayList;
import java.util.LinkedList;

import karolis.vycius.kviz.GameFinished;
import karolis.vycius.kviz.entities.Question;
import karolis.vycius.kviz.game.helps.Helps;
import karolis.vycius.kviz.game.helps.HelpsList;
import karolis.vycius.kviz.game.helps.OnHelpUsed;
import karolis.vycius.kviz.helpers.DialogsHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;

public abstract class BaseGameMode implements OnHelpUsed {
	protected Activity activity;
	protected BaseGameListener gameListener;
	protected OnHelpUsed helpUsedListener = null;
	protected LinkedList<Question> questions;
	protected final float startingCoef = 10f;
	protected float coef = startingCoef;

	protected CountDownTimer timer;
	protected long timeLeft;
	protected int points = 0, questionsAnswered = 0;
	protected Helps helps;
	protected boolean gameFinished = false;

	private final long tickInterval = 1000l;

	public BaseGameMode(Activity activity, BaseGameListener listener, OnHelpUsed helpListener) {
		this.activity = activity;
		this.gameListener = listener;
		this.helpUsedListener = helpListener;

		initilize();

		new LoadQuestionsToGame().execute();
	}

	protected abstract LinkedList<Question> generateQuestions();

	protected long getGameLength() {
		return 600000l;
	}

	protected Question getCurrentQuestion() {
		if (!isNoMoreQuestions())
			return questions.getFirst();

		return null;
	}

	public int getPoints() {
		return points;
	}

	public int getQuestionNumber() {
		return questionsAnswered + 1;
	}

	public int getCurrentLevel() {
		return getCurrentQuestion().getLevel();
	}

	protected boolean isHelpsAvailable() {
		return true;
	}

	private final void initilize() {
		points = 0;
		timeLeft = 0l;

		if (isHelpsAvailable())
			helps = new Helps(activity, this);

		// Set timer
		if (getGameLength() > 0) {
			timer = new CountDownTimer(getGameLength(), tickInterval) {

				@Override
				public void onTick(long millisUntilFinished) {
					timeLeft = millisUntilFinished / 1000l;
					gameListener.onTimerTick(timeLeft / 60l, timeLeft % 60l);
				}

				@Override
				public void onFinish() {
					finishGame();
				}
			};
		}
		// ----------------

	}

	public ArrayList<HelpsList> getAvailableHelps() {
		return helps.getAvailableHelps();
	}

	protected boolean isNoMoreQuestions() {
		return questions.isEmpty();
	}

	protected void deleteCurrentQuestion() {
		if (!isNoMoreQuestions())
			questions.removeFirst();
	}

	protected void finishGame() {
		if (gameFinished)
			return;

		destroy();

		gameListener.onGameLost(getBundleWithInfo());
	}

	public Bundle getBundleWithInfo() {
		Bundle bundle = new Bundle();

		Question mCurQuestion = getCurrentQuestion();
		bundle.putSerializable(GameFinished.TAG_LAST_QUESTION, mCurQuestion);
		bundle.putInt(GameFinished.TAG_LEVEL,
				((mCurQuestion != null) ? mCurQuestion.getLevel() : 6));

		bundle.putInt(GameFinished.TAG_POINTS, points);
		bundle.putLong(GameFinished.TAG_TIME, getGameLength() / 1000l - timeLeft);
		bundle.putInt(GameFinished.TAH_KIQ, Math.round(0.2f * points));
		bundle.putInt(GameFinished.TAG_QUESTIONS_ANSWERED, questionsAnswered);

		return bundle;
	}

	protected void refreshGameScreen() {
		if (!gameFinished)
			gameListener.onQuestionChanged(getCurrentQuestion(), points, getQuestionNumber());
	}

	protected void addPoints(Question question) {
		if (gameFinished)
			return;

		points += Math.round(coef * question.getLevel());
		coef = startingCoef;
	}

	@Override
	public void onHelpUsed(HelpsList help, ArrayList<HelpsList> availableHelps) {
		switch (help) {
			case FIFTY_FIFTY:
				coef *= 0.5f;
				getCurrentQuestion().fiftyFiftyHelp();
				break;
			case RIGHT_WAY:
				coef *= 0.25f;
				getCurrentQuestion().rightWayHelp();
				break;
			case VETO:
				coef *= 0.75f;
				Question question = changeQuestion();
				deleteCurrentQuestion();
				questions.add(question);
				break;
			default:
				break;

		}

		if (helpUsedListener != null)
			helpUsedListener.onHelpUsed(help, availableHelps);

		refreshGameScreen();
	}

	protected abstract Question changeQuestion();

	public void useHelp(HelpsList help) {
		if (isHelpsAvailable() && helps != null && isHelpAvailable(help)) {
			helps.useHelp(help);
		}

	}

	public void submitAnswer(int answerNumber) {

		if (getCurrentQuestion().isAnswerGood(answerNumber)) {
			addPoints(getCurrentQuestion());

			deleteCurrentQuestion();

			++questionsAnswered;

			if (isNoMoreQuestions()) {
				finishGame();
				return;
			}

			refreshGameScreen();
		} else {
			finishGame();
		}
	}

	public void destroy() {
		gameFinished = true;

		if (timer != null)
			timer.cancel();
	}

	public boolean isHelpAvailable(HelpsList help) {
		int availableAnswers = getCurrentQuestion().countAvailable();

		if (availableAnswers == 1)
			return false;

		return true;
	}

	private class LoadQuestionsToGame extends AsyncTask<Void, Void, LinkedList<Question>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = DialogsHelper.getInterestingFactLoadingDialog(activity);
			progressDialog.show();
		}

		@Override
		protected LinkedList<Question> doInBackground(Void... params) {
			return generateQuestions();
		}

		@Override
		protected void onPostExecute(LinkedList<Question> result) {
			super.onPostExecute(result);
			questions = result;

			refreshGameScreen();

			if (timer != null)
				timer.start();

			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
		}

	}
}
