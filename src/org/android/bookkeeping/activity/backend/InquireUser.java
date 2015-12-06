/* InquireUser.java
 * Created on 2011-8-27
 */
package org.android.bookkeeping.activity.backend;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.DropdownAdapter;
import org.android.bookkeeping.activity.ListViewAdapter;
import org.android.bookkeeping.bo.SpinnerBO;
import org.android.bookkeeping.bo.UserBO;
import org.android.bookkeeping.common.sharepreference.BookkeepingSharePreference;
import org.android.bookkeeping.provider.Users.User;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class InquireUser extends Activity {
	private Spinner genderSpinner, roleModify;
	private Button inquiry, backToInquireUser;
	private ListView listView;
	private EditText userNo, name, nickName, passwordModify,
			confirmPasswordModify, nameModify, nickNameModify, phoneModify,
			emailModify;
	private TextView userNoModify;
	private RadioButton maleButtonModify, femaleButtonModify;
	private CheckBox marriedModify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initQueryPage();
	}

	private void initQueryPage() {
		setContentView(R.layout.inquireuser);

		userNo = (EditText) findViewById(R.id.user_no_input);
		name = (EditText) findViewById(R.id.user_name_input);
		nickName = (EditText) findViewById(R.id.nick_name_input);
		genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
		inquiry = (Button) findViewById(R.id.inquire_user_btn);

		initGenderSpinner();
		inquiry.setOnClickListener(inquiryBtnOnClickListener);
	}

	private void initGenderSpinner() {
		String[] values = new String[] { "A", "M", "F" };
		String[] texts = new String[] { getResources().getString(R.string.all),
				getResources().getString(R.string.male),
				getResources().getString(R.string.female) };

		genderSpinner.setAdapter(DropdownAdapter.getAdapter(InquireUser.this,
				values, texts));
	}

	private OnClickListener inquiryBtnOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			List<HashMap<String, String>> userList = getUserList();

			if (!userList.isEmpty()) {
				setContentView(R.layout.userlistmanager);

				backToInquireUser = (Button) findViewById(R.id.back_to_inquire_user_btn);
				backToInquireUser
						.setOnClickListener(backToInquireUserOnClickListener);

				showList();
			} else {
				new BookkeepingAlertDialog().show(InquireUser.this,
						R.string.no_eligibility_record);
			}
		}
	};

	private List<HashMap<String, String>> getUserList() {
		List<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();

		UserBO userBO = getUserInfoFromPage();
		String SqlCondition = " 1=1 ";
		ArrayList<String> conditionValuesList = new ArrayList<String>();

		if (!StringUtil.isNull(userBO.getUserNo())) {
			SqlCondition = SqlCondition + " and userno = ? ";
			conditionValuesList.add(userBO.getUserNo());
		}
		if (!StringUtil.isNull(userBO.getName())) {
			SqlCondition = SqlCondition + " and name = ? ";
			conditionValuesList.add(userBO.getName());
		}
		if (!StringUtil.isNull(userBO.getNickName())) {
			SqlCondition = SqlCondition + " and nickname = ? ";
			conditionValuesList.add(userBO.getNickName());
		}
		if (!userBO.getGender().equalsIgnoreCase("A")) {
			SqlCondition = SqlCondition + " and gender = ? ";
			conditionValuesList.add(userBO.getGender());
		}

		String[] conditionValues = conditionValuesList.isEmpty() ? null
				: new String[conditionValuesList.size()];
		if (conditionValues != null)
			conditionValuesList.toArray(conditionValues);
		String[] columns = new String[] { User._ID, User.USERNO, User.NAME,
				User.NICKNAME, User.ROLE };

		Cursor c = getContentResolver().query(User.CONTENT_URI, columns,
				SqlCondition, conditionValues, null);
		startManagingCursor(c);

		while (c.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", c.getString(0));
			map.put(String.valueOf(R.id.user_list_user_no), c.getString(1));
			map.put(String.valueOf(R.id.user_list_user_name), c.getString(2));
			map.put(String.valueOf(R.id.user_list_user_nickname),
					c.getString(3));
			map.put(String.valueOf(R.id.user_list_user_role), c.getString(4));
			userList.add(map);
		}

		return userList;
	}

	private UserBO getUserInfoFromPage() {
		UserBO userBO = new UserBO();

		userBO.setUserNo(userNo.getText().toString());
		userBO.setName(name.getText().toString());
		userBO.setNickName(nickName.getText().toString());

		userBO.setGender(((SpinnerBO) genderSpinner.getSelectedItem())
				.getValue());

		return userBO;
	}

	private OnClickListener backToInquireUserOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			initQueryPage();
		}
	};

	private void showList() {
		List<HashMap<String, String>> userList = getUserList();
		listView = (ListView) findViewById(R.id.user_listview);
		ListViewAdapter lva = new ListViewAdapter(this, R.layout.userlist,
				userList);

		int[] listItems = new int[] { R.id.user_list_user_no,
				R.id.user_list_user_name, R.id.user_list_user_nickname,
				R.id.user_list_user_role };
		int[] operaItems = new int[] { R.id.user_view, R.id.user_modify,
				R.id.user_delete };

		lva.allowDelete(true).allowModify(true).allowView(true)
				.setListViewItems(listItems).setListOperationItems(operaItems)
				.setCustomizedActivity(this);

		listView.setAdapter(lva);
	}

	@SuppressWarnings("unused")
	private void viewInfo(String id) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(InquireUser.LAYOUT_INFLATER_SERVICE);

		final View vAlertDialog = inflater.inflate(R.layout.viewuser, null,
				false);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.inquire_user_title);
		builder.setView(vAlertDialog);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		TextView viewUserNo = (TextView) vAlertDialog
				.findViewById(R.id.user_no_output);
		TextView viewUserName = (TextView) vAlertDialog
				.findViewById(R.id.user_name_output);
		TextView viewUserNickname = (TextView) vAlertDialog
				.findViewById(R.id.user_nickname_output);
		TextView viewGender = (TextView) vAlertDialog
				.findViewById(R.id.gender_output);
		TextView viewPhone = (TextView) vAlertDialog
				.findViewById(R.id.phone_output);
		TextView viewEmail = (TextView) vAlertDialog
				.findViewById(R.id.email_output);
		TextView viewMarried = (TextView) vAlertDialog
				.findViewById(R.id.married_output);
		TextView viewRole = (TextView) vAlertDialog
				.findViewById(R.id.role_output);

		UserBO userBO = getUserById(id);

		viewUserNo.setText(userBO.getUserNo());
		viewUserName.setText(userBO.getName());
		viewUserNickname.setText(userBO.getNickName());

		if (userBO.getGender() != null) {
			if (userBO.getGender().equalsIgnoreCase("M")) {
				viewGender.setText(getText(R.string.male));
			} else if (userBO.getGender().equalsIgnoreCase("F")) {
				viewGender.setText(getText(R.string.female));
			}
		}
		viewPhone.setText(userBO.getPhone());
		viewEmail.setText(userBO.getEmail());

		if (userBO.getMarried().equalsIgnoreCase("Y")) {
			viewMarried.setText(getText(R.string.married_yes));
		} else {
			viewMarried.setText(getText(R.string.married_no));
		}
		if (userBO.getRole().equalsIgnoreCase("A")) {
			viewRole.setText(getText(R.string.admin));
		} else if (userBO.getRole().equalsIgnoreCase("U")) {
			viewRole.setText(getText(R.string.user));
		}

		builder.show();
	}

	@SuppressWarnings("unused")
	private void modifyInfo(final String id) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(InquireUser.LAYOUT_INFLATER_SERVICE);

		final View vAlertDialog = inflater.inflate(R.layout.modifyuser, null,
				false);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.modify_user_title);
		builder.setView(vAlertDialog);

		userNoModify = (TextView) vAlertDialog.findViewById(R.id.user_no_input);
		passwordModify = (EditText) vAlertDialog
				.findViewById(R.id.tv_password_input);
		confirmPasswordModify = (EditText) vAlertDialog
				.findViewById(R.id.confrim_password_input);
		nameModify = (EditText) vAlertDialog.findViewById(R.id.user_name_input);
		nickNameModify = (EditText) vAlertDialog
				.findViewById(R.id.user_nickname_input);
		maleButtonModify = (RadioButton) vAlertDialog.findViewById(R.id.male);
		femaleButtonModify = (RadioButton) vAlertDialog
				.findViewById(R.id.female);
		phoneModify = (EditText) vAlertDialog.findViewById(R.id.phone_input);
		emailModify = (EditText) vAlertDialog.findViewById(R.id.email_input);
		marriedModify = (CheckBox) vAlertDialog
				.findViewById(R.id.married_check_box);
		roleModify = (Spinner) vAlertDialog.findViewById(R.id.role_spinner);
		initRoleSpinner();

		setValuesForModify(id);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (validate()) {
							if (modifyUser(id) < 0) {
								new BookkeepingAlertDialog().show(
										InquireUser.this, R.string.add_failed);
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

	private void initRoleSpinner() {
		String[] values = new String[] { "U", "A" };
		String[] texts = new String[] {
				getResources().getString(R.string.user),
				getResources().getString(R.string.admin) };

		roleModify.setAdapter(DropdownAdapter.getAdapter(InquireUser.this,
				values, texts));
	}

	private void setValuesForModify(String id) {
		UserBO userBO = getUserById(id);

		userNoModify.setText(userBO.getUserNo());
		passwordModify.setText(userBO.getPassword());
		confirmPasswordModify.setText(userBO.getPassword());
		nameModify.setText(userBO.getName());
		nickNameModify.setText(userBO.getNickName());

		if (userBO.getGender() != null) {
			if (userBO.getGender().equalsIgnoreCase("M")) {
				maleButtonModify.setChecked(true);
			} else if (userBO.getGender().equalsIgnoreCase("F")) {
				femaleButtonModify.setChecked(true);
			}
		}
		phoneModify.setText(userBO.getPhone());
		emailModify.setText(userBO.getEmail());

		if (userBO.getMarried().equalsIgnoreCase("Y"))
			marriedModify.setChecked(true);

		List<SpinnerBO> roleList = DropdownAdapter.getList(new String[] { "U",
				"A" }, new String[] { getResources().getString(R.string.user),
				getResources().getString(R.string.admin) });
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).toString().equalsIgnoreCase(userBO.getRole())) {
				roleModify.setSelection(i);
			}
		}
	}

	private UserBO getUserById(String id) {
		UserBO userBO = new UserBO();

		Uri uri = ContentUris.withAppendedId(User.CONTENT_URI,
				Long.parseLong(id));

		Cursor c = getContentResolver().query(uri, null, null, null, null);

		if (c.moveToNext()) {
			userBO.setUserNo(c.getString(1));
			userBO.setPassword(c.getString(2));
			userBO.setName(c.getString(3));
			userBO.setNickName(c.getString(4));
			userBO.setGender(c.getString(5));
			userBO.setPhone(c.getString(6));
			userBO.setEmail(c.getString(7));
			userBO.setMarried(c.getString(8));
			userBO.setRole(c.getString(9));
		}

		startManagingCursor(c);

		return userBO;
	}

	private boolean validate() {
		UserBO userBO = getUserInfoForModify();

		BookkeepingAlertDialog bad = new BookkeepingAlertDialog();

		if (StringUtil.isNull(userBO.getUserNo())) {
			bad.show(InquireUser.this, R.string.user_no_null);
			return false;
		} else if (!StringUtil.isNull(userBO.getPassword())) {
			if (StringUtil.isNull(userBO.getConfirmPassword())) {
				bad.show(InquireUser.this, R.string.password_no_null);
				return false;
			} else if (!userBO.getPassword()
					.equals(userBO.getConfirmPassword())) {
				bad.show(InquireUser.this, R.string.password_disaccord);
				return false;
			}
		}

		return true;
	}

	private int modifyUser(String id) {
		Uri uri = ContentUris.withAppendedId(User.CONTENT_URI,
				Long.parseLong(id));
		ContentValues values = new ContentValues();

		UserBO userBO = getUserInfoForModify();

		if (!StringUtil.isNull(userBO.getPassword())) {
			values.put(User.PASSWORD, userBO.getPassword());
		}
		values.put(User.NAME, userBO.getName());
		values.put(User.NICKNAME, userBO.getNickName());
		values.put(User.GENDER, userBO.getGender());
		values.put(User.PHONE, userBO.getPhone());
		values.put(User.EMAIL, userBO.getEmail());
		values.put(User.ISMARRIED, userBO.getMarried());
		values.put(User.ROLE, userBO.getRole());

		return getContentResolver().update(uri, values, null, null);
	}

	@SuppressWarnings("unused")
	private void delete(String id) {
		if (isAutoLoginUser(id)) {
			BookkeepingSharePreference
					.clearAutoLoginInfoFromSP(getApplicationContext());
		}
		Uri uri = ContentUris.withAppendedId(User.CONTENT_URI,
				Long.parseLong(id));

		getContentResolver().delete(uri, null, null);
	}

	private boolean isAutoLoginUser(String id) {
		UserBO userBO = BookkeepingSharePreference
				.getAutoLoginInfoFromSP(getApplicationContext());

		return userBO == null ? false : userBO.getId().equals(id) ? true
				: false;
	}

	private UserBO getUserInfoForModify() {
		UserBO userBO = new UserBO();

		userBO.setUserNo(userNoModify.getText().toString());
		userBO.setPassword(passwordModify.getText().toString());
		userBO.setConfirmPassword(confirmPasswordModify.getText().toString());
		userBO.setName(nameModify.getText().toString());
		userBO.setNickName(nickNameModify.getText().toString());

		if (maleButtonModify.isChecked()) {
			userBO.setGender("M");
		}
		if (femaleButtonModify.isChecked()) {
			userBO.setGender("F");
		}
		userBO.setPhone(phoneModify.getText().toString());
		userBO.setEmail(emailModify.getText().toString());

		if (marriedModify.isChecked()) {
			userBO.setMarried("Y");
		} else {
			userBO.setMarried("N");
		}
		userBO.setRole(((SpinnerBO) roleModify.getSelectedItem()).getValue());

		return userBO;
	}
}
