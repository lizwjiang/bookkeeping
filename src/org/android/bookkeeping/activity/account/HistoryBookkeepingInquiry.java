package org.android.bookkeeping.activity.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.ListViewAdapter;
import org.android.bookkeeping.bo.SpinnerBO;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class HistoryBookkeepingInquiry extends Activity {
	private Spinner itemSpinner, consumerSpinner;
	private Button historySubmitBtn, cancelBtn;
	private ImageButton fromDateBtn, toDateBtn;
	private ListView listView;
	private EditText fromDate, toDate;
	private TextView relationship;
	private int yearOfFromDate, monthOfFromDate, dayOfFromDate, yearOfToDate,
			monthOfToDate, dayOfToDate, yearOfBookkeeping, monthOfBookkeeping,
			dayOfBookkeeping;
	private static final int FROMDATE = 0, TODATE = 1, MODIFYDATE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initQueryPage();
	}

	private void initQueryPage() {
		setContentView(R.layout.historybookkeepinginquiry);

		itemSpinner = (Spinner) findViewById(R.id.his_item_spinner);
		consumerSpinner = (Spinner) findViewById(R.id.his_consumer_spinner);
		relationship = (TextView) findViewById(R.id.his_relationship_with_user);
		fromDate = (EditText) findViewById(R.id.from_date_input);
		toDate = (EditText) findViewById(R.id.to_date_input);
		fromDateBtn = (ImageButton) findViewById(R.id.from_date_btn);
		toDateBtn = (ImageButton) findViewById(R.id.to_date_btn);
		historySubmitBtn = (Button) findViewById(R.id.his_submit_btn);

		initDate();
		BookkeepingInquiry.initItemSpinner(HistoryBookkeepingInquiry.this,
				itemSpinner);
		BookkeepingInquiry.initConsumerSpinner(HistoryBookkeepingInquiry.this,
				consumerSpinner, relationship);

		fromDateBtn.setOnClickListener(dateOnClickListener);
		toDateBtn.setOnClickListener(dateOnClickListener);
		historySubmitBtn.setOnClickListener(submitBtnOnClickListener);
	}

	private void initDate() {
		fromDate.setText(DateUtil.getNowDate());
		toDate.setText(DateUtil.getNowDate());
	}

	private OnClickListener dateOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.from_date_btn:
				yearOfFromDate = Integer.parseInt(fromDate.getText().toString()
						.substring(0, 4));
				monthOfFromDate = Integer.parseInt(fromDate.getText()
						.toString().substring(5, 7)) - 1;
				dayOfFromDate = Integer.parseInt(fromDate.getText().toString()
						.substring(8, 10));
				showDialog(FROMDATE);
				break;
			case R.id.to_date_btn:
				yearOfToDate = Integer.parseInt(toDate.getText().toString()
						.substring(0, 4));
				monthOfToDate = Integer.parseInt(toDate.getText().toString()
						.substring(5, 7)) - 1;
				dayOfToDate = Integer.parseInt(toDate.getText().toString()
						.substring(8, 10));
				showDialog(TODATE);
				break;
			case R.id.show_date_btn:
				yearOfBookkeeping = Integer.parseInt(BookkeepingInquiry.modifyDate.getText()
						.toString().substring(0, 4));
				monthOfBookkeeping = Integer.parseInt(BookkeepingInquiry.modifyDate.getText()
						.toString().substring(5, 7)) - 1;
				dayOfBookkeeping = Integer.parseInt(BookkeepingInquiry.modifyDate.getText()
						.toString().substring(8, 10));
				showDialog(MODIFYDATE);
				break;
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case FROMDATE:
			return new DatePickerDialog(this, fromDateSetListener,
					yearOfFromDate, monthOfFromDate, dayOfFromDate);
		case TODATE:
			return new DatePickerDialog(this, toDateSetListener, yearOfToDate,
					monthOfToDate, dayOfToDate);
		case MODIFYDATE:
			return new DatePickerDialog(this, bookkeepingDateSetListener,
					yearOfBookkeeping, monthOfBookkeeping, dayOfBookkeeping);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			fromDate.setText(new StringBuilder()
					.append(year)
					.append("-")
					.append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
							: (monthOfYear + 1)).append("-")
					.append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
		}
	};

	private DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			toDate.setText(new StringBuilder()
					.append(year)
					.append("-")
					.append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
							: (monthOfYear + 1)).append("-")
					.append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
		}
	};

	private DatePickerDialog.OnDateSetListener bookkeepingDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			BookkeepingInquiry.modifyDate.setText(new StringBuilder()
					.append(year)
					.append("-")
					.append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
							: (monthOfYear + 1)).append("-")
					.append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
		}
	};

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case FROMDATE:
			((DatePickerDialog) dialog).updateDate(yearOfFromDate,
					monthOfFromDate, dayOfFromDate);
			break;
		case TODATE:
			((DatePickerDialog) dialog).updateDate(yearOfToDate, monthOfToDate,
					dayOfToDate);
			break;
		}
	}

	private OnClickListener submitBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			List<HashMap<String, String>> bookkeepingList = getBookkeepingList();

			if (!bookkeepingList.isEmpty()) {
				setContentView(R.layout.bookkeepinginquiry);

				cancelBtn = (Button) findViewById(R.id.list_back_btn);
				cancelBtn.setOnClickListener(cancelBtnOnClickListener);

				showList();
			} else {
				new BookkeepingAlertDialog().show(
						HistoryBookkeepingInquiry.this,
						R.string.no_eligibility_record);
			}
		}
	};

	private List<HashMap<String, String>> getBookkeepingList() {
		List<HashMap<String, String>> bkList = new ArrayList<HashMap<String, String>>();
		String SqlCondition = " 1=1 ";
		ArrayList<String> conditionValuesList = new ArrayList<String>();

		if (!StringUtil.isNull(fromDate.getText().toString())
				&& !StringUtil.isNull(fromDate.getText().toString())) {
			SqlCondition = SqlCondition
					+ " and date(consumption_date) between date(?) and date(?) ";
			conditionValuesList.add(fromDate.getText().toString());
			conditionValuesList.add(toDate.getText().toString());
		}
		if (!StringUtil.isNull(((SpinnerBO) itemSpinner.getSelectedItem())
				.getValue())) {
			SqlCondition = SqlCondition + " and item = ? ";
			conditionValuesList.add(((SpinnerBO) itemSpinner.getSelectedItem())
					.getValue());
		}

		if (!StringUtil.isNull(((SpinnerBO) consumerSpinner.getSelectedItem())
				.getValue())) {
			SqlCondition = SqlCondition + " and consumer = ? ";
			conditionValuesList.add(((SpinnerBO) consumerSpinner
					.getSelectedItem()).getValue());
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

	private OnClickListener cancelBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initQueryPage();
		}
	};

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

	@SuppressWarnings("unused")
	private void viewInfo(String id) {
		BookkeepingInquiry.setViewInfo(HistoryBookkeepingInquiry.this, id);
	}

	@SuppressWarnings("unused")
	private void modifyInfo(final String id) {
		BookkeepingInquiry.setModifyInfo(HistoryBookkeepingInquiry.this, id,
				dateOnClickListener);
	}

	@SuppressWarnings("unused")
	private void delete(String id) {
		DBHelper helper = new DBHelper(getApplicationContext());

		helper.del(DBUtil.BOOKKEEPING_TBL_NAME, Integer.parseInt(id));
	}
}
