/* Users.java
 * Created on 2011-9-3
 */
package org.android.bookkeeping.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Users {
	public static final String AUTHORITY = "org.android.bookkeeping";

	public static final class User implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/user");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bookkeeping.user";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.bookkeeping.user";

		public static final String _ID = "id";
		public static final String USERNO = "userno";
		public static final String PASSWORD = "password";
		public static final String NAME = "name";
		public static final String NICKNAME = "nickname";
		public static final String GENDER = "gender";
		public static final String PHONE = "phone";
		public static final String EMAIL = "email";
		public static final String ISMARRIED = "ismarried";
		public static final String ROLE = "role";
	}
}
