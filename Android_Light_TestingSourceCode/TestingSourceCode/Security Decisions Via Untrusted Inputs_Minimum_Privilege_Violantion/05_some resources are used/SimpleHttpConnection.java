package javapadawan.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class SimpleHttpConnection extends Activity implements OnClickListener {

	private Button btnConnect, btnCancel;
	private final int INTENT_NEXT_SCREEN = 0;
	private Intent connectIntent = null;
	private ProgressBar progressBar;
	private NetworkConnection networkConnection;

	/** Called when the activity is first created. */
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initComponents();
	}

	private void initComponents() {
		connectIntent = new Intent(this, NextScreen.class);
		progressBar = (ProgressBar) findViewById(R.id.pbProgressBar);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnCancel.setVisibility(Button.INVISIBLE);
	}

	
	public void onClick(View v) {
		if (v == btnConnect) {
			try {
				networkConnection = new NetworkConnection(progressBar, 
						getString(R.string.simplehttpurl), this,
						connectIntent, INTENT_NEXT_SCREEN);
				networkConnection.start();
				btnConnect.setVisibility(Button.INVISIBLE);
				btnCancel.setVisibility(Button.VISIBLE);
			} catch (Exception ex) {
				Log.e(getClass().getName(), "Error on onClick btnConnect ", ex);
			}
		} else if(v == btnCancel) {
			if(networkConnection != null) {
				try {
					networkConnection.close();
				} catch(Exception ex) {
					Log.e(getClass().getName(), "Error on onClick btnCancel ", ex);
				}
			}
			reset();
		}
	}

	private void reset() {
		progressBar.setProgress(0);
		progressBar.setSecondaryProgress(0);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		
		btnCancel.setVisibility(Button.INVISIBLE);
		btnConnect.setVisibility(Button.VISIBLE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(getClass().getName(), requestCode + " - " + resultCode);
		if (requestCode == INTENT_NEXT_SCREEN) {
			reset();
		}
	}
}