package karolis.vycius.kviz.fragments;

import java.util.ArrayList;

import karolis.vycius.kviz.QuestionsActivity;
import karolis.vycius.kviz.R;
import karolis.vycius.kviz.adapters.GameHelpsAdapter;
import karolis.vycius.kviz.game.helps.HelpsList;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GameSidebar extends Fragment implements OnItemClickListener {
	private QuestionsActivity questionActivity = null;
	private GameHelpsAdapter adapter;
	private ListView listView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof QuestionsActivity)
			questionActivity = (QuestionsActivity) activity;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.helps_sidebar, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new GameHelpsAdapter(getActivity());

		listView = (ListView) getActivity().findViewById(R.id.helpsListView);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void populateAdapter(ArrayList<HelpsList> helps) {
		if (questionActivity != null)
			questionActivity.toggle();

		if (adapter != null)
			adapter.replace(helps);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view.getTag() instanceof HelpsList && questionActivity != null) {
			questionActivity.useHelp((HelpsList) view.getTag());
		}

	}
}
