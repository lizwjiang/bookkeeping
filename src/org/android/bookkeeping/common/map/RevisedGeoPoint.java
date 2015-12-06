/* RevisedGeoPoint.java
 * Created on 2011-9-20
 */
package org.android.bookkeeping.common.map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

/**
 * Android 2.2 has a bug which is the error "Service not Available" will be raised 
 * once getting getFromLocationName called. 
 * the below class is used to get the right GeoPoint 
 * @author Johnson_Jiang01
 * @version 1.0, 2011-9-20
 */
public class RevisedGeoPoint {
	
	public static JSONObject getLocationInfo(String address) {

		HttpGet httpGet = new HttpGet("http://maps.google."
				+ "com/maps/api/geocode/json?address=" + address
				+ "&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return jsonObject;
	}

	public static GeoPoint getGeoPoint(JSONObject jsonObject) {
		Double lon = new Double(0);
		Double lat = new Double(0);

		try {
			lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return new GeoPoint((int) (lat * 1e6), (int) (lon * 1e6));

	}

	public static String getAddressByLatLng(double lat, double lng) {
		String address = null;
		JSONObject jsonObject = geocodeAddr(lat, lng);
		try {
			JSONArray placemarks = jsonObject.getJSONArray("Placemark");
			JSONObject place = placemarks.getJSONObject(0);
			address = place.getString("address");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return address;
	}
	
	/**
	 *  由经纬度获得地址
     * @param lat  纬度
     * @param lng 经度
     * @return
     */
	private static JSONObject geocodeAddr(double lat, double lng) {
		String urlString = "http://ditu.google.com/maps/geo?q=+" + lat + ","
				+ lng + "&output=json&oe=utf8&hl=zh-CN&sensor=false";
		// String urlString =
		// "http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&language=zh_CN&sensor=false";
		StringBuilder sTotalString = new StringBuilder();
		try {
			URL url = new URL(urlString);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

			InputStream urlStream = httpConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlStream));

			String sCurrentLine = "";

			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				sTotalString.append(sCurrentLine);
			}
			bufferedReader.close();
			httpConnection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(sTotalString.toString());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonObject;
	}
}
