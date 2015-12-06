/* DBUtil.java
* Created on 2011-8-11
*/
package org.android.bookkeeping.util;
/**
 * 
* Add one sentence class summary here.
* Add class description here.
*
* @author Johnson_Jiang01
* @version 1.0, 2011-8-11
 */
public class DBUtil {
	public static final String DB_NAME = "bookkeeping.db";
	public static final int DB_VERSION = 1;
	public static final String USER_TBL_NAME = "USER";
	public static final String CONSUMER_TBL_NAME = "CONSUMER";
	public static final String ITEM_TBL_NAME = "ITEM";
	public static final String GLOBALPARAM_TBL_NAME = "GLOBALPARAMETER";
	public static final String BOOKKEEPING_TBL_NAME = "BOOKKEEPING";
	public static final String MONTHLY_TEMP_TBL_NAME = "MONTHLY_TEMP_DATA_TBL";
	
	public static final String USER_TBL_SQL = "CREATE TABLE IF NOT EXISTS user " +
			"(id integer primary key autoincrement, userno varchar(12), password varchar(12)," +
			"name varchar(20),nickname varchar(30), gender varchar(1), phone varchar(11),email varchar(60)," +
			"ismarried varchar(1),role varchar(1), rche_user_id varchar(12),rche_time varchar(20),lchg_user_id varchar(12)," +
			"lchg_time varchar(20))";
	
	public static final String CONSUMER_TBL_SQL = "CREATE TABLE IF NOT EXISTS consumer " +
			"(id integer primary key autoincrement, name varchar(20), relationship varchar(20), rche_user_id varchar(12)," +
			"rche_time varchar(20),lchg_user_id varchar(12),lchg_time varchar(20))";
	
	public static final String ITEM_TBL_SQL = "CREATE TABLE IF NOT EXISTS item " +
			"(id integer primary key autoincrement, name varchar(20), rche_user_id varchar(12)," +
			"rche_time varchar(20),lchg_user_id varchar(12),lchg_time varchar(20))";
	
	public static final String GLOBAL_PARAM_TBL_SQL = "CREATE TABLE IF NOT EXISTS GlobalParameter " +
			"(id integer primary key autoincrement, single_cosume_max_amount real, weekly_cosume_max_amount real, " +
			" monthly_cosume_max_amount real," +
			"rche_user_id varchar(12), rche_time varchar(20),lchg_user_id varchar(12),lchg_time varchar(20))";
	
	public static final String BOOKKEEPING_TBL_SQL = "CREATE TABLE IF NOT EXISTS bookkeeping " +
			"(id integer primary key autoincrement, consumption_date varchar(10), consumer varchar(20)," +
			"item varchar(40),amount real, reimbursement varchar(1), description varchar(40)," +
			"rche_user_id varchar(12),rche_time varchar(20),lchg_user_id varchar(12), lchg_time varchar(20))";	
	
	public static final String MONTHLY_TEMP_DATA_TBL_SQL = "CREATE TABLE IF NOT EXISTS MONTHLY_TEMP_DATA_TBL " +
			"(id integer primary key autoincrement, month varchar(2), consumer varchar(20)," +
			"amount real,rche_user_id varchar(12),rche_time varchar(20),lchg_user_id varchar(12),lchg_time varchar(20))";
	
	public static final String ADMIN_USER_INITIAL_DATA_SQL = "INSERT INTO USER " +
			"(id,userno, password,name,role,rche_user_id, rche_time,lchg_user_id,lchg_time) VALUES(" +
			"1,'admin','admin','Administrator','A','system','2011-08-16','system','2011-08-16')";
	
}
