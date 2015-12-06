/* StringUtil.java
* Created on 2011-8-9
*/
package org.android.bookkeeping.util;

import java.util.regex.Pattern;

/**
 * 
* Add one sentence class summary here.
* Add class description here.
*
* @author Johnson_Jiang01
* @version 1.0, 2011-8-9
 */
public class StringUtil {
	public static boolean isNull(String str){
		if(str == null || str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean isDigit(String str) {
		if (str == null || str.trim().length() <= 0)
			return false;
		return Pattern.matches("^\\d+$", str);
	}
}
