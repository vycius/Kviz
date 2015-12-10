package karolis.vycius.kviz.fragments;

import java.util.HashMap;
import java.util.Map;

import karolis.vycius.kviz.LoginActivity;
import karolis.vycius.kviz.QuestionsActivity;
import karolis.vycius.kviz.R;
import karolis.vycius.kviz.RecordsActivity;
import karolis.vycius.kviz.RegistrationActivity;
import karolis.vycius.kviz.adapters.OnlineRecordsAdapter;
import karolis.vycius.kviz.adapters.RecordBaseAdapter;
import karolis.vycius.kviz.entities.Helper;
import karolis.vycius.kviz.helpers.MyKviz;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class OnlineRecords extends SherlockListFragment implements OnNavigationListener {

	private final static String RECORDS_API_URL = "http://kviz.lt/riestainis/scores";
	private int currentSpinnerSelection = 2;
	private AQuery aQuery;
	private RecordBaseAdapter recordsAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		recordsAdapter = new OnlineRecordsAdapter(getActivity());
		setListAdapter(recordsAdapter);

		aQuery = new AQuery(getActivity());
		if (Helper.isInternetAvailable(getActivity()))
			loadRecords();
		else
			setEmptyText(getString(R.string.recordsGlobalNoInternet));

	}

	private void loadRecords() {
		if (recordsAdapter != null)
			recordsAdapter.clear();

		setListShown(false);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("timeInt", currentSpinnerSelection);

		aQuery.ajax(RECORDS_API_URL, params, JSONArray.class, this, "jsonCallback");
	}

	public void jsonCallback(String url, JSONArray json, AjaxStatus status) {
		if (json != null) {
			recordsAdapter.populate(json);
		} else
			Toast.makeText(getActivity(), R.string.recordsGlobalError, Toast.LENGTH_SHORT)
					.show();

		setListShown(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (MyKviz.isLoggedIn(getActivity()))
			inflater.inflate(R.menu.online_records, menu);
		else
			inflater.inflate(R.menu.online_records_not_logged_in, menu);

		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setListNavigationCallbacks(RecordsActivity.periodAdapter, this);
		actionBar.setSelectedNavigationItem(currentSpinnerSelection);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.onlineRecordsSignIn:
				loadActivity(LoginActivity.class);
				return true;
			case R.id.onlineRecordsRegister:
				loadActivity(RegistrationActivity.class);
				return true;
			case R.id.recordsPlay:
				startActivity(new Intent(getActivity(), QuestionsActivity.class));
				getActivity().finish();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {
		if (currentSpinnerSelection == position)
			return true;
		// TODO

		currentSpinnerSelection = position;

		loadRecords();
		return true;
	}

	void loadActivity(Class<?> cls) {
		Intent intent = new Intent(getActivity(), cls);

		startActivity(intent);
	}
}
