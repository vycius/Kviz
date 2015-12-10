package karolis.vycius.kviz.fragments;

import karolis.vycius.kviz.GameFinished;
import karolis.vycius.kviz.R;
import karolis.vycius.kviz.helpers.TextHelper;
import karolis.vycius.kviz.loaders.AsyncRandomFact;
import karolis.vycius.kviz.loaders.AsyncRandomFact.OnFactLoaded;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameStatistics extends Fragment implements OnFactLoaded {

	private AsyncRandomFact randomFactLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.game_finished_statistics, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = getArguments();

		if (args != null) {
			TextView pointsTV = (TextView) getActivity().findViewById(
					R.id.gameStatisticsPoints);
			TextView levelTV = (TextView) getActivity().findViewById(R.id.gameStatisticsLevel);
			TextView questionNrTV = (TextView) getActivity().findViewById(
					R.id.gameStatisticQuestionNumber);
			TextView KIQTV = (TextView) getActivity().findViewById(R.id.gameStatisticKIQ);

			TextView pointsTextTV = (TextView) getActivity().findViewById(
					R.id.gameStatisticsPointsText);
			TextView questionNrTVText = (TextView) getActivity().findViewById(
					R.id.game_finished_question_tv_text);

			int points = args.getInt(GameFinished.TAG_POINTS);
			int kiq = Math.round(0.2f * points);
			int questionsAnswered = args.getInt(GameFinished.TAG_QUESTIONS_ANSWERED);

			pointsTV.setText(String.valueOf(points));
			KIQTV.setText(String.valueOf(kiq));
			levelTV.setText(String.valueOf(args.getInt(GameFinished.TAG_LEVEL)));
			pointsTextTV.setText(TextHelper.nominativeEnding(points,
					getString(R.string.pointsWordNotFull)));

			questionNrTV.setText(String.valueOf(questionsAnswered));

			questionNrTVText.setText(TextHelper.nominativeEnding(questionsAnswered, "klausim"));
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		randomFactLoader = new AsyncRandomFact(getActivity(), this);
		randomFactLoader.loadFact();
	}

	@Override
	public void onFactLoaded(String fact) {
		TextView factTV = (TextView) getActivity().findViewById(R.id.gameStatisticsFactTV);
		factTV.setText(fact);
	}

}
