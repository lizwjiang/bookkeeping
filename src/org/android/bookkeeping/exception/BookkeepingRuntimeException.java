/* BookkeepingRuntimeException.java
* Created on 2011-9-20
*/
package org.android.bookkeeping.exception;
public class BookkeepingRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -5537606659222922945L;

	public BookkeepingRuntimeException() {
		super();
	}

	public BookkeepingRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BookkeepingRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public BookkeepingRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
