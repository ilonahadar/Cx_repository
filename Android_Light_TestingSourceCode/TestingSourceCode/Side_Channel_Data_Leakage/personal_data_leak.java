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
			String password = et3.getText().toString();

			String outFilename = "/sdcard/file.txt";

			FileWriter fstream = new FileWriter(outFilename);
			BufferedWriter out = new BufferedWriter(fstream);
			// Vulenrability - keep data on external storage
			out.write(password);
			
		} catch(Exception e){
			Toast.makeText(this, "Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		try
		{          	  

			TextView et4 = (TextView) findViewById(R.id.editText3);
			String password = et4.getText().toString();

			String outFilename = "/sdcard/file.txt";

			FileWriter fstream = new FileWriter(outFilename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(encryption(password));
			

		} catch(Exception e){
			Toast.makeText(this, "Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}


	};

	public String encryption(String input){

		return input;
		
	}


}