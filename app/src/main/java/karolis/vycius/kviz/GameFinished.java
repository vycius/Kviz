package karolis.vycius.kviz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import karolis.vycius.kviz.adapters.TabsAdapter;
import karolis.vycius.kviz.database.DBProvider;
import karolis.vycius.kviz.database.UsersDB;
import karolis.vycius.kviz.fragments.GameLastQuestion;
import karolis.vycius.kviz.fragments.GameStatistics;
import karolis.vycius.kviz.helpers.DialogsHelper;
import karolis.vycius.kviz.helpers.MyKviz;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class GameFinished extends SherlockFragmentActivity implements OnClickListener {
	public final static String TAG_LAST_QUESTION = "lastQuestion";
	public final static String TAG_POINTS = "points";
	public final static String TAG_LEVEL = "level";
	public final static String TAG_TIME = "time";
	public final static String TAH_KIQ = "kiq";
	public final static String TAG_QUESTIONS_ANSWERED = "questionsAnswered";

	private final static String TAG_DEFAULT_NAME = "Zaidejas";
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private int points;
	private AQuery androidQuery;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_finished);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.gameFinishedPager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		final Bundle args = getIntent().getExtras();

		mTabsAdapter.addTab(
				mTabHost.newTabSpec(getString(R.string.gameFinishedTabStatistics))
						.setIndicator(getString(R.string.gameFinishedTabStatistics),
								getResources().getDrawable(R.drawable.ic_dashboard)),
				GameStatistics.class, args);

		if (args.containsKey(TAG_LAST_QUESTION)
				& args.getSerializable(TAG_LAST_QUESTION) != null)
			mTabsAdapter.addTab(
					mTabHost.newTabSpec(getString(R.string.gameFinishedTabAnswer))
							.setIndicator(getString(R.string.gameFinishedTabAnswer),
									getResources().getDrawable(R.drawable.ic_lightbulb)),
					GameLastQuestion.class, args);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

		points = getIntent().getExtras().getInt(TAG_POINTS);

		setOnClickListeners();
		showSaveRecordDialog();
		setActionBarProperties();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (token == null)
			saveRecordOnline();
	}

	protected void showSaveRecordDialog() {
		// TODO: prideti issaugojima surinkus kazkiek tasku
		if (points == 0)
			return;

		try {
			final View saveRecordsView = getLayoutInflater().inflate(
					R.layout.game_finished_dialog, null);

			final AutoCompleteTextView autoCompleteTV = (AutoCompleteTextView) saveRecordsView
					.findViewById(R.id.saveRecordsDialogPersonName);

			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (DialogInterface.BUTTON_POSITIVE == which)
						saveRecord(autoCompleteTV.getText().toString());
				}
			};

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());

			String defaultName = prefs.getString(TAG_DEFAULT_NAME, "");

			DialogsHelper.showSaveRecordDialog(this, points, getDistinctNames(), listener,
					defaultName, autoCompleteTV, saveRecordsView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveRecord(String name) {
		if (name == null || name.length() == 0)
			name = getString(R.string.gameFinishedDefaultName);

		java.util.Date date = new java.util.Date();

		ContentValues values = new ContentValues();
		values.put(UsersDB.NAME_COLUMN, name);
		values.put(UsersDB.POINTS_COLUMN, points);
		values.put(UsersDB.TIME_COLUMN, new Timestamp(date.getTime()).getTime() / 1000l);

		getContentResolver().insert(DBProvider.USERS_TABLE_URI, values);

		// Change default Name
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Editor editor = prefs.edit();

		editor.putString(TAG_DEFAULT_NAME, name);

		editor.commit();
	}

	protected ArrayList<String> getDistinctNames() {
		ArrayList<String> distinctNames = new ArrayList<String>();

		try {
			Cursor cursor = getContentResolver().query(DBProvider.DISTINCT_NAMES_URI, null,
					null, null, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
				distinctNames.add(cursor.getString(cursor
						.getColumnIndexOrThrow(UsersDB.NAME_COLUMN)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return distinctNames;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	public void setOnClickListeners() {
		int[] ids = { R.id.gameFinishedNewGame, R.id.gameFinishedBackToMenu };

		for (int id : ids) {
			findViewById(id).setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.gameFinishedBackToMenu:
				finish();
				break;
			case R.id.gameFinishedNewGame:
				Intent intent = new Intent(GameFinished.this, QuestionsActivity.class);

				startActivity(intent);
				finish();
				break;
		}
	}

	void setActionBarProperties() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	void saveRecordOnline() {
		token = MyKviz.getUserToken(this);

		if (token == null || points == 0)
			return;

		androidQuery = new AQuery(getApplicationContext());

		Map<String, Object> gidParams = new HashMap<String, Object>();
		gidParams.put("token", token);

		androidQuery.ajax(MyKviz.API_GID_URL, gidParams, String.class, this, "gidCallback");
	}

	public void gidCallback(String url, String gid, AjaxStatus status) {
		if (gid != null) {
			Bundle args = getIntent().getExtras();

			Map<String, Object> params = new HashMap<String, Object>();

			params.put("token", token);
			params.put("score", points);
			params.put("timeLeft", points);
			params.put("gameID", MyKviz.getGameId(gid, points));
			params.put("qNR", args.get(TAG_QUESTIONS_ANSWERED));
			params.put("timeLeft", 600l - args.getLong(TAG_TIME));
			params.put("flag", "android");

			params.put("beforeZone", 0);
			params.put("time", 0);
			params.put("help50", 0);
			params.put("helpWay", 0);
			params.put("helpVeto", 0);
			params.put("helpSkip", 0);

			androidQuery.ajax(MyKviz.API_SAVE_SCORE_URL, params, String.class, this,
					"recSavedCallback");
		}
	}

	public void recSavedCallback(String url, String string, AjaxStatus status) {
		Log.d("Record saved", string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getSupportMenuInflater().inflate(R.menu.game_finish, menu);

		try {
			ShareActionProvider mShareActionProvider = (ShareActionProvider) menu.findItem(
					R.id.gameFinsihShareRecord).getActionProvider();

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.gameFinishedShareSubject));
			intent.putExtra(Intent.EXTRA_TEXT,
					getString(R.string.gameFinishedShareText, points, "tasku", 0));

			if (intent != null)
				mShareActionProvider.setShareIntent(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.gameFinsihShowRecords:
				startActivity(new Intent(this, RecordsActivity.class));
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
