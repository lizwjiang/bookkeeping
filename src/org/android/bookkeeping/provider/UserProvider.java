/* UserProvider.java
 * Created on 2011-9-3
 */
package org.android.bookkeeping.provider;

import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.provider.Users.User;
import org.android.bookkeeping.util.DBUtil;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class UserProvider extends ContentProvider {
	private DBHelper dbHelper;
	private static final UriMatcher sUriMatcher;
	private static final int USER = 1;
	private static final int USER_ID = 2;
	private String[] columns;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Users.AUTHORITY, "user", USER);
		sUriMatcher.addURI(Users.AUTHORITY, "user/#", USER_ID);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		columns = new String[] { User._ID, User.USERNO, User.PASSWORD,
				User.NAME, User.NICKNAME, User.GENDER, User.PHONE, User.EMAIL,
				User.ISMARRIED, User.ROLE };
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (sUriMatcher.match(uri)) {
		case USER:
			return dbHelper.query(DBUtil.USER_TBL_NAME,
					projection != null ? projection : columns, selection,
					selectionArgs, sortOrder);
		case USER_ID:
			return dbHelper.query(DBUtil.USER_TBL_NAME,
					projection != null ? projection : columns,
					User._ID + "= ?",
					new String[] { uri.getPathSegments().get(1) }, sortOrder);
		default:
			throw new IllegalArgumentException("Uri is invalid:" + uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case USER:
			return User.CONTENT_TYPE;
		case USER_ID:
			return User.CONTENT_ITEM_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = dbHelper.insert(DBUtil.USER_TBL_NAME, values);
		Uri empUri = null;
		if (rowId > 0) {
			empUri = ContentUris.withAppendedId(User.CONTENT_URI, rowId);
		}
		return empUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count;
		switch (sUriMatcher.match(uri)) {
		case USER_ID:
			count = dbHelper.del(DBUtil.USER_TBL_NAME,
					Integer.parseInt(uri.getPathSegments().get(1)));
			break;
		default:
			throw new IllegalArgumentException("Uri is invalid:" + uri);
		}

		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count;
		switch (sUriMatcher.match(uri)) {
		case USER_ID:
			count = dbHelper.update(DBUtil.USER_TBL_NAME, values, User._ID
					+ "= ?", new String[] { uri.getPathSegments().get(1) });
			break;
		default:
			throw new IllegalArgumentException("Uri is invalid:" + uri);
		}
		return count;
	}

}
