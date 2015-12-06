/* AddConsumerItem.java
 * Created on 2011-8-13
 */
package org.android.bookkeeping.activity.backend;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.util.DBUtil;
import org.android.bookkeeping.util.StringUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddConsumer extends Activity {
	private EditText nameInput;
	private EditText relationshipInput;
	private Button backToConsumer;
	private Button yesBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addconsumer);

		nameInput = (EditText) findViewById(R.id.consumer_name_input);
		relationshipInput = (EditText) findViewById(R.id.consumer_relationship_input);
		backToConsumer = (Button) findViewById(R.id.back_to_consumer_list);
		yesBtn = (Button) findViewById(R.id.consumer_confirm_yes);

		backToConsumer.setOnClickListener(backToConsumerListener);
		yesBtn.setOnClickListener(yesButtonClickListener);
	}

	private OnClickListener backToConsumerListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			backToConsumerList();
		}
	};

	private void backToConsumerList() {
		Intent intent = new Intent();
		intent.setClass(AddConsumer.this, Consumer.class);
		startActivity(intent);
	}

	private OnClickListener yesButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = nameInput.getText().toString();
			String relationship = relationshipInput.getText().toString();

			if (StringUtil.isNull(name)) {
				new BookkeepingAlertDialog().show(AddConsumer.this,
						R.string.consumer_name_no_null);
			} else if (StringUtil.isNull(relationship)) {
				new BookkeepingAlertDialog().show(AddConsumer.this,
						R.string.relationship_no_null);
			} else {
				if (addConsumer(name, relationship) == -1) {
					new BookkeepingAlertDialog().show(AddConsumer.this,
							R.string.add_failed);
				} else {
					new BookkeepingAlertDialog().show(AddConsumer.this,
							R.string.add_success, new int[] {
									R.string.add_more,
									R.string.back_to_consumer },
							new String[] { "clear" },
							new String[] { "backToConsumerList" });
				}
			}
		}
	};
	
	private long addConsumer(String name, String relationship) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("relationship", relationship);

		DBHelper helper = new DBHelper(getApplicationContext());

		return helper.insert(DBUtil.CONSUMER_TBL_NAME, values);
	}

	@SuppressWarnings("unused")
	private void clear() {
		nameInput.setText("");
		relationshipInput.setText("");
		nameInput.requestFocus();
	}

}
