/* DateUtil.java
 * Created on 2011-8-15
 */
package org.android.bookkeeping.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
* Add one sentence class summary here.
* Add class description here.
*
* @author Johnson_Jiang01
* @version 1.0, 2011-8-15
 */
public class DateUtil {

	public static String getNowDate() {
		return getNowDateByFormat("yyyy-MM-dd");
	}

	public static String getNowDateByFormat(String dateFormat){
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(new Date());
	}
	
	public static String getDateString(int year, int month, int day) {
		return String.format("%04d-%02d-%02d", year, month, day);
	}
}