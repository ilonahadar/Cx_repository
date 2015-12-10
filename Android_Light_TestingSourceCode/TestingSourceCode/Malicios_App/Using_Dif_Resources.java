package com.example.HelloWorld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class HelloWorldActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Using network
		HttpGet httpget = new HttpGet("http://localhost/");

		// Using SMS
		SmsManager sms = SmsManager.getDefault();        
		sms.sendTextMessage(phoneNumber, null, message, pi, null);    

		// Using Telephony
	    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		//Using GPS
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 

		// Using Contacts
		ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

		//Using Camera
		Camera camera = Camera.open();

	}
}
	


	