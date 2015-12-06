package org.android.bookkeeping.activity.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.ListViewAdapter;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ItemMaintenance extends Activity {
	private ListView listView;
    private Button addItemBtn;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

		listView = (ListView) findViewById(R.id.item_listView);
        addItemBtn = (Button)findViewById(R.id.add_item_btn);
        addItemBtn.setOnClickListener(addItemOnBtnClickListener);
        
		showList();
    }

    private OnClickListener addItemOnBtnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent addItemIntent = new Intent();
			addItemIntent.setClass(ItemMaintenance.this, AddItem.class);
			startActivity(addItemIntent);
		}
    	
    };
    
    private void showList() {
		List<HashMap<String, String>> allItems = getAllItems();
		int[] listItems = new int[] { R.id.item_name};
		int[] operaItems = new int[] { 0, 0, R.id.item_delete_btn };

		ListViewAdapter lva = new ListViewAdapter(this, R.layout.itemmaintenancelist,
				allItems);
		lva.allowDelete(true).allowModify(false).allowView(false)
				.setListViewItems(listItems).setListOperationItems(operaItems)
				.setCustomizedActivity(this);

		listView.setAdapter(lva);
	}
    
    
	public void delete(String id) {
		DBHelper helper = new DBHelper(getApplicationContext());

		helper.del(DBUtil.ITEM_TBL_NAME, Integer.parseInt(id));
	}

	
	private List<HashMap<String, String>> getAllItems() {
		List<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();

		DBHelper helper = new DBHelper(getApplicationContext());
		Cursor c = helper.query(DBUtil.ITEM_TBL_NAME,
				new String[] { "id, name" });

		startManagingCursor(c);
		
		while (c.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", c.getString(0));
			map.put(String.valueOf(R.id.item_name), c.getString(1));
			itemList.add(map);
		}

		helper.close();

		return itemList;
	}
}


