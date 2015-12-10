package karolis.vycius.kviz.fragments;

import java.util.ArrayList;

import karolis.vycius.kviz.LoginActivity;
import karolis.vycius.kviz.MainActivity;
import karolis.vycius.kviz.R;
import karolis.vycius.kviz.RegistrationActivity;
import karolis.vycius.kviz.adapters.MyKvizSidebarAdapter;
import karolis.vycius.kviz.adapters.MyKvizSidebarGridAdapter;
import karolis.vycius.kviz.entities.MyKvizSidebarValue;
import karolis.vycius.kviz.helpers.MyKviz;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class MyKvizSidebar extends Fragment implements OnItemClickListener {

	private final String KVIZ_WEBSITE_URL = "http://www.kviz.lt";

	private ArrayList<MyKvizSidebarValue> offlineEntries, onlineEntries, otherEntries;
	private MyKvizSidebarAdapter adapter;
	private MyKvizSidebarGridAdapter gridAdapter;
	private ListView listView;
	private GridView gridView;
	private boolean loggedIn = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.my_kviz_sidebar_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();

		adapter = new MyKvizSidebarAdapter(getActivity());
		gridAdapter = new MyKvizSidebarGridAdapter(getActivity(), otherEntries);

		listView = (ListView) getActivity().findViewById(R.id.myKvizSidebarListView);
		gridView = (GridView) getActivity().findViewById(R.id.myKvizSidebarGridView);

		listView.setAdapter(adapter);
		gridView.setAdapter(gridAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						openKvizWebsite();
						break;
					case 1:
						openKvizGooglePlay();
						break;

					case 2:
						toogle();
						break;
				}
			}
		});

		listView.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshScreen();
	}

	void refreshScreen() {
		loggedIn = MyKviz.isLoggedIn(getActivity());

		if (loggedIn)
			adapter.swap(onlineEntries);
		else
			adapter.swap(offlineEntries);
	}

	private void init() {
		offlineEntries = new ArrayList<MyKvizSidebarValue>();
		onlineEntries = new ArrayList<MyKvizSidebarValue>();

		offlineEntries.add(new MyKvizSidebarValue(R.string.sidebarOLoginButton,
				R.drawable.login));
		offlineEntries.add(new MyKvizSidebarValue(R.string.sidebarORegisterButton,
				R.drawable.signup));

		onlineEntries.add(new MyKvizSidebarValue(R.string.sidebarOdisconnect,
				R.drawable.logout));

		otherEntries = new ArrayList<MyKvizSidebarValue>();

		otherEntries.add(new MyKvizSidebarValue(R.string.sidebarOnlineKvizWebsite,
				R.drawable.kvizlt));
		otherEntries.add(new MyKvizSidebarValue(R.string.sidebarOnlineRateApp,
				R.drawable.reviewus));
		otherEntries.add(new MyKvizSidebarValue(R.string.sidebarOExit, R.drawable.iseiti));

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!loggedIn) {
			switch (position) {
				case 0:
					startActivity(new Intent(getActivity(), LoginActivity.class));
					break;
				case 1:
					startActivity(new Intent(getActivity(), RegistrationActivity.class));
					break;
			}
		} else {
			switch (position) {
				case 0:
					MyKviz.deleteUserToken(getActivity().getBaseContext());
					refreshScreen();
					break;
			}
		}
	}

	void openKvizWebsite() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KVIZ_WEBSITE_URL));
		startActivity(browserIntent);
	}

	void toogle() {
		if (getActivity() instanceof MainActivity) {
			try {
				((MainActivity) getActivity()).toggle();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void openKvizGooglePlay() {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.kvizGooglePlayUrlMarket)));
			startActivity(intent);
		} catch (Exception e1) {
			openKvizWebsite();
		}

	}

}
