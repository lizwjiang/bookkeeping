package org.android.bookkeeping.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.backend.LoginResult;
import org.android.bookkeeping.application.BookkeepingApplication;
import org.android.bookkeeping.bo.UserBO;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.common.sharepreference.BookkeepingSharePreference;
import org.android.bookkeeping.util.Constants;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.StringUtil;
/**
 * 
 * This is the initial window of the whole bookkeeping system. it is actually a login window.
 * as soon as users input user no and password and click submit button, system will validate
 * the matching between user no and password. if no, error window will be popped up. 
 *
 * @author Johnson_Jiang01
 * @version 1.0, 2011-8-9
 */
public class Main extends Activity {
	BookkeepingApplication bookkeepingApp;
	EditText userNo, password;
	Button registerBtn, clearBtn;
	CheckBox autoLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		bookkeepingApp = (BookkeepingApplication)getApplication(); 
		
		UserBO userBO = BookkeepingSharePreference.getAutoLoginInfoFromSP(Main.this);
		if(userBO != null){// go to login result page directly
			startActivityFromMainToLoginResult(userBO);
		} else {
			userNo = (EditText) findViewById(R.id.user_no);
			password = (EditText) findViewById(R.id.password);
			autoLogin = (CheckBox)findViewById(R.id.auto_login);
			registerBtn = (Button) findViewById(R.id.register);
			clearBtn = (Button) findViewById(R.id.clear);

			registerBtn.setOnClickListener(registerListener);
			clearBtn.setOnClickListener(clearBtnOnClickListener);
		}
	}

	private OnClickListener registerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String userNoVal = userNo.getText().toString();
			if(StringUtil.isNull(userNoVal)){
				new BookkeepingAlertDialog().show(Main.this,
						R.string.user_no_null);
				userNo.requestFocus();
				
			}else{
				String passwordVal = password.getText().toString();
				if(StringUtil.isNull(passwordVal)){
					new BookkeepingAlertDialog().show(Main.this,
							R.string.password_no_null);
					password.requestFocus();
				}else{
					UserBO userBO = getUserByNo(userNoVal);
					if(userBO == null || !userBO.getPassword().trim().equals(passwordVal)){
						new BookkeepingAlertDialog().show(Main.this,
								R.string.password_no_nomatching);
					}else{
						if(autoLogin.isChecked()) 
							BookkeepingSharePreference.saveCurrentUserInfoForAutoLogin(Main.this, userBO);
						startActivityFromMainToLoginResult(userBO);
					}
				}
			}	
		}
	};
	
	private UserBO getUserByNo(String userNo){
		DBHelper helper = new DBHelper(getApplicationContext());
		Cursor c = helper.query(DBUtil.USER_TBL_NAME,
				new String[] { "id, password, role" }, "userno=?",
				new String[] { userNo });
		startManagingCursor(c);
		
		UserBO userBO = null;
		if(c.moveToNext()){
			userBO = new UserBO();
			userBO.setUserNo(userNo);
			userBO.setId(c.getString(0));
			userBO.setPassword(c.getString(1));
			userBO.setRole(c.getString(2));
		}

		helper.close(); 
		return userBO;
	}
	
	private void startActivityFromMainToLoginResult(UserBO userBO){
		bookkeepingApp.setUserBO(userBO);
		
		Bundle b = new Bundle();
		b.putString(Constants.USER_NO, userBO.getUserNo());
		b.putString(Constants.USER_ROLE, userBO.getRole());
		
		Intent intent = new Intent();					
		intent.putExtras(b);
		intent.setClass(Main.this, LoginResult.class);
		startActivity(intent);
	}
	
	private OnClickListener clearBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			userNo.setText("");
			password.setText("");
			userNo.requestFocus();
			autoLogin.setChecked(false);
		}
	};
	
}
