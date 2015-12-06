/* ListViewAdapter.java
* Created on 2011-8-22
*/
package org.android.bookkeeping.activity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.android.bookkeeping.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private Integer resource;
	private List<HashMap<String, String>> value;
	private boolean allowView, allowModify, allowDelete;
	private Activity customizedActivity;
	
	private int[] listViewItems, listOperationItems;
	
	public ListViewAdapter(Context context, Integer resource,
			List<HashMap<String, String>> value) {
		this.context = context;
		this.resource = resource;
		this.value = value;
	}
	
	public void setCustomizedActivity(Activity activity ){
		this.customizedActivity = activity;
	}
	
	public ListViewAdapter setListViewItems(int[] items){
		this.listViewItems = items;
		return this;
	}
	
	public ListViewAdapter setListOperationItems(int[] items){
		this.listOperationItems = items;
		return this;
	}
	
	public ListViewAdapter allowView(boolean isAllow){
		allowView = isAllow;
		return this;
	}
	
	public ListViewAdapter allowModify(boolean isAllow){
		allowModify = isAllow;
		return this;
	}
	
	public ListViewAdapter allowDelete(boolean isAllow){
		allowDelete = isAllow;
		return this;
	}
	
	public void allowAll(boolean isAllow){
		allowView = isAllow;
		allowModify = isAllow;
		allowDelete = isAllow;
	}
	
	@Override
	public int getCount() {
		return value.size();
	}

	@Override
	public Object getItem(int position) {
		return value.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String id = value.get(position).get("id");
		View view = View.inflate(context, resource, null);

		if(listViewItems == null) throw new IllegalArgumentException("arguments should be there fro list view");
		
		for(int i = 0; i< listViewItems.length; i++){
			TextView tv = (TextView) view.findViewById(listViewItems[i]);
			tv.setText(value.get(position).get(String.valueOf(listViewItems[i])));
		}
		//here when setting button, need to create an array which size is 3. the first one should be add
		if(listOperationItems != null){// for view info
			if(listOperationItems[0] > 0 && allowView == true){
				Button b = (Button)view.findViewById(listOperationItems[0]);
				b.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						try {
							Method method = customizedActivity.getClass().getDeclaredMethod("viewInfo", String.class);
							method.setAccessible(true);
							method.invoke(customizedActivity, id);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
			}
			if(listOperationItems[1] > 0 && allowModify == true){
				Button b = (Button)view.findViewById(listOperationItems[1]);
				b.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						try{
							Method method = customizedActivity.getClass().getDeclaredMethod("modifyInfo", String.class);
							method.setAccessible(true);
							method.invoke(customizedActivity, id);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				});
			}
			if(listOperationItems[2] > 0 && allowDelete == true){
				Button b = (Button)view.findViewById(listOperationItems[2]);
				b.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						confirmDeletionInfo(id);
					}
				});
			}
		}

		return view;
	}

	public void confirmDeletionInfo(final String id) {
		Builder builder = new Builder(customizedActivity);
		builder.setTitle(R.string.tooltip)
				.setMessage(R.string.confirm_deletion);
		builder.setPositiveButton(R.string.confirm_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try{
							Method method = customizedActivity.getClass().getDeclaredMethod("delete", String.class);
							Method method1 = customizedActivity.getClass().getDeclaredMethod("showList");
							method.setAccessible(true);
							method1.setAccessible(true);
							method.invoke(customizedActivity, id);
							method1.invoke(customizedActivity);
						} catch (Exception e) {
							e.printStackTrace();
						} 						
					}
				});
		builder.setNegativeButton(R.string.confirm_no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.show();
	}
	
}
