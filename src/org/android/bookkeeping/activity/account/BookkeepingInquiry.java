package org.android.bookkeeping.activity.account;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.DropdownAdapter;
import org.android.bookkeeping.activity.backend.InquireUser;
import org.android.bookkeeping.bo.BookkeepingBO;
import org.android.bookkeeping.bo.SpinnerBO;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.StringUtil;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class BookkeepingInquiry {
	private static Spinner modifyItem, modifyConsumer;
	private static Button bkSubmitBtn, bkBackBtn;
	private static ImageButton showDateBtn;
	private static EditText modifyAmount, modifyDescription;
	public static EditText modifyDate;
	private static TextView modifyRelationship;
	private static CheckBox modifyReimbursement;

	public static void initItemSpinner(Context ct, Spinner spinner) {
		DBHelper helper = new DBHelper(ct);

		Cursor c = helper.query(DBUtil.ITEM_TBL_NAME,
				new String[] { "id,name" });

		ArrayList<String> valuesList = new ArrayList<String>();

		while (c.moveToNext()) {
			valuesList.add(c.getString(1));
		}

		String[] values = valuesList.isEmpty() ? null : new String[valuesList
				.size()];

		c.close();
		helper.close();

		if (values == null) {
			new BookkeepingAlertDialog().show(ct, R.string.init_item);
		} else {
			valuesList.toArray(values);
			spinner.setAdapter(DropdownAdapter.getAdapter(ct, values, values));
		}
	}

	public static void initConsumerSpinner(Context ct, Spinner spinner,
			final TextView textView) {
		DBHelper helper = new DBHelper(ct);

		Cursor c = helper.query(DBUtil.CONSUMER_TBL_NAME,
				new String[] { "name,relationship" });

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

		c.close();
		helper.close();

		if (values == null) {
			new BookkeepingAlertDialog().show(ct, R.string.init_consumer);
		} else {
			valuesList.toArray(values);
			relationshipList.toArray(relationships);
			spinner.setAdapter(DropdownAdapter.getAdapter(ct, values, values));
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					textView.setText(relationships[arg2]);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					textView.setText("");
				}
			});
		}
	}

	public static void setViewInfo(final Context ct, String id) {
		LayoutInflater inflater = (LayoutInflater) ct
				.getSystemService(InquireUser.LAYOUT_INFLATER_SERVICE);
		final View vAlertDialog = inflater.inflate(R.layout.viewbookkeeping,
				null, false);

		final AlertDialog.Builder builder = new AlertDialog.Builder(ct);

		builder.setTitle(R.string.daily_inquiry_title);
		builder.setView(vAlertDialog);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		TextView viewBkDate = (TextView) vAlertDialog
				.findViewById(R.id.view_bk_date);
		TextView viewBkItem = (TextView) vAlertDialog
				.findViewById(R.id.view_bk_item);
		TextView viewBkConsumer = (TextView) vAlertDialog
				.findViewById(R.id.view_bk_consumer);
		TextView viewBkAmount = (TextView) vAlertDialog
				.findViewById(R.id.view_bk_amount);
		TextView viewBkReimbursement = (TextView) vAlertDialog
				.findViewById(R.id.view_bk_reimbursement);
		TextView viewBkDescription = (TextView) vAlertDialog
				.findViewById(R.id.view_bk_description);

		BookkeepingBO bkBO = getBkById(ct, id);

		viewBkDate.setText(bkBO.getDate());
		viewBkItem.setText(bkBO.getItem());
		viewBkConsumer.setText(bkBO.getConsumer());
		viewBkAmount.setText(bkBO.getAmount());
		viewBkReimbursement.setText(bkBO.getReimbursement());
		viewBkDescription.setText(bkBO.getDescription());

		builder.show();
	}

	public static void setModifyInfo(final Context ct, final String id,
			OnClickListener dateBtnOnClickListener) {
		LayoutInflater inflater = (LayoutInflater) ct
				.getSystemService(DailyBookkeepingInquiry.LAYOUT_INFLATER_SERVICE);

		final View vAlertDialog = inflater.inflate(R.layout.bookkeeping, null,
				false);

		AlertDialog.Builder builder = new AlertDialog.Builder(ct);

		builder.setTitle(R.string.bookkeeping_title);
		builder.setView(vAlertDialog);

		modifyDate = (EditText) vAlertDialog.findViewById(R.id.date_input);
		modifyItem = (Spinner) vAlertDialog.findViewById(R.id.item_spinner);
		modifyConsumer = (Spinner) vAlertDialog
				.findViewById(R.id.consumer_spinner);
		modifyRelationship = (TextView) vAlertDialog
				.findViewById(R.id.relationship_with_user);
		modifyAmount = (EditText) vAlertDialog.findViewById(R.id.amount_input);
		modifyReimbursement = (CheckBox) vAlertDialog
				.findViewById(R.id.reimbursement_check_box);
		modifyDescription = (EditText) vAlertDialog
				.findViewById(R.id.description_input);
		showDateBtn = (ImageButton) vAlertDialog
				.findViewById(R.id.show_date_btn);
		bkSubmitBtn = (Button) vAlertDialog.findViewById(R.id.bk_submit_btn);
		bkBackBtn = (Button) vAlertDialog.findViewById(R.id.bk_back_btn);
		bkSubmitBtn.setVisibility(View.INVISIBLE);
		bkBackBtn.setVisibility(View.INVISIBLE);

		initItemSpinner(ct, modifyItem);
		initConsumerSpinner(ct, modifyConsumer, modifyRelationship);

		showDateBtn.setOnClickListener(dateBtnOnClickListener);

		setValuesForModify(ct, id);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (validate(ct)) {
							if (modifyBookkeeping(ct, id) < 0) {
								new BookkeepingAlertDialog().show(ct,
										R.string.add_failed);
							} else {
								try {
									Field field = dialog.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);
								} catch (Exception e) {
									e.printStackTrace();
								}
								dialog.dismiss();
							}
						} else {
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						dialog.cancel();
					}
				});

		builder.show();
	}

	private static void setValuesForModify(Context ct, String id) {
		BookkeepingBO bkBO = getBkById(ct, id);

		modifyDate.setText(bkBO.getDate());
		modifyAmount.setText(bkBO.getAmount());
		modifyDescription.setText(bkBO.getDescription());

		if (bkBO.getReimbursement().equalsIgnoreCase("Y"))
			modifyReimbursement.setChecked(true);

		for (int i = 0; i < modifyItem.getChildCount(); i++) {
			if (modifyItem.getItemAtPosition(i).toString()
					.equalsIgnoreCase(bkBO.getItem())) {
				modifyItem.setSelection(i);
			}
		}

		for (int i = 0; i < modifyConsumer.getChildCount(); i++) {
			if (modifyConsumer.getItemAtPosition(i).toString()
					.equalsIgnoreCase(bkBO.getConsumer())) {
				modifyConsumer.setSelection(i);
			}
		}
	}

	private static BookkeepingBO getBkById(Context ct, String id) {
		BookkeepingBO bkBO = new BookkeepingBO();

		String[] columns = new String[] { "consumption_date", "item",
				"consumer", "amount", "reimbursement", "description" };

		String SqlCondition = " id=? ";
		String[] conditionValue = { id };

		DBHelper helper = new DBHelper(ct);
		Cursor c = helper.query(DBUtil.BOOKKEEPING_TBL_NAME, columns,
				SqlCondition, conditionValue, null);

		if (c.moveToNext()) {
			bkBO.setDate(c.getString(0));
			bkBO.setItem(c.getString(1));
			bkBO.setConsumer(c.getString(2));
			bkBO.setAmount(c.getString(3));
			bkBO.setReimbursement(c.getString(4));
			bkBO.setDescription(c.getString(5));
		}

		c.close();
		helper.close();

		return bkBO;
	}

	private static boolean validate(Context ct) {
		BookkeepingBO bkBO = getBkInfoForModify();

		BookkeepingAlertDialog bad = new BookkeepingAlertDialog();

		if (StringUtil.isNull(bkBO.getDate())) {
			bad.show(ct, R.string.date_no_null);
			return false;
		} else if (StringUtil.isNull(bkBO.getItem())) {
			bad.show(ct, R.string.item_no_null);
			return false;
		} else if (StringUtil.isNull(bkBO.getConsumer())) {
			bad.show(ct, R.string.consumer_no_null);
			return false;
		} else if (StringUtil.isNull(bkBO.getAmount())) {
			bad.show(ct, R.string.amount_no_null);
		}

		return true;
	}

	private static BookkeepingBO getBkInfoForModify() {
		BookkeepingBO bkBO = new BookkeepingBO();

		bkBO.setDate(modifyDate.getText().toString());
		bkBO.setAmount(modifyAmount.getText().toString());
		bkBO.setDescription(modifyDescription.getText().toString());

		if (modifyReimbursement.isChecked()) {
			bkBO.setReimbursement("Y");
		} else {
			bkBO.setReimbursement("N");
		}

		bkBO.setItem(((SpinnerBO) modifyItem.getSelectedItem()).getValue());
		bkBO.setConsumer(((SpinnerBO) modifyConsumer.getSelectedItem())
				.getValue());
		return bkBO;
	}

	private static int modifyBookkeeping(Context ct, String id) {
		DBHelper helper = new DBHelper(ct);

		ContentValues values = new ContentValues();

		String SqlCondition = " id=? ";
		String[] conditionValue = { id };

		BookkeepingBO bkBO = getBkInfoForModify();

		values.put("consumption_date", bkBO.getDate());
		values.put("consumer", bkBO.getConsumer());
		values.put("item", bkBO.getItem());
		values.put("amount", bkBO.getAmount());
		values.put("reimbursement", bkBO.getReimbursement());
		values.put("description", bkBO.getDescription());

		return helper.update(DBUtil.BOOKKEEPING_TBL_NAME, values, SqlCondition,
				conditionValue);
	}
}
