package org.android.bookkeeping.activity.account;

import org.android.bookkeeping.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class InquireBookkeeping extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookkeepingmanager);
	
		TabHost tabHost = getTabHost();
		Resources res = getResources();
		
		Intent dailyInquiryIntent = new Intent(this, DailyBookkeepingInquiry.class);
		TabHost.TabSpec dailyInquirySpec = tabHost.newTabSpec(res.getString(R.string.daily_inquiry_title));
		dailyInquirySpec.setIndicator(getText(R.string.daily_inquiry_title), res.getDrawable(android.R.drawable.alert_dark_frame));
		dailyInquirySpec.setContent(dailyInquiryIntent);
		tabHost.addTab(dailyInquirySpec);
		
		Intent historyInquiryIntent = new Intent(this, HistoryBookkeepingInquiry.class);
		TabHost.TabSpec historyInquirySpec = tabHost.newTabSpec(res.getString(R.string.history_inquiry_title));
		historyInquirySpec.setIndicator(getText(R.string.history_inquiry_title), res.getDrawable(android.R.drawable.alert_dark_frame));
		historyInquirySpec.setContent(historyInquiryIntent);
		tabHost.addTab(historyInquirySpec);
	}
}
