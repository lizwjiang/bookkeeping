package org.android.bookkeeping.activity.backend;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.account.Bookkeeping;
import org.android.bookkeeping.activity.account.InquireBookkeeping;
import org.android.bookkeeping.activity.statistics.StatisticsQuery;
import org.android.bookkeeping.common.sharepreference.BookkeepingSharePreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LoginResult extends Activity {
	ImageButton autoLogin, globalParam, manageItem, manageConsumer, manageUser,
			bookkeeping, manageBookkeeping, statisticsReport, dataSync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginresult);
		
		autoLogin = (ImageButton)findViewById(R.id.cancel_auto_login_btn);
		globalParam = (ImageButton)findViewById(R.id.global_param_btn);
		manageItem = (ImageButton)findViewById(R.id.manage_item_btn);
		manageConsumer = (ImageButton)findViewById(R.id.manage_consumer_btn);
		manageUser = (ImageButton)findViewById(R.id.manage_user_btn);
		bookkeeping = (ImageButton)findViewById(R.id.bookkeeping_btn);
		manageBookkeeping = (ImageButton)findViewById(R.id.bookkeeping_inquiry_btn);
		statisticsReport = (ImageButton)findViewById(R.id.statistics_report_btn);
		dataSync = (ImageButton)findViewById(R.id.data_sync_btn);
		
		autoLogin.setOnClickListener(autoLoginBtnOnClickListener);
		globalParam.setOnClickListener(globalParamBtnOnClickListener);
		manageItem.setOnClickListener(manageItemBtnOnClickListener);
		manageConsumer.setOnClickListener(manageConsumerBtnOnClickListener);
		manageUser.setOnClickListener(manageUserBtnOnClickListener);
		bookkeeping.setOnClickListener(bookkeepingBtnOnClickListener);
		manageBookkeeping.setOnClickListener(manageBookkeepingBtnOnClickListener);
		statisticsReport.setOnClickListener(StatisticsReportBtnOnClickListener);
		dataSync.setOnClickListener(dataSyncBtnOnClickListener);
	}
	
	private OnClickListener autoLoginBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BookkeepingSharePreference.clearAutoLoginInfoFromSP(LoginResult.this);
			new BookkeepingAlertDialog().show(LoginResult.this, R.string.default_user_deleted_success);
		}
	};
	private OnClickListener globalParamBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(GlobalParameter.class);
		}
	};
	
	private OnClickListener manageItemBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(ItemMaintenance.class);
		}
	};

	private OnClickListener manageConsumerBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(Consumer.class);
		}
	};
	private OnClickListener manageUserBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(UserHost.class);
		}
	};
	private OnClickListener bookkeepingBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(Bookkeeping.class);
		}
	};
	private OnClickListener manageBookkeepingBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(InquireBookkeeping.class);
		}
	};
	private OnClickListener StatisticsReportBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(StatisticsQuery.class);
		}
	};
	private OnClickListener dataSyncBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			jumpFromLoginResultToAnotherActivity(DataSync.class);
		}
	};

	private void jumpFromLoginResultToAnotherActivity(Class another){
		jumpToAnotherActivity(LoginResult.this, another);
	}
	private void jumpToAnotherActivity(Context one, Class another){
		Intent intent = new Intent();	
		intent.setClass(one, another);
		startActivity(intent);
	}
}
