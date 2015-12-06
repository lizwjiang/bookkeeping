/* MonthlyTotalAmountUpdateService.java
 * Created on 2011-9-20
 */
package org.android.bookkeeping.service;

import java.util.HashMap;

import org.android.bookkeeping.R;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

public class MonthlyTotalAmountUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String month = intent.getStringExtra("month");
		String consumer = intent.getStringExtra("consumer");
		String amount = intent.getStringExtra("amount");

		HashMap<String, String> monthlyTotalAmount = getMonthlyTotalAmount(
				month, consumer);
		long totalAmount;

		if (!monthlyTotalAmount.isEmpty()) {
			totalAmount = Long.parseLong(monthlyTotalAmount.get(String
					.valueOf(R.string.amount))) + Long.parseLong(amount);

			DBHelper helper = new DBHelper(getApplicationContext());

			ContentValues values = new ContentValues();
			values.put("amount", totalAmount);

			helper.update(DBUtil.MONTHLY_TEMP_TBL_NAME, values, "id = ?",
					new String[] { monthlyTotalAmount.get("id") });

			helper.close();
		} else {
			totalAmount = Long.parseLong(amount);

			DBHelper helper = new DBHelper(getApplicationContext());

			ContentValues values = new ContentValues();
			values.put("month", month);
			values.put("consumer", consumer);
			values.put("amount", totalAmount);

			helper.insert(DBUtil.MONTHLY_TEMP_TBL_NAME, values);

			helper.close();
		}

		if (totalAmount > 100) {
			Intent broadcastIntent = new Intent("org.android.bookkeeping.broadcast");
			this.sendBroadcast(broadcastIntent);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	private HashMap<String, String> getMonthlyTotalAmount(String month,
			String consumer) {
		HashMap<String, String> map = new HashMap<String, String>();
		DBHelper helper = new DBHelper(getApplicationContext());

		Cursor c = helper.query(DBUtil.MONTHLY_TEMP_TBL_NAME,
				new String[] { "id, amount" }, "month = ? and consumer = ?",
				new String[] { month, consumer });

		if (c.moveToNext()) {
			map.put("id", c.getString(0));
			map.put(String.valueOf(R.string.amount), c.getString(1));
		}

		c.close();
		helper.close();

		return map;
	}

}
