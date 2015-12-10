package karolis.vycius.kviz.fragments;

import karolis.vycius.kviz.R;
import karolis.vycius.kviz.RecordsActivity;
import karolis.vycius.kviz.adapters.OfflineRecordaAdapter;
import karolis.vycius.kviz.database.DBProvider;
import karolis.vycius.kviz.database.UsersDB;
import karolis.vycius.kviz.entities.Helper;
import karolis.vycius.kviz.loaders.AsyncTaskDeleteRecords;
import karolis.vycius.kviz.loaders.AsyncTaskDeleteRecords.OnRecordsDeleted;
import karolis.vycius.kviz.loaders.OfflineRecordsQueryHandler;
import karolis.vycius.kviz.loaders.OfflineRecordsQueryHandler.OnRecordsLoaded;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class OfflineRecords extends SherlockListFragment implements OnQueryTextListener,
		OnRecordsLoaded, ActionMode.Callback, OnItemClickListener, OnItemLongClickListener,
		OnRecordsDeleted, OnNavigationListener {
	private int currentSpinnerSelection = 2;
	private String currentSearchText = "";
	private OfflineRecordaAdapter recordsAdapter;
	private OfflineRecordsQueryHandler queryHandler;
	private ActionMode actionMode = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		recordsAdapter = new OfflineRecordaAdapter(getActivity());
		setListAdapter(recordsAdapter);

		setHasOptionsMenu(true);

		queryHandler = new OfflineRecordsQueryHandler(getActivity().getContentResolver(), this);

		loadRecords();

		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
		setEmptyText(getString(R.string.recordsNoLocalRecords));

	}

	private void loadRecords() {
		setListShown(false);
		final String[] columns = { UsersDB.ID_COLUMN_WITH_AS, UsersDB.NAME_COLUMN,
				UsersDB.POINTS_COLUMN };
		final String where = "lower(" + UsersDB.NAME_COLUMN + ") LIKE '%' || ? || '%' AND "
				+ formatBeetween(UsersDB.TIME_COLUMN);
		final String[] whereArgs = { currentSearchText };
		final String orderBy = UsersDB.POINTS_COLUMN + " DESC, " + UsersDB.TIME_COLUMN
				+ " DESC LIMIT 200";

		queryHandler.startQuery(0, null, DBProvider.USERS_TABLE_URI, columns, where,
				whereArgs, orderBy);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.offline_records, menu);

		MenuItem searchMenuItem = menu.findItem(R.id.offlineRecordsSearch);

		final SearchView searchView = (SearchView) searchMenuItem.getActionView();

		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint(getString(R.string.recordsSearchHint));

		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setListNavigationCallbacks(RecordsActivity.periodAdapter, this);
		actionBar.setSelectedNavigationItem(currentSpinnerSelection);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.offlineRecordsEditButton:
				startActionMode();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void startActionMode() {
		if (actionMode == null)
			actionMode = getSherlockActivity().startActionMode(this);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {
		if (currentSpinnerSelection == position)
			return true;

		currentSpinnerSelection = position;
		loadRecords();

		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.equals(currentSearchText))
			return true;

		currentSearchText = newText;

		loadRecords();
		return true;
	}

	private String formatBeetween(String column) {
		long from = 0, to = Helper.TimeStampHelper.getCurrentTimeStamp();

		switch (currentSpinnerSelection) {
			case 1:
				from = Helper.TimeStampHelper.getTodayStart();
				break;
			case 2:
				from = Helper.TimeStampHelper.getWeekStart();
				break;
			case 3:
				from = Helper.TimeStampHelper.getMonthStart();
				break;
			case 4:
				from = Helper.TimeStampHelper.getYesterdayStart();
				to = Helper.TimeStampHelper.getYesterdayFinish();
				break;
		}

		return column + " BETWEEN " + from / 1000l + " AND " + to / 1000l;
	}

	@Override
	public void onQueryComplete(Cursor cursor) {
		recordsAdapter.populate(cursor);
		setListShown(true);
	}

	private void onListItemSelect(int position) {
		if (actionMode != null) {
			recordsAdapter.toggleSelection(position);

			actionMode.setTitle(getString(R.string.recordsActionBarSelected,
					recordsAdapter.getSelectedCount()));
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.getMenuInflater().inflate(R.menu.action_mode_records, menu);
		mode.setTitle(getString(R.string.recordsActionBarSelected, 0));
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.recordsDeleteSelected:
				Log.d("Records selected", recordsAdapter.getSelectedCount() + " ");
				if (recordsAdapter.getSelectedCount() > 0)
					new AsyncTaskDeleteRecords(getActivity(), recordsAdapter.getSelectedIds(),
							this).execute();

				mode.finish();
				return true;
		}

		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		recordsAdapter.removeSelection();
		actionMode = null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (actionMode == null)
			startActionMode();

		onListItemSelect(position);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		onListItemSelect(position);
	}

	@Override
	public void onRecordsDeleted() {
		loadRecords();
	}

}
