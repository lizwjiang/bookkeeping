package org.android.bookkeeping.activity.account;

import java.util.ArrayList;
import java.util.Calendar;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.DropdownAdapter;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.receiver.OverspendReceiver;
import org.android.bookkeeping.service.MonthlyTotalAmountUpdateService;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class Bookkeeping extends Activity {
	private Spinner itemSpinner, consumerSpinner;
	private Button bkSubmitBtn, bkBackBtn;
	private ImageButton showDateBtn;
	private EditText date, amount, description;
	private TextView relationship;
	private CheckBox reimbursementChkBox;
	private int yearOfBookkeeping, monthOfBookkeeping, dayOfBookkeeping;
	private static final int SHOWCALENDAR = 0;
	private OverspendReceiver overspendReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookkeeping);

		itemSpinner = (Spinner) findViewById(R.id.item_spinner);
		consumerSpinner = (Spinner) findViewById(R.id.consumer_spinner);
		bkSubmitBtn = (Button) findViewById(R.id.bk_submit_btn);
		bkBackBtn = (Button) findViewById(R.id.bk_back_btn);
		showDateBtn = (ImageButton) findViewById(R.id.show_date_btn);
		date = (EditText) findViewById(R.id.date_input);
		amount = (EditText) findViewById(R.id.amount_input);
		description = (EditText) findViewById(R.id.description_input);
		relationship = (TextView) findViewById(R.id.relationship_with_user);
		reimbursementChkBox = (CheckBox) findViewById(R.id.reimbursement_check_box);

		initItemSpinner();
		initConsumerSpinner();
		initDateTime();
		showDateBtn.setOnClickListener(dateBtnOnClickListener);
		bkSubmitBtn.setOnClickListener(bkSubmitBtnClickListener);
		
		initReceiver();
	}

	private void initItemSpinner() {
		DBHelper helper = new DBHelper(getApplicationContext());

		Cursor c = helper.query(DBUtil.ITEM_TBL_NAME,
				new String[] { "id,name" });
		startManagingCursor(c);

		ArrayList<String> valuesList = new ArrayList<String>();

		while (c.moveToNext()) {
			valuesList.add(c.getString(1));
		}

		String[] values = valuesList.isEmpty() ? null : new String[valuesList
				.size()];

		helper.close();

		if (values == null) {
			new BookkeepingAlertDialog().show(Bookkeeping.this,
					R.string.init_item);
		} else {
			valuesList.toArray(values);

			itemSpinner.setAdapter(DropdownAdapter.getAdapter(Bookkeeping.this,
					values, values));
		}
	}

	private void initConsumerSpinner() {
		DBHelper helper = new DBHelper(getApplicationContext());

		Cursor c = helper.query(DBUtil.CONSUMER_TBL_NAME,
				new String[] { "name,relationship" });
		startManagingCursor(c);

		ArrayList<String> valuesList = new ArrayList<String>();
		ArrayList<String> relationshipList = new ArrayList<String>();

		while (c.moveToNext()) {
			valuesList.add(c.getString(0));
			relationshipList.add(c.getString(1));
		}

		String[] values = valuesList.isEmpty() ? null : new String[valuesList
				.size()];

		final String[] relationships = relationshipList.isEmpty() ? null
				: new String[relationshipList.size()];

		helper.close();

		if (values == null) {
			new BookkeepingAlertDialog().show(Bookkeeping.this,
					R.string.init_consumer);
		} else {
			valuesList.toArray(values);
			relationshipList.toArray(relationships);

			consumerSpinner.setAdapter(DropdownAdapter.getAdapter(
					Bookkeeping.this, values, values));
			consumerSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							relationship.setText(relationships[arg2]);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							relationship.setText("");
						}
					});
		}
	}

	private void initDateTime() {
		final Calendar c = Calendar.getInstance();
		yearOfBookkeeping = c.get(Calendar.YEAR);
		monthOfBookkeeping = c.get(Calendar.MONTH);
		dayOfBookkeeping = c.get(Calendar.DAY_OF_MONTH);

		updateDisplay();
	}

	private void updateDisplay() {
		date.setText(new StringBuilder()
				.append(yearOfBookkeeping)
				.append("-")
				.append((monthOfBookkeeping + 1) < 10 ? "0"
						+ (monthOfBookkeeping + 1) : (monthOfBookkeeping + 1))
				.append("-")
				.append((dayOfBookkeeping < 10) ? "0" + dayOfBookkeeping
						: dayOfBookkeeping));
	}

	private OnClickListener dateBtnOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			yearOfBookkeeping = Integer.parseInt(date.getText().toString()
					.substring(0, 4));
			monthOfBookkeeping = Integer.parseInt(date.getText().toString()
					.substring(5, 7)) - 1;
			dayOfBookkeeping = Integer.parseInt(date.getText().toString()
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

	private OnClickListener bkSubmitBtnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String consumptionDate = date.getText().toString();
			String consumer = consumerSpinner.getSelectedItem().toString();
			String item = itemSpinner.getSelectedItem().toString();
			String amountValue = amount.getText().toString();
			String descriptionText = description.getText().toString();

			if (StringUtil.isNull(consumptionDate)) {
				new BookkeepingAlertDialog().show(Bookkeeping.this,
						R.string.date_no_null);
			} else if (StringUtil.isNull(consumer)) {
				new BookkeepingAlertDialog().show(Bookkeeping.this,
						R.string.consumer_no_null);
			} else if (StringUtil.isNull(item)) {
				new BookkeepingAlertDialog().show(Bookkeeping.this,
						R.string.item_no_null);
			} else if (StringUtil.isNull(amountValue)) {
				new BookkeepingAlertDialog().show(Bookkeeping.this,
						R.string.amount_no_null);
			} else {
				if (addBookkeeping(consumptionDate, consumer, item,
						amountValue, descriptionText) == -1) {
					new BookkeepingAlertDialog().show(Bookkeeping.this,
							R.string.add_failed);
				} else {
					new BookkeepingAlertDialog().show(Bookkeeping.this,
							R.string.add_success, new int[] {
									R.string.add_more,
									R.string.back_to_bookkeeping },
							new String[] { "clear" }, new String[] { "clear" });
				}
			}
		}
	};

	private long addBookkeeping(String consumptionDate, String consumer,
			String item, String amountValue, String descriptionText) {

		startMonthlyTotalAmountUpdateService(consumptionDate, consumer,
				amountValue);

		ContentValues values = new ContentValues();

		values.put("consumption_date", consumptionDate);
		values.put("consumer", consumer);
		values.put("item", item);
		values.put("amount", amountValue);
		values.put("reimbursement", reimbursementChkBox.isChecked() ? "Y" : "N");
		values.put("description", descriptionText);

		DBHelper helper = new DBHelper(getApplicationContext());

		return helper.insert(DBUtil.BOOKKEEPING_TBL_NAME, values);
	}

	private void startMonthlyTotalAmountUpdateService(String date,
			String consumer, String amount) {
		Intent intent = new Intent(Bookkeeping.this, MonthlyTotalAmountUpdateService.class);
		intent.putExtra("month", date.substring(5, 7));
		intent.putExtra("consumer", consumer);
		intent.putExtra("amount", amount);
		this.startService(intent);
	}

	@SuppressWarnings("unused")
	private void clear() {
		amount.setText("");
		description.setText("");
		reimbursementChkBox.setChecked(false);
		consumerSpinner.setSelection(0);
		itemSpinner.setSelection(0);
		initDateTime();
	}
	
	private void initReceiver() {
		overspendReceiver = new OverspendReceiver();
		
		IntentFilter filter = new IntentFilter();
		
		filter.addAction("org.android.bookkeeping.broadcast");
		
		this.registerReceiver(overspendReceiver, filter);
	}
}
