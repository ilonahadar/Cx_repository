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
	}

	public void ButtonClickHandler(View view)
	{
		try
		{          	  

			TextView et3 = (TextView) findViewById(R.id.editText3);
			String secret = et3.getText().toString();

			TextView tv3 =	(TextView) findViewById(R.id.textView3);
			if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			{     Toast.makeText(this, "External SD card not mounted", Toast.LENGTH_LONG).show();
			}
			else{
				tv3.setText("Text2 ");
			}

			String outFilename = "/sdcard/file.txt";
			

			FileWriter fstream = new FileWriter(outFilename);
			BufferedWriter out = new BufferedWriter(fstream);

			// Vulnerability - Unsecure data storage
			out.write(secret);
			out.close();
			tv3 =	(TextView) findViewById(R.id.textView3);

			tv3.setText("Text2 " + secret + " was written to " + outFilename);           	

		} catch(Exception e){
			Toast.makeText(this, "Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}



	};



	public void ButtonReadOnclick(View view)
	{
		try
		{

			String inFilename = "/sdcard/file.txt";
			// String inFilename = "/data/data/com.example.HelloWorld/Private.txt";         

			TextView tv =	(TextView) findViewById(R.id.editText3);
			FileReader fstream = new FileReader(inFilename);
			BufferedReader inf = new BufferedReader(fstream);
			String s = inf.readLine();
			inf.close();
			tv.setText(s);
		}

		catch(Exception e){
			Toast.makeText(this, "Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}  


	}
}