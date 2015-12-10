package karolis.vycius.kviz;

import karolis.vycius.kviz.adapters.TabsAdapter;
import karolis.vycius.kviz.fragments.OfflineRecords;
import karolis.vycius.kviz.fragments.OnlineRecords;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class RecordsActivity extends SherlockFragmentActivity {
	TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	public static SpinnerAdapter periodAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_records_activity);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.recordsViewPager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		mTabsAdapter.addTab(
				mTabHost.newTabSpec(getString(R.string.recordsLocaleTitle)).setIndicator(
						getString(R.string.recordsLocaleTitle),
						getResources().getDrawable(R.drawable.ic_mobile_phone)),
				OfflineRecords.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec(getString(R.string.recordsGlobalTitle)).setIndicator(
						getString(R.string.recordsGlobalTitle),
						getResources().getDrawable(R.drawable.ic_web)), OnlineRecords.class,
				null);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

		setActionBarProperties();

	}

	void setActionBarProperties() {
		periodAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPeriods,
				android.R.layout.simple_spinner_dropdown_item);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setListNavigationCallbacks(periodAdapter, null);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}
}
