package org.android.bookkeeping.activity.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.ListViewAdapter;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.DateUtil;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

public class DailyBookkeepingInquiry extends Activity {
	private ListView listView;
	private Button listBackBtn;
	private int yearOfBookkeeping, monthOfBookkeeping, dayOfBookkeeping;
	private static final int SHOWCALENDAR = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookkeepinginquiry);
		listBackBtn = (Button) findViewById(R.id.list_back_btn);
		listBackBtn.setVisibility(View.INVISIBLE);
		showList();
	}

	private void showList() {
		List<HashMap<String, String>> bkList = getBookkeepingList();
		listView = (ListView) findViewById(R.id.bookkeeping_listView);
		ListViewAdapter lva = new ListViewAdapter(this,
				R.layout.bookkeepinginquirylist, bkList);

		int[] listItems = new int[] { R.id.bk_list_item, R.id.bk_list_consumer,
				R.id.bk_list_amount, R.id.bk_list_date,
				R.id.bk_list_reimbursement };
		int[] operaItems = new int[] { R.id.bk_list_view, R.id.bk_list_modify,
				R.id.bk_list_delete };

		lva.allowDelete(true).allowModify(true).allowView(true)
				.setListViewItems(listItems).setListOperationItems(operaItems)
				.setCustomizedActivity(this);

		listView.setAdapter(lva);
	}

	private List<HashMap<String, String>> getBookkeepingList() {
		List<HashMap<String, String>> bkList = new ArrayList<HashMap<String, String>>();
		String SqlCondition = " 1=1 ";
		ArrayList<String> conditionValuesList = new ArrayList<String>();

		if (!StringUtil.isNull(DateUtil.getNowDate())) {
			SqlCondition = SqlCondition + " and consumption_date = ? ";
			conditionValuesList.add(DateUtil.getNowDate());
		}

		String[] conditionValues = conditionValuesList.isEmpty() ? null
				: new String[conditionValuesList.size()];

		if (conditionValues != null)
			conditionValuesList.toArray(conditionValues);

		String[] columns = new String[] { "id", "item", "consumer", "amount",
				"consumption_date", "reimbursement" };

		DBHelper helper = new DBHelper(getApplicationContext());
		Cursor c = helper.query(DBUtil.BOOKKEEPING_TBL_NAME, columns,
				SqlCondition, conditionValues, null);
		startManagingCursor(c);

		while (c.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", c.getString(0));
			map.put(String.valueOf(R.id.bk_list_item), c.getString(1));
			map.put(String.valueOf(R.id.bk_list_consumer), c.getString(2));
			map.put(String.valueOf(R.id.bk_list_amount), c.getString(3));
			map.put(String.valueOf(R.id.bk_list_date), c.getString(4));
			map.put(String.valueOf(R.id.bk_list_reimbursement), c.getString(5));
			bkList.add(map);
		}
		return bkList;
	}

	@SuppressWarnings("unused")
	private void viewInfo(String id) {
		BookkeepingInquiry.setViewInfo(DailyBookkeepingInquiry.this, id);
	}

	@SuppressWarnings("unused")
	private void modifyInfo(final String id) {
		BookkeepingInquiry.setModifyInfo(DailyBookkeepingInquiry.this, id,
				dateBtnOnClickListener);
	}

	private OnClickListener dateBtnOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			yearOfBookkeeping = Integer.parseInt(BookkeepingInquiry.modifyDate.getText()
					.toString().substring(0, 4));
			monthOfBookkeeping = Integer.parseInt(BookkeepingInquiry.modifyDate.getText()
					.toString().substring(5, 7)) - 1;
			dayOfBookkeeping = Integer.parseInt(BookkeepingInquiry.modifyDate.getText().toString()
					.substring(8, 10));
			showDialog(SHOWCALENDAR);
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			yearOfBookkeeping = year;
			monthOfBookkeeping = monthOfYear;
			dayOfBookkeeping = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		BookkeepingInquiry.modifyDate.setText(new StringBuilder()
				.append(yearOfBookkeeping)
				.append("-")
				.append((monthOfBookkeeping + 1) < 10 ? "0"
						+ (monthOfBookkeeping + 1) : (monthOfBookkeeping + 1))
				.append("-")
				.append((dayOfBookkeeping < 10) ? "0" + dayOfBookkeeping
						: dayOfBookkeeping));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SHOWCALENDAR:
			return new DatePickerDialog(this, mDateSetListener,
					yearOfBookkeeping, monthOfBookkeeping, dayOfBookkeeping);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case SHOWCALENDAR:
			((DatePickerDialog) dialog).updateDate(yearOfBookkeeping,
					monthOfBookkeeping, dayOfBookkeeping);
		}
	}

	@SuppressWarnings("unused")
	private void delete(String id) {
		DBHelper helper = new DBHelper(getApplicationContext());

		helper.del(DBUtil.BOOKKEEPING_TBL_NAME, Integer.parseInt(id));
	}
}
