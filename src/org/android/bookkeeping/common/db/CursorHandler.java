/* CursorHandler.java
* Created on 2011-9-23
*/
package org.android.bookkeeping.common.db;

import android.database.Cursor;

public interface CursorHandler<T> {
	public T handle(Cursor c);
}
