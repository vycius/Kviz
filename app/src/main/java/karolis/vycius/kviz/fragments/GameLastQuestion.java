package karolis.vycius.kviz.fragments;

import karolis.vycius.kviz.GameFinished;
import karolis.vycius.kviz.R;
import karolis.vycius.kviz.entities.Question;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameLastQuestion extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.game_finished_last_question, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();

		Question question = (Question) bundle.getSerializable(GameFinished.TAG_LAST_QUESTION);

		if (question != null) {
			TextView questionTextTV = (TextView) getActivity().findViewById(
					R.id.gameFinishedLastQuestionText);
			TextView descriptionTV = (TextView) getActivity().findViewById(
					R.id.gameFinishedLastQuestionDescription);

			questionTextTV.setText(Html.fromHtml(question.getQuestionText()));

			if (question.getDescription() != null && question.getDescription().length() > 0)
				descriptionTV.setText(Html.fromHtml(question.getDescription()));
			else
				descriptionTV.setText(R.string.gameFinishedLastQuestionNoDesc);

			int[] ids = new int[] { R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4 };

			for (int i = 0; i < 4; ++i) {
				TextView answerTV = (TextView) getActivity().findViewById(ids[i]);
				answerTV.setText(Html.fromHtml(question.getAnswerText(i)));

				if (question.isAnswerGood(i)) {
					answerTV.setBackgroundResource(R.drawable.mainbuttongreen);
				} else if (isScreenSmall())
					answerTV.setVisibility(View.GONE);

			}
		}
	}

	private boolean isScreenSmall() {
		return getResources().getDisplayMetrics().heightPixels <= 480;
	}
}
