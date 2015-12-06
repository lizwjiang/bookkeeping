/* ConsumerItem.java
 * Created on 2011-8-13
 */
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

public class Consumer extends Activity {

	private ListView listView;
	private Button addConsumer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumermanager);

		addConsumer = (Button) findViewById(R.id.add_consumer);
		addConsumer.setOnClickListener(addConsumerListener);

		showList();
	}

	public void showList() {
		List<HashMap<String, String>> allConsumer;
		allConsumer = getAllConsumers();

		listView = (ListView) findViewById(R.id.consumer_listview);
		ListViewAdapter lva = new ListViewAdapter(this, R.layout.consumerlist,
				allConsumer);

		int[] listItems = new int[] { R.id.consumer_name, R.id.relationship };
		int[] operaItems = new int[] { 0, 0, R.id.consumer_delete };

		lva.allowDelete(true).allowModify(false).allowView(false)
				.setListViewItems(listItems).setListOperationItems(operaItems)
				.setCustomizedActivity(this);

		listView.setAdapter(lva);
	}

	public void delete(String id) {
		DBHelper helper = new DBHelper(getApplicationContext());

		helper.del(DBUtil.CONSUMER_TBL_NAME, Integer.parseInt(id));
	}

	private List<HashMap<String, String>> getAllConsumers() {
		List<HashMap<String, String>> consumerList = new ArrayList<HashMap<String, String>>();

		DBHelper helper = new DBHelper(getApplicationContext());
		Cursor c = helper.query(DBUtil.CONSUMER_TBL_NAME,
				new String[] { "id, name, relationship" });

		startManagingCursor(c);

		while (c.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", c.getString(0));
			map.put(String.valueOf(R.id.consumer_name), c.getString(1));
			map.put(String.valueOf(R.id.relationship), c.getString(2));
			consumerList.add(map);
		}

		helper.close();

		return consumerList;
	}

	private OnClickListener addConsumerListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Consumer.this, AddConsumer.class);
			startActivity(intent);
		}
	};

}
