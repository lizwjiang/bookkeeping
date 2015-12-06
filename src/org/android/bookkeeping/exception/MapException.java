/* BookkeepingMapException.java
* Created on 2011-9-20
*/
package org.android.bookkeeping.exception;
public class MapException extends BookkeepingRuntimeException {
	private static final long serialVersionUID = -4547459770346087503L;

	public MapException(){
		super();
	}
	
	public MapException(String msg){
		super(msg);
	}
	
	public MapException(String msg, Throwable t){
		super(msg, t);
	}
	
	public MapException(Throwable t){
		super(t);
	}
}
