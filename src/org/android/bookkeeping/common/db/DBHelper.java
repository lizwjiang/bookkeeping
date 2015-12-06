package org.android.bookkeeping.common.db;

import org.android.bookkeeping.util.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context c) {
		super(c, DBUtil.DB_NAME, null, DBUtil.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBUtil.USER_TBL_SQL);
		db.execSQL(DBUtil.BOOKKEEPING_TBL_SQL);
		db.execSQL(DBUtil.CONSUMER_TBL_SQL);
		db.execSQL(DBUtil.GLOBAL_PARAM_TBL_SQL);
		db.execSQL(DBUtil.ITEM_TBL_SQL);
		db.execSQL(DBUtil.MONTHLY_TEMP_DATA_TBL_SQL);
	}

	public long insert(String tbl_name, ContentValues values) {
		return getWritableDatabase().insert(tbl_name, null, values);
	}

	public int update(String tbl_name, ContentValues values,
			String whereClause, String[] whereArgs) {
		return getWritableDatabase().update(tbl_name, values, whereClause, whereArgs);
	}

	public <T>T query(String tbl_name, CursorHandler<T> ch){
		Cursor c = getWritableDatabase().query(tbl_name, null, null, null,
				null, null, null);
		return ch.handle(c);
	}
	
	public Cursor query(String tbl_name) {
		Cursor c = getWritableDatabase().query(tbl_name, null, null, null,
				null, null, null);
		return c;
	}

	public Cursor query(String table, String[] columns) {
		return getWritableDatabase().query(table, columns, null, null, null,
				null, null);
	}
	
	public <T>T query(String tbl_name, String[] columns, CursorHandler<T> ch){
		Cursor c = getWritableDatabase().query(tbl_name, columns, null, null, null,
				null, null);
		return ch.handle(c);
	}
	public Cursor query(String table, String[] columns, String whereClause,
			String[] whereArgs) {
		return getWritableDatabase().query(table, columns, whereClause,
				whereArgs, null, null, null);
	}

	public Cursor query(String table, String[] columns, String whereClause,
			String[] whereArgs, String orderBy) {
		return getWritableDatabase().query(table, columns, whereClause,
				whereArgs, null, null, orderBy);
	}

	public Cursor query(String table, String[] columns, String whereClause,
			String[] whereArgs, String groupBy, String orderBy) {
		Cursor c = getWritableDatabase().query(table, columns, whereClause,
				whereArgs, groupBy, null, orderBy);
		return c;
	}

	public int del(String tbl_name, int id) {
		return getWritableDatabase().delete(tbl_name, "id=?",
				new String[] { String.valueOf(id) });
	}

	public void execSQL(String sql, Object[] args) {
		getWritableDatabase().execSQL(sql);
	}

	public void execSQL(String sql) {
		getWritableDatabase().execSQL(sql);
	}

	private void dropTable(SQLiteDatabase db, String taleName) {
		db.execSQL("DROP TABLE IF EXISTS " + taleName);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db, DBUtil.BOOKKEEPING_TBL_NAME);
		dropTable(db, DBUtil.CONSUMER_TBL_NAME);
		dropTable(db, DBUtil.GLOBALPARAM_TBL_NAME);
		dropTable(db, DBUtil.ITEM_TBL_NAME);
		dropTable(db, DBUtil.USER_TBL_NAME);
		dropTable(db, DBUtil.MONTHLY_TEMP_TBL_NAME);
		onCreate(db);
		db.execSQL(DBUtil.ADMIN_USER_INITIAL_DATA_SQL);
	}
}
