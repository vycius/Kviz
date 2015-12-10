package karolis.vycius.kviz;

import java.util.ArrayList;
import java.util.Locale;

import karolis.vycius.kviz.entities.Question;
import karolis.vycius.kviz.fragments.GameSidebar;
import karolis.vycius.kviz.game.BaseGameListener;
import karolis.vycius.kviz.game.ClassicGameMode;
import karolis.vycius.kviz.game.helps.HelpsList;
import karolis.vycius.kviz.game.helps.OnHelpUsed;
import karolis.vycius.kviz.helpers.DialogsHelper;
import karolis.vycius.kviz.widget.AutoFitButton;
import karolis.vycius.kviz.widget.AutoFitTextView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class QuestionsActivity extends SlidingFragmentActivity implements OnClickListener,
		BaseGameListener, OnHelpUsed {

	private ClassicGameMode game;
	private TextView timerTV, questionNumberTV;
	private AutoFitTextView questionButton;
	private GameSidebar gameSidebar;
	private AutoFitButton[] answerButtons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (game != null)
			return;

		setContentView(R.layout.activity_questions);

		setHelpsSlidingMenu();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (game == null) {
			initilize();

			game = new ClassicGameMode(this, this, this);
		}

		if (game != null && gameSidebar != null)
			gameSidebar.populateAdapter(game.getAvailableHelps());
	}

	public void initilize() {
		timerTV = (TextView) findViewById(R.id.clock);
		questionButton = (AutoFitTextView) findViewById(R.id.klausimas);
		questionNumberTV = (TextView) findViewById(R.id.questionOfQuestion);

		final int[] answerButtonIds = new int[] { R.id.answer1, R.id.answer2, R.id.answer3,
				R.id.answer4 };

		answerButtons = new AutoFitButton[answerButtonIds.length];

		for (int i = 0; i < answerButtonIds.length; ++i) {
			answerButtons[i] = (AutoFitButton) findViewById(answerButtonIds[i]);
			answerButtons[i].setOnClickListener(this);
			answerButtons[i].setClickable(false);
		}

		int otherElementsIds[] = { R.id.questionOfQuestion, R.id.takeHelps };

		for (int id : otherElementsIds) {
			findViewById(id).setOnClickListener(this);
		}
	}

	public void setHelpsSlidingMenu() {
		setBehindContentView(R.layout.fragment_container);
		SlidingMenu slidingMenu = getSlidingMenu();

		slidingMenu.setSlidingEnabled(true);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		gameSidebar = new GameSidebar();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, gameSidebar).commit();

		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindScrollScale(0.35f);
		slidingMenu.setFadeDegree(0.35f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.answer1:
				game.submitAnswer(0);
				break;
			case R.id.answer2:
				game.submitAnswer(1);
				break;
			case R.id.answer3:
				game.submitAnswer(2);
				break;
			case R.id.answer4:
				game.submitAnswer(3);
				break;
			case R.id.questionOfQuestion:
				showInfoDialog();
				break;
			case R.id.takeHelps:
				toggle();
				break;
		}

	}

	@Override
	public void onTimerTick(long min, long s) {
		timerTV.setText(String.format(Locale.US, "%d:%02d", min, s));
	}

	@Override
	public void onQuestionChanged(Question question, int points, int questionNR) {

		questionButton.setText(Html.fromHtml(question.getQuestionText()));

		for (int i = 0; i < answerButtons.length; ++i) {
			answerButtons[i].setText(Html.fromHtml(question.getAnswerText(i)));

			if (question.countAvailable() == 1) {
				if (question.isAnswerGood(i)) {
					answerButtons[i].setClickable(true);
					answerButtons[i].setBackgroundResource(R.drawable.mainbuttongreen);
				} else {
					answerButtons[i].setClickable(false);
					answerButtons[i].setBackgroundResource(R.drawable.mainbuttonred);
				}
			} else {
				if (question.isAvailable(i)) {
					answerButtons[i].setClickable(true);
					answerButtons[i].setBackgroundResource(R.drawable.mainbuttonyellow);
				} else {
					answerButtons[i].setClickable(false);
					answerButtons[i].setBackgroundResource(R.drawable.mainbuttonred);
				}
			}
		}

		questionNumberTV.setText(String.format(Locale.US, "%d", questionNR));
	}

	public void showInfoDialog() {
		try {
			DialogsHelper.showGameInfoDialog(this, game.getPoints(), game.getCurrentLevel(),
					game.getQuestionNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onGameLost(Bundle bundle) {
		Intent intent = new Intent(QuestionsActivity.this, GameFinished.class);
		intent.putExtras(bundle);

		startActivity(intent);
		overridePendingTransition(R.animator.fadein, R.animator.fadeout);

		finish();
	}

	public void useHelp(HelpsList help) {
		game.useHelp(help);
	}

	@Override
	public void onHelpUsed(HelpsList help, ArrayList<HelpsList> availableHelps) {
		gameSidebar.populateAdapter(availableHelps);
	}

	@Override
	protected void onDestroy() {
		if (game != null)
			game.destroy();
		super.onDestroy();
	}

}