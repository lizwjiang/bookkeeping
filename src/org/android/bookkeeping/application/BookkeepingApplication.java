package org.android.bookkeeping.application;

import org.android.bookkeeping.bo.UserBO;

import android.app.Application;

public class BookkeepingApplication extends Application{
	private UserBO userBO;		
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(userBO == null){
			userBO = new UserBO();
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		userBO = null;
	}

	/**
	 * @return the loginBO
	 */
	public UserBO getUserBO() {
		return userBO;
	}

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}
	
}
