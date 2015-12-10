package karolis.vycius.kviz;

import karolis.vycius.kviz.fragments.MyKvizSidebar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSlidingMenus();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setOnClickListeners();
		checkForNewerDBVersion();
	}

	void setOnClickListeners() {
		final int elements[] = { R.id.prefs, R.id.about, R.id.rec, R.id.play };

		for (int e : elements)
			findViewById(e).setOnClickListener(this);
	}

	public void checkForNewerDBVersion() {
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("Prefs", 0);

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			if (pInfo != null && !pInfo.versionName.equals(prefs.getString("DBversion", "0"))) {

				if (getApplicationContext().getDatabasePath("MyDB").exists())
					deleteDatabase("MyDB");

				Editor editor = prefs.edit();

				editor.putString("DBversion", pInfo.versionName);
				editor.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.play:
				startActivity(new Intent(MainActivity.this, QuestionsActivity.class));
				break;
			case R.id.rec:
				startActivity(new Intent(MainActivity.this, RecordsActivity.class));
				break;
			case R.id.about:

				startActivity(new Intent(MainActivity.this, About.class));
				break;
			case R.id.prefs:
				toggle();
				break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.animator.fadein, R.animator.fadeout);
	}

	void setSlidingMenus() {
		setBehindContentView(R.layout.fragment_container);
		getSlidingMenu().setSlidingEnabled(true);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container,
						Fragment.instantiate(this, MyKvizSidebar.class.getName())).commit();

		setSlidingMenuOptions(getSlidingMenu());
	}

	void setSlidingMenuOptions(SlidingMenu menu) {
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindScrollScale(0.35f);
		menu.setFadeDegree(0.35f);
	}
}
