/* SpinnerBO.java
 * Created on 2011-8-30
 */
package org.android.bookkeeping.bo;

/**
 * 
 * Add one sentence class summary here. Add class description here.
 * 
 * @author David_Chen
 * @version 1.0, 2011-8-30
 */
public class SpinnerBO {
	private String value;
	private String text;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		return getText();
	}

	public SpinnerBO(String value, String text) {
		this.value = value;
		this.text = text;
	}
}
