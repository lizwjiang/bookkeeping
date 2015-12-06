/* MapActivity.java
 * Created on 2011-9-17
 */
package org.android.bookkeeping.common.map;

import java.util.List;

import org.android.bookkeeping.R;
import org.android.bookkeeping.util.StringUtil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapActivity extends MapActivity {
	private MapView mapView;
	private GeoPoint gp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setTraffic(true);
		mapView.setSatellite(false);
		mapView.setStreetView(true);
		mapView.displayZoomControls(true);
		mapView.setBuiltInZoomControls(true);

		gp = this.getCurrentGeoPoint("上海市新行路317号");

		MapController mapController = mapView.getController();
		mapController.setZoom(14);
		mapController.animateTo(gp);

		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);

		mapView.invalidate();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private GeoPoint getCurrentGeoPoint(String name) {
		GeoPoint geoPoint = null;
		if (StringUtil.isNull(name)) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    Activity#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for Activity#requestPermissions for more details.
				return null;
			}
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			geoPoint = new GeoPoint((int) (location.getLatitude() * 1e6),
				(int) (location.getLongitude() * 1e6));
		}else{
			geoPoint = RevisedGeoPoint.getGeoPoint(RevisedGeoPoint
					.getLocationInfo(name));
				
		}
		return geoPoint;
	}

	class MapOverlay extends Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			Point screenPts = new Point();
			mapView.getProjection().toPixels(gp, screenPts);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.pushpin);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
			return true;
		}

	}

}
