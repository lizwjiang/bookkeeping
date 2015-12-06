/* DropdownAdapter.java
* Created on 2011-9-9
*/
package org.android.bookkeeping.activity;

import java.util.ArrayList;
import java.util.List;

import org.android.bookkeeping.bo.SpinnerBO;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

/**
 * 
* Add one sentence class summary here.
* Add class description here.
*
* @author Johnson_Jiang01
* @version 1.0, 2011-9-9
 */
public class DropdownAdapter {
	public static SpinnerAdapter getAdapter(Context context, String[] values, String[] texts){
		if (values == null || values.length == 0 || texts == null
				|| texts.length == 0 || values.length != texts.length) {
			throw new IllegalArgumentException(
					"Illegal argument values or texts");
		}

		ArrayAdapter<SpinnerBO> adapter = new ArrayAdapter<SpinnerBO>(context,
				android.R.layout.simple_spinner_item, getList(values, texts));
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
	
	public static List<SpinnerBO> getList(String[] values, String[] texts){
		List<SpinnerBO> list = new ArrayList<SpinnerBO>();
		for(int i = 0; i < values.length; i++){
			SpinnerBO spinnerBO = new SpinnerBO(values[i], texts[i]);
			list.add(spinnerBO);
		}
		return list;
	}
	
}
