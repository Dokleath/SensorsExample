package com.example.devicecapabilities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements LocationListener, SensorEventListener {

	private LocationManager locMgr;
	private SensorManager sensorManager;
	private Sensor mProximitySensor;
	private Sensor mAccelerometerSensor;
	private String provider;
	private static TextView txtLatitude;
	private static TextView txtLongitude;
	private static TextView txtProximitySensor;
	private static TextView txtAccelerometer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		
	    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mProximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	    mAccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		
		boolean enabled = locMgr
		  .isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) {
		  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		  startActivity(intent);
		} 
		
		Criteria criteria = new Criteria();
	    provider = locMgr.getBestProvider(criteria, false);
	    Location location = locMgr.getLastKnownLocation(provider);
	    
	    if (location != null) {
	       System.out.println("Provider " + provider + " has been selected.");
	     } else {
	       txtLatitude.setText("Location not available");
	       txtLongitude.setText("Location not available");
	     }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	    locMgr.requestLocationUpdates(provider, 400, 1, this);
	    
	    sensorManager.registerListener(this, mProximitySensor,
	    		    SensorManager.SENSOR_DELAY_NORMAL);
	    
	    sensorManager.registerListener(this, mAccelerometerSensor,
    		    SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    locMgr.removeUpdates(this);
	    sensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			txtLatitude = (TextView) rootView.findViewById(R.id.txtLatitude);
			txtLongitude = (TextView) rootView.findViewById(R.id.txtLongitude);
			txtProximitySensor = (TextView) rootView.findViewById(R.id.txtProximitySensor);
			txtAccelerometer = (TextView) rootView.findViewById(R.id.txtAccelerometer);
			return rootView;
		}
	}

	/*
	 * LocationManager Start
	 */
	@Override
	public void onLocationChanged(Location location) {
	    txtLatitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
	    txtLongitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, provider + " u hek prej perdorimit!",
		        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, provider + " tani eshte ne perdorim!",
		        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Pjese e LocationListener
	}

	/*
	 * LocationManager End
	 */
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			handleAccelerometerSensor(event);
		} else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			handleProximitySensor(event);
		}
	}
	
	private void handleProximitySensor(SensorEvent event) {
		if (event.values[0] == 0) {
			txtProximitySensor.setText("Proximity Sensor: ngat");
		} else {
			txtProximitySensor.setText("Proximity Sensor: larg");
		}
	}
	private void handleAccelerometerSensor(SensorEvent event) {
	    float[] values = event.values;	    
	    float x = values[0];
	    float y = values[1];
	    float z = values[2];	    
	    txtAccelerometer.setText("Accelerometer: X (" + String.valueOf(x) + "), Y (" + String.valueOf(y) + "), Z (" + String.valueOf(x) + ")");
	  }

}
