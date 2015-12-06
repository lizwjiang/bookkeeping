/* DataSync.java
* Created on 2011-9-20
*/
package org.android.bookkeeping.activity.backend;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.android.bookkeeping.R;
import org.android.bookkeeping.activity.BookkeepingAlertDialog;
import org.android.bookkeeping.activity.DropdownAdapter;
import org.android.bookkeeping.bo.SpinnerBO;
import org.android.bookkeeping.common.db.DBHelper;
import org.android.bookkeeping.exception.SyncDataException;
import org.android.bookkeeping.util.Constants;
import org.android.bookkeeping.util.DBUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class DataSync extends Activity {
	String TAG = "Sync";
	Spinner syncItemSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datasync);
		
		syncItemSpinner = (Spinner)findViewById(R.id.sync_item);
		String[] values = getResources().getStringArray(R.array.sync_item_values);
		String[] texts = getResources().getStringArray(R.array.sync_item);
		syncItemSpinner.setAdapter(DropdownAdapter.getAdapter(DataSync.this,
				values, texts));
		
		Button startSync = (Button)findViewById(R.id.start_sync_btn);
		startSync.setOnClickListener(startSyncBtnOnClickListener);
	}

	private OnClickListener startSyncBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String syncItem = ((SpinnerBO)syncItemSpinner.getSelectedItem()).getValue();
			
			if(syncItem.equals("all")){
				
			}else if(syncItem.equals("global")){
				
			}else if(syncItem.equals("item")){
				syncItem();
			}else if(syncItem.equals("consumer")){
				
			}else if(syncItem.equals("user")){
				
			}else if(syncItem.equals("bookkeeping")){
				
			}
			
		}
	};

	String getSyncXML(String itemId, List<HashMap<String, String>> list){
		StringWriter writer = new StringWriter(); 
		XmlSerializer serializer = Xml.newSerializer();
		try{
			serializer.setOutput(writer);
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "syncItems");
			serializer.attribute(null, "id", itemId);
			
			for(HashMap<String, String> hm : list){
				serializer.startTag(null, "syncItem");
				for(Iterator<String> ite = hm.keySet().iterator(); ite.hasNext();){
					String tag = ite.next();
					serializer.startTag(null, tag);
					serializer.text(hm.get(tag));
					serializer.endTag(null, tag);
				}
				serializer.endTag(null, "syncItem");
			}
			
			serializer.endTag(null, "syncItems");
			serializer.endDocument();
		}catch(Exception e){
			throw new SyncDataException(e);
		}
		return writer.toString();
	}
	
	void syncItem() {
		//String xml = getSyncXML("item", getAllItems());
		
		
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("id", "2");
		hm.put("name", "食品");
		hm.put("test", "测试");
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		list.add(hm);
		String xml = getSyncXML("item", list);
		System.out.println(xml);
		sendXMLToServer4Sync(xml);
	}
	
	private List<HashMap<String, String>> getAllItems(){
		DBHelper helper = new DBHelper(DataSync.this);
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		Cursor c = helper.query(DBUtil.ITEM_TBL_NAME, new String[]{"id", "name"});
		
		while (c.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", c.getString(0));
			map.put("name", c.getString(1));
			list.add(map);
		}
		
		return list;
	}
	
	private void sendXMLToServer4Sync(String xml) {
		HttpClient client = HttpClients.createDefault();
		//String httpUrl = Constants.SYNC_URL;
		String httpUrl = "http://192.168.1.101:8080/";
		HttpPost request = new HttpPost(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("xml",xml));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			request.setEntity(entity);
			
			HttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				new BookkeepingAlertDialog().show(DataSync.this, R.string.data_sync_success);
			}else{
				new BookkeepingAlertDialog().show(DataSync.this, R.string.data_sync_failure);
			}
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
			new BookkeepingAlertDialog().show(DataSync.this, R.string.data_sync_failure);
		}
	}
}
