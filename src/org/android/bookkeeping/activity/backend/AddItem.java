package org.android.bookkeeping.activity.backend;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddItem extends Activity{
	private EditText itemName;
	private Button submitBtn;
	private Button cancelBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		
		itemName = (EditText)findViewById(R.id.add_item_editText);
		submitBtn = (Button)findViewById(R.id.add_item_submit);
		cancelBtn = (Button)findViewById(R.id.add_item_cancel);
		
		submitBtn.setOnClickListener(submitBtnOnClickListener);
		cancelBtn.setOnClickListener(cancelBtnOnClickListener);
	}
	
	private OnClickListener submitBtnOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			String inputItemName = itemName.getText().toString();
			if (StringUtil.isNull(inputItemName)) {
				new BookkeepingAlertDialog().show(AddItem.this,
						R.string.item_name_no_null);
			}else if(isDuplicatedItemName(inputItemName)){
				new BookkeepingAlertDialog().show(AddItem.this,
						R.string.item_name_exist);
			}else{
				if(addItem(inputItemName) != -1){
					new BookkeepingAlertDialog().show(AddItem.this,
						new int[] { 1, 1 }, R.string.add_success,
						new int[] { R.string.add_more,
								R.string.back_to_itemMaintenance },
						new String[] {"cleanInputs"}, new String[]{"backToItemList"});
				}else{
					new BookkeepingAlertDialog().show(AddItem.this,
							R.string.add_failed);
				}
			}
			
		}   	
    };
	
	public boolean isDuplicatedItemName(String name){
		boolean validateResult = false;
		DBHelper helper = new DBHelper(getApplicationContext());
		Cursor c = helper.query(DBUtil.ITEM_TBL_NAME,
				new String[] { "id, name" }, "name= ?", new String[]{name});
		startManagingCursor(c);
		
		if (c.moveToNext()) {
			validateResult = true;
		}
		helper.close();

		return validateResult;
	}
    
	private OnClickListener cancelBtnOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(AddItem.this, ItemMaintenance.class);
			startActivity(intent);
			finish();
		}  	
    };
	
	private long addItem(String inputItemName) {
		DBHelper helper = new DBHelper(getApplicationContext());
		
		ContentValues values = new ContentValues();
		values.put("name", inputItemName);
		
		return helper.insert(DBUtil.ITEM_TBL_NAME, values);
	}

	@SuppressWarnings("unused")
	private void cleanInputs() {
		itemName.setText("");
		itemName.requestFocus();
	}
	
	@SuppressWarnings("unused")
	private void backToItemList() {
		Intent intent = new Intent();
		intent.setClass(AddItem.this, ItemMaintenance.class);
		startActivity(intent);
	}
	
}
