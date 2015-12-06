/* OverspendReceiver.java
 * Created on 2011-9-19
 */
package org.android.bookkeeping.receiver;

import org.android.bookkeeping.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public class OverspendReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(), 0);

		Resources res = context.getResources();

		Notification n = new Notification(R.drawable.income,
				res.getString(R.string.overspend), System.currentTimeMillis());


		/*n.setLatestEventInfo(context, res.getString(R.string.overspend),
				res.getString(R.string.overspend_message), contentIntent);*/

		n.vibrate = new long[] { 100, 250, 100, 500 };

		nm.notify(0, n);
	}
}
