/* StatisticsQuery.java
 * Created on 2011-9-7
 */
package org.android.bookkeeping.activity.statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.DropdownAdapter;
import org.android.bookkeeping.bo.SpinnerBO;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.Constants;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.DateUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class StatisticsQuery extends Activity {
	private Spinner statisticsType, pictureType, dateType;
	private EditText fromDate, toDate;
	private Button submit;
	private ImageButton fromDateButton, toDateButton;
	private int yearOfFromDate, monthOfFromDate, dayOfFromDate, yearOfToDate,
			monthOfToDate, dayOfToDate;
	private static final int FROMDATE = 0, TODATE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statisticsquery);

		statisticsType = (Spinner) findViewById(R.id.statistics_type_input);
		pictureType = (Spinner) findViewById(R.id.picture_type_input);
		dateType = (Spinner) findViewById(R.id.statistics_date_input);

		fromDate = (EditText) findViewById(R.id.statistics_from_date_input);
		fromDateButton = (ImageButton) findViewById(R.id.statistics_from_date_button);
		toDate = (EditText) findViewById(R.id.statistics_to_date_input);
		toDateButton = (ImageButton) findViewById(R.id.statistics_to_date_button);

		submit = (Button) findViewById(R.id.statistics_submit);

		initRoleSpinner();

		fromDateButton.setOnClickListener(dateOnClickListener);
		toDateButton.setOnClickListener(dateOnClickListener);
		submit.setOnClickListener(submitOnClickListener);
	}

	private void initRoleSpinner() {
		String[] statisticsTypeValues = new String[] { "M", "I", "C" };
		String[] statisticsTypeTexts = new String[] {
				getResources().getString(R.string.statistics_monthly),
				getResources().getString(R.string.statistics_item),
				getResources().getString(R.string.statistics_consumer) };

		statisticsType.setAdapter(DropdownAdapter
				.getAdapter(StatisticsQuery.this, statisticsTypeValues,
						statisticsTypeTexts));

		String[] pictureTypeValues = new String[] { "M", "I", "C" };
		String[] pictureTypeTexts = new String[] {
				getResources().getString(R.string.statistics_pie_chart),
				getResources().getString(R.string.statistics_column_chart),
				getResources().getString(R.string.statistics_line_chart) };

		pictureType.setAdapter(DropdownAdapter.getAdapter(StatisticsQuery.this,
				pictureTypeValues, pictureTypeTexts));

		String[] dateTypeValues = new String[] { "T", "W", "M", "Q", "Y" };
		String[] dateTypeTexts = new String[] {
				getResources().getString(R.string.statistics_today),
				getResources().getString(R.string.statistics_this_week),
				getResources().getString(R.string.statistics_this_month),
				getResources().getString(R.string.statistics_this_quarter),
				getResources().getString(R.string.statistics_this_year) };

		dateType.setAdapter(DropdownAdapter.getAdapter(StatisticsQuery.this,
				dateTypeValues, dateTypeTexts));

		dateType.setOnItemSelectedListener(dateTypeOnItemSelectedListener);
	}

	private OnItemSelectedListener dateTypeOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			setDateValues(((SpinnerBO) dateType.getSelectedItem()).getValue());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private void setDateValues(String dateType) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);

		if (dateType.equalsIgnoreCase("T")) {
			fromDate.setText(DateUtil.getNowDate());
		} else if (dateType.equalsIgnoreCase("W")) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
				calendar.add(Calendar.WEEK_OF_MONTH, -1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			fromDate.setText(format.format(calendar.getTime()));
		} else if (dateType.equalsIgnoreCase("M")) {
			calendar.set(Calendar.DATE, 1);
			fromDate.setText(format.format(calendar.getTime()));
		} else if (dateType.equalsIgnoreCase("Q")) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)
					- calendar.get(Calendar.MONTH) % 3);
			calendar.set(Calendar.DATE, 1);
			fromDate.setText(format.format(calendar.getTime()));
		} else if (dateType.equalsIgnoreCase("Y")) {
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
			calendar.set(Calendar.DATE, 1);
			fromDate.setText(format.format(calendar.getTime()));
		}
		toDate.setText(DateUtil.getNowDate());
	}

	private OnClickListener dateOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.statistics_from_date_button:
				yearOfFromDate = Integer.parseInt(fromDate.getText().toString()
						.substring(0, 4));
				monthOfFromDate = Integer.parseInt(fromDate.getText()
						.toString().substring(5, 7)) - 1;
				dayOfFromDate = Integer.parseInt(fromDate.getText().toString()
						.substring(8, 10));
				showDialog(FROMDATE);
				break;
			case R.id.statistics_to_date_button:
				yearOfToDate = Integer.parseInt(toDate.getText().toString()
						.substring(0, 4));
				monthOfToDate = Integer.parseInt(toDate.getText().toString()
						.substring(5, 7)) - 1;
				dayOfToDate = Integer.parseInt(toDate.getText().toString()
						.substring(8, 10));
				showDialog(TODATE);
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

	private OnClickListener submitOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (getBundle().isEmpty() == false) {
				Intent intent = new Intent();
				intent.setClass(StatisticsQuery.this, StatisticsChart.class);
				intent.putExtras(getBundle());
				startActivity(intent);
			} else {
				new BookkeepingAlertDialog().show(StatisticsQuery.this,
						R.string.no_eligibility_record);
			}
		}
	};

	private Bundle getBundle() {
		List<HashMap<String, String>> valuesList = new ArrayList<HashMap<String, String>>();

		String fromDateStr = fromDate.getText().toString();
		String toDateStr = toDate.getText().toString();
		String statisticsTypeStr = ((SpinnerBO) statisticsType
				.getSelectedItem()).getValue();

		DBHelper helper = new DBHelper(getApplicationContext());

		Cursor c = null;

		if (statisticsTypeStr.equalsIgnoreCase("M")) {
			c = helper.query(DBUtil.BOOKKEEPING_TBL_NAME, new String[] {
					"sum(amount)", "strftime('%m',consumption_date)" },
					"date(consumption_date) between date(?) and date(?)",
					new String[] { fromDateStr, toDateStr },
					"strftime('%m',consumption_date)", null);
		} else if (statisticsTypeStr.equalsIgnoreCase("I")) {
			c = helper.query(DBUtil.BOOKKEEPING_TBL_NAME, new String[] {
					"sum(amount)", "item" },
					"date(consumption_date) between date(?) and date(?)",
					new String[] { fromDateStr, toDateStr }, "item", null);
		} else if (statisticsTypeStr.equalsIgnoreCase("C")) {
			c = helper.query(DBUtil.BOOKKEEPING_TBL_NAME, new String[] {
					"sum(amount)", "consumer" },
					"date(consumption_date) between date(?) and date(?)",
					new String[] { fromDateStr, toDateStr }, "consumer", null);
		}

		startManagingCursor(c);

		while (c.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Constants.STATISTICS_VALUE_KEY, c.getString(0));
			map.put(Constants.STATISTICS_CATEGORY_KEY, c.getString(1));
			valuesList.add(map);
		}

		helper.close();

		double[] valueKey = valuesList.isEmpty() ? null : new double[valuesList
				.size()];

		String[] categoryKey = valuesList.isEmpty() ? null
				: new String[valuesList.size()];

		for (int i = 0; i < valuesList.size(); i++) {
			valueKey[i] = Double.parseDouble(valuesList.get(i).get(
					Constants.STATISTICS_VALUE_KEY));
			categoryKey[i] = valuesList.get(i).get(
					Constants.STATISTICS_CATEGORY_KEY);
		}
		Bundle bundle = new Bundle();

		if (valueKey != null) {
			bundle.putStringArray(Constants.STATISTICS_CATEGORY_KEY,
					categoryKey);
			bundle.putDoubleArray(Constants.STATISTICS_VALUE_KEY, valueKey);
		}

		return bundle;
	}
}
