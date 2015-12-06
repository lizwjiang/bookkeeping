/* SyncDataException.java
* Created on 2011-9-21
*/
package org.android.bookkeeping.exception;
public class SyncDataException extends BookkeepingRuntimeException {
	private static final long serialVersionUID = -2335648697823981303L;

	public SyncDataException() {
		super();
	}

	public SyncDataException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SyncDataException(String detailMessage) {
		super(detailMessage);
	}

	public SyncDataException(Throwable throwable) {
		super(throwable);
	}

}
