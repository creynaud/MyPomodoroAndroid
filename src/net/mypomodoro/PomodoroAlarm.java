package net.mypomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class PomodoroAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent startPomodoroActivityIntent = new Intent(context,
				PomodoroActivity.class);
		// TODO get rid of magic constant
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 123,
				startPomodoroActivityIntent, PendingIntent.FLAG_ONE_SHOT);

		// TODOÊlocalize
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("My pomodoro ended!")
				.setContentTitle("My pomodoro ended!")
				.setContentText("My pomodoro slice just ended!")
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(pendingIntent);
		Notification notification = mBuilder.getNotification();
		// TODO get rid of magic constant
		notificationManager.notify(0, notification);
	}

}
