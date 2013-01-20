package net.mypomodoro;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PomodoroActivity extends SherlockActivity {

	private static final String POMODORO_END_TIME_IN_MS_PREFS_KEY = "pomodoroEndTimeInMs";
	private static final String STARTED_PREFS_KEY = "started";

	private static final int DURATION_IN_S = 30;

	private boolean started;
	private long pomodoroEndTimeInMs;

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mypomodoro);
		ImageView pomodoroImageView = (ImageView) findViewById(R.id.pomodoroImageView);
		pomodoroImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				Intent alarmIntent = new Intent(PomodoroActivity.this,
						PomodoroAlarm.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						PomodoroActivity.this, 0, alarmIntent, 0);
				if (isStarted()) {
					alarmManager.cancel(pendingIntent);
					setStarted(false, 0);
				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.add(Calendar.SECOND, DURATION_IN_S);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(), pendingIntent);
					setStarted(true, calendar.getTimeInMillis());
				}
				updateStartedStatusView();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = getSharedPreferences(
				PomodoroActivity.class.getName(), 0);
		setStarted(settings.getBoolean(STARTED_PREFS_KEY, false),
				settings.getLong(POMODORO_END_TIME_IN_MS_PREFS_KEY, 0));
	}

	@Override
	protected void onPause() {
		super.onPause();

		cancelTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Log.d("boo", "settings");
			break;
		case R.id.search:
			Log.d("boo", "search");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateStartedStatusView() {
		// TODOÊlocalize
		TextView textView = (TextView) findViewById(R.id.textView);
		if (this.started) {
			textView.setText("Started: " + (getRemainingTimeInMs() / 1000)
					+ "s");
		} else {
			textView.setText("Not started");
		}
	}

	private void setStarted(boolean started, long pomodoroEndTimeInMs) {
		this.started = started;
		this.pomodoroEndTimeInMs = pomodoroEndTimeInMs;

		persistStartedStatus();
		updateTimer();
		updateStartedStatusView();
	}

	private void persistStartedStatus() {
		SharedPreferences settings = getSharedPreferences(
				PomodoroActivity.class.getName(), 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(STARTED_PREFS_KEY, this.started);
		editor.putLong(POMODORO_END_TIME_IN_MS_PREFS_KEY,
				this.pomodoroEndTimeInMs);
		editor.commit();
	}

	private boolean isStarted() {
		return this.started;
	}

	private void updateTimer() {
		if (this.started) {
			startTimer();
		} else {
			cancelTimer();
		}
	}

	private void startTimer() {
		timer = new Timer();
		int repeatEachSecond = 1000;
		int startsRightNow = 0;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						long remainingTimeInMs = getRemainingTimeInMs();
						if (remainingTimeInMs < 0) {
							setStarted(false, 0);
						} else {
							updateStartedStatusView();
						}
					}
				});
			}
		}, startsRightNow, repeatEachSecond);
	}

	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private long getRemainingTimeInMs() {
		if (started) {
			return pomodoroEndTimeInMs - System.currentTimeMillis();
		} else {
			return -1;
		}
	}
}
