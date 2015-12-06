/* AddUser.java
 * Created on 2011-8-27
 */
package org.android.bookkeeping.activity.backend;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.DropdownAdapter;
import org.android.bookkeeping.activity.Main;
import org.android.bookkeeping.bo.SpinnerBO;
import org.android.bookkeeping.bo.UserBO;
import org.android.bookkeeping.provider.Users.User;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class AddUser extends Activity {
	private EditText userNo, password, confirmPassword, name, nickName, phone,
			email;
	private RadioButton maleButton, femaleButton;
	private CheckBox married;
	private Spinner role;
	private Button ok, backToMainPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adduser);

		userNo = (EditText) findViewById(R.id.user_no_input);
		password = (EditText) findViewById(R.id.tv_password_input);
		confirmPassword = (EditText) findViewById(R.id.confrim_password_input);
		name = (EditText) findViewById(R.id.user_name_input);
		nickName = (EditText) findViewById(R.id.user_nickname_input);
		maleButton = (RadioButton) findViewById(R.id.male);
		femaleButton = (RadioButton) findViewById(R.id.female);
		phone = (EditText) findViewById(R.id.phone_input);
		email = (EditText) findViewById(R.id.email_input);
		married = (CheckBox) findViewById(R.id.married_check_box);
		role = (Spinner) findViewById(R.id.role_spinner);
		ok = (Button) findViewById(R.id.add_user_submit);
		backToMainPage = (Button) findViewById(R.id.add_user_backtomain);

		initRoleSpinner();
		ok.setOnClickListener(okOnClickListener);
		backToMainPage.setOnClickListener(backToMainPageOnClickListener);
	}

	private void initRoleSpinner() {
		String[] values = new String[] { "U", "A" };
		String[] texts = new String[] {
				getResources().getString(R.string.user),
				getResources().getString(R.string.admin) };

		role.setAdapter(DropdownAdapter.getAdapter(AddUser.this, values, texts));
	}

	private OnClickListener okOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (validate()) {
				if (addUser() == null) {
					new BookkeepingAlertDialog().show(AddUser.this,
							R.string.add_failed);
				} else {
					new BookkeepingAlertDialog().show(AddUser.this, new int[] {
							1, 0 }, R.string.add_success, new int[] {
							R.string.ok, 0 }, new String[] { "clear" });
				}
			}
		}
	};

	private boolean validate() {
		UserBO userBO = getUserInfo();

		BookkeepingAlertDialog bad = new BookkeepingAlertDialog();

		if (StringUtil.isNull(userBO.getUserNo())) {
			bad.show(AddUser.this, R.string.user_no_null);
			return false;
		} else if (StringUtil.isNull(userBO.getPassword())) {
			bad.show(AddUser.this, R.string.password_no_null);
			return false;
		} else if (StringUtil.isNull(userBO.getConfirmPassword())) {
			bad.show(AddUser.this, R.string.password_no_null);
			return false;
		} else if (!userBO.getPassword().equals(userBO.getConfirmPassword())) {
			bad.show(AddUser.this, R.string.password_disaccord);
			return false;
		} else if (isDuplicatedUserNo()) {
			bad.show(AddUser.this, R.string.user_is_exist);
			return false;
		}

		return true;
	}

	private boolean isDuplicatedUserNo() {
		UserBO userBO = getUserInfo();

		Cursor c = getContentResolver().query(User.CONTENT_URI,
				new String[] { User._ID }, "userno = ?",
				new String[] { userBO.getUserNo() }, null);
		startManagingCursor(c);

		return c.getCount() == 0 ? false : true;
	}

	private Uri addUser() {
		UserBO userBO = getUserInfo();

		Uri uri = User.CONTENT_URI;

		ContentValues values = new ContentValues();
		values.put(User.USERNO, userBO.getUserNo());
		values.put(User.PASSWORD, userBO.getPassword());
		values.put(User.NAME, userBO.getName());
		values.put(User.NICKNAME, userBO.getNickName());
		values.put(User.GENDER, userBO.getGender());
		values.put(User.PHONE, userBO.getPhone());
		values.put(User.EMAIL, userBO.getEmail());
		values.put(User.ISMARRIED, userBO.getMarried());
		values.put(User.ROLE, userBO.getRole());

		return getContentResolver().insert(uri, values);
	}

	private UserBO getUserInfo() {
		UserBO userBO = new UserBO();

		userBO.setUserNo(userNo.getText().toString());
		userBO.setPassword(password.getText().toString());
		userBO.setConfirmPassword(confirmPassword.getText().toString());
		userBO.setName(name.getText().toString());
		userBO.setNickName(nickName.getText().toString());

		if (maleButton.isChecked())
			userBO.setGender("M");
		if (femaleButton.isChecked())
			userBO.setGender("F");

		userBO.setPhone(phone.getText().toString());
		userBO.setEmail(email.getText().toString());

		if (married.isChecked())
			userBO.setMarried("Y");
		else
			userBO.setMarried("N");

		userBO.setRole(((SpinnerBO) role.getSelectedItem()).getValue());

		return userBO;
	}

	@SuppressWarnings("unused")
	private void clear() {
		userNo.setText("");
		password.setText("");
		confirmPassword.setText("");
		name.setText("");
		nickName.setText("");
		maleButton.setChecked(false);
		femaleButton.setChecked(false);
		phone.setText("");
		email.setText("");
		married.setChecked(false);
		role.setSelection(0);
		userNo.requestFocus();
	}

	private OnClickListener backToMainPageOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(AddUser.this, Main.class);
			startActivity(intent);
		}
	};
}
