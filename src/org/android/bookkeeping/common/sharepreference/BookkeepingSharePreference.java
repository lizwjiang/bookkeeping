/* BookkeepingSharePreference.java
* Created on 2011-9-2
*/
package org.android.bookkeeping.common.sharepreference;

import org.android.bookkeeping.bo.UserBO;
import org.android.bookkeeping.util.Constants;
import org.android.bookkeeping.util.StringUtil;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * Add one sentence class summary here.
 * Add class description here.
 *
 * @author Johnson_Jiang01
 * @version 1.0, 2011-9-2
 */
public class BookkeepingSharePreference {
	
	@SuppressWarnings("static-access")
	public static void saveCurrentUserInfoForAutoLogin(Context context, UserBO userBO){
		SharedPreferences.Editor editor = context.getSharedPreferences(
				Constants.DEFAULT_USER, context.MODE_WORLD_WRITEABLE).edit();
		editor.putString(Constants.USER_NO, userBO.getUserNo());
		editor.putString(Constants.USER_ROLE, userBO.getRole());

		editor.commit();
	}
	
	@SuppressWarnings("static-access")
	public static UserBO getAutoLoginInfoFromSP(Context context){
		UserBO userBO = null;
		SharedPreferences pre = context.getSharedPreferences(Constants.DEFAULT_USER, context.MODE_WORLD_READABLE);
		String userNo = pre.getString(Constants.USER_NO, "");
		if(!StringUtil.isNull(userNo)){
			userBO = new UserBO();
			userBO.setUserNo(userNo);
			userBO.setRole(pre.getString(Constants.USER_ROLE, ""));
		}
		return userBO;
	}
	
	@SuppressWarnings("static-access")
	public static void clearAutoLoginInfoFromSP(Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences(
				Constants.DEFAULT_USER, context.MODE_WORLD_WRITEABLE).edit();
		editor.remove(Constants.USER_NO);
		editor.remove(Constants.USER_ROLE);
		editor.commit();
	}
	
	@SuppressWarnings("static-access")
	public static void clearAll(Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences(
				Constants.DEFAULT_USER, context.MODE_WORLD_WRITEABLE).edit();
		editor.clear();
		editor.commit();
	}
}
