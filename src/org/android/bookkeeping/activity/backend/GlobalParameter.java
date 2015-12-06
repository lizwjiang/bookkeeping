/* GlobalParameter.java
 * Created on 2011-8-15
 */
package org.android.bookkeeping.activity.backend;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.application.BookkeepingApplication;
import org.android.bookkeeping.bo.GlobalParamBO;
import org.android.bookkeeping.bo.UserBO;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.DateUtil;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GlobalParameter extends Activity {
	private EditText singleMaxAmt, weeklyMaxAmt, monthlyMaxAmt;
	private Button submitBtn;
	BookkeepingApplication bookkeepingApp;
	private int id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.globalparam);
		bookkeepingApp = (BookkeepingApplication)getApplication(); 
		getAllRequiredViews();
		GlobalParamBO gpBO = getGlobalParameters();
		if(gpBO != null) setGlobalParameters(gpBO);
		setListener();
	}

	private void getAllRequiredViews() {
		singleMaxAmt = (EditText) findViewById(R.id.single_max_amount);
		weeklyMaxAmt = (EditText) findViewById(R.id.weekly_max_amount);
		monthlyMaxAmt = (EditText) findViewById(R.id.monthly_max_amount);
		submitBtn = (Button) findViewById(R.id.gp_submit);
	}

	private GlobalParamBO getGlobalParameters() {
		DBHelper helper = new DBHelper(getApplicationContext());
		String[] columns = new String[] { "id", "single_cosume_max_amount",
				"weekly_cosume_max_amount", " monthly_cosume_max_amount" };
		Cursor c = helper.query(DBUtil.GLOBALPARAM_TBL_NAME, columns);
		startManagingCursor(c);

		GlobalParamBO gpBO = null;
		if (c.moveToNext()) {
			gpBO = new GlobalParamBO();
			id = c.getInt(0);
			gpBO.setId(c.getInt(0));
			gpBO.setSingleMaxAmount(c.getDouble(1));
			gpBO.setWeeklyMaxAmount(c.getDouble(2));
			gpBO.setMonthlyMaxAmount(c.getDouble(3));
		}

		helper.close();
		return gpBO;
	}

	private void setGlobalParameters(GlobalParamBO bo) {
		singleMaxAmt.setText(String.valueOf(bo.getSingleMaxAmount()));
		weeklyMaxAmt.setText(String.valueOf(bo.getWeeklyMaxAmount()));
		monthlyMaxAmt.setText(String.valueOf(bo.getMonthlyMaxAmount()));
	}

	private void setListener() {
		submitBtn.setOnClickListener(submitListener);
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			long result = saveGlobalParam();
			if (result != -1) {
				if (id >= 0) {
					new BookkeepingAlertDialog().show(GlobalParameter.this,
							R.string.modify_success);
				} else {
					new BookkeepingAlertDialog().show(GlobalParameter.this,
							R.string.add_success);
				}
			} else {
				if (id >= 0) {
					new BookkeepingAlertDialog().show(GlobalParameter.this,
							R.string.modify_failed);
				} else {
					new BookkeepingAlertDialog().show(GlobalParameter.this,
							R.string.add_failed);
				}
			}
		}
	};

	private long saveGlobalParam() {
		ContentValues cv = new ContentValues();
		cv.put("single_cosume_max_amount", !StringUtil.isNull(singleMaxAmt
				.getText().toString()) ? Double.parseDouble(singleMaxAmt
				.getText().toString()) : 0);
		cv.put("weekly_cosume_max_amount", !StringUtil.isNull(weeklyMaxAmt
				.getText().toString()) ? Double.parseDouble(weeklyMaxAmt
				.getText().toString()) : 0);
		cv.put("monthly_cosume_max_amount", !StringUtil.isNull(monthlyMaxAmt
				.getText().toString()) ? Double.parseDouble(monthlyMaxAmt
				.getText().toString()) : 0);
		
		UserBO userBO = bookkeepingApp.getUserBO();
		cv.put("lchg_user_id", userBO.getUserNo());
		cv.put("lchg_time", DateUtil.getNowDate());

		DBHelper helper = new DBHelper(getApplicationContext());
		if (id >= 0) {			
			return helper.update(DBUtil.GLOBALPARAM_TBL_NAME, cv, "id = ?",
					new String[] { String.valueOf(id) });
		} else {
			cv.put("rche_user_id", userBO.getUserNo());
			cv.put("rche_time", DateUtil.getNowDate());
			return helper.insert(DBUtil.GLOBALPARAM_TBL_NAME, cv);
		}
	}
}
