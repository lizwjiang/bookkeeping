/* UserHost.java
 * Created on 2011-8-27
 */
package org.android.bookkeeping.activity.backend;

import org.android.bookkeeping.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class UserHost extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermanager);
	
		TabHost tabHost = getTabHost();
		Resources res = getResources();
		
		Intent addUserIntent = new Intent(this, AddUser.class);
		TabHost.TabSpec userItemSpec = tabHost.newTabSpec(res.getString(R.string.add_user_title));
		userItemSpec.setIndicator(getText(R.string.add_user_title), res.getDrawable(android.R.drawable.alert_light_frame));
		userItemSpec.setContent(addUserIntent);
		tabHost.addTab(userItemSpec);
		
		Intent inquireIntent = new Intent(this, InquireUser.class);
		TabHost.TabSpec addUserItemSpec = tabHost.newTabSpec(res.getString(R.string.inquire_user_title));
		addUserItemSpec.setIndicator(getText(R.string.inquire_user_title), res.getDrawable(android.R.drawable.alert_light_frame));
		addUserItemSpec.setContent(inquireIntent);
		tabHost.addTab(addUserItemSpec);
	}
}
