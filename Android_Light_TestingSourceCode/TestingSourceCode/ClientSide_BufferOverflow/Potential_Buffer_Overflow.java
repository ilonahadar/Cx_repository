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

	//Native method declaration
	//  native byte[] loadFile(String name);
	
	//Load the library
	//  static {
		System.loadLibrary("nativelib");
	//  }
 
 //   byte buf[];
//Create class instance
//    ReadFile mappedFile=new ReadFile();
//Call native method to load ReadFile.java
//    buf=mappedFile.loadFile("ReadFile.java");
//Print contents of ReadFile.java
//    for(int i=0;i<buf.length;i++) {
 //     System.out.print((char)buf[i]);
//	}
	}
}
	


	