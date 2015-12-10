package javapadawan.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class NetworkConnection implements Runnable {

	private ProgressBar progressBar;
	private String response;
	private String serverUrl;
	private final Handler handler = new Handler();
	private Activity parent;
	private Intent intent;
	private int intentId;

	private int current = 0;
	private int total = 0;
	
	private Thread runner;
	private URL url;
	private URLConnection urlConnection;
	private InputStream inputStream;

	public static final String NC_RESPONSE = "RESPONSE";

	public NetworkConnection(ProgressBar progressBar, String serverUrl,
			Activity activity, Intent nextIntent, int intentId) {
		this.progressBar = progressBar;
		this.progressBar.setVisibility(ProgressBar.VISIBLE);
		this.serverUrl = serverUrl;
		this.parent = activity;
		this.intent = nextIntent;
		this.intentId = intentId;
	}

	public String getResponse() {
		return response;
	}

	public void run() {
		try {
			transfer(serverUrl);
		} catch (Exception e) {
			Log.e(getClass().getName(), "Error on startStreaming ", e);
			return;
		}
	}

	public void start() throws IOException {
		runner = new Thread(this);
		runner.start();
	}

	public void transfer(String urlStr) throws IOException {
		url = new URL(urlStr);
		urlConnection = url.openConnection();
		inputStream = urlConnection.getInputStream();
		if (inputStream == null) {
			Log.e(getClass().getName(), "Error on transfer " + url);
		}
		int contentLength = (int) urlConnection.getContentLength();
		if (contentLength == -1)
			contentLength = 255;
		int ch = 0;
		total = contentLength;
		Log.v(getClass().getName(), contentLength + "");
		StringBuffer buffer = new StringBuffer();
		while ((ch = inputStream.read()) != -1) {
			buffer.append((char) ch);
			fireDataLoadUpdate();
			current++;
		}
		inputStream.close();
		response = buffer.toString();
		Log.v(getClass().getName(), response);
		intent.putExtra(NC_RESPONSE, response);
		parent.startActivityForResult(intent, intentId);
	}

	private void fireDataLoadUpdate() {
		Runnable updater = new Runnable() {
			public void run() {
				double loadProgress = ((double) current / (double) total);
				Log.v(getClass().getName(), "loadProgress=" + loadProgress);
				progressBar.setProgress((int) (loadProgress * 100));
				progressBar.setSecondaryProgress((int) (loadProgress * 100));
				Log.v(getClass().getName(), "" + progressBar.getProgress());
			}
		};
		handler.post(updater);
	}

	public void close() {
		if (runner != null) {

			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
				inputStream = null;
			}
			if(urlConnection != null) {
				try {
					urlConnection = null;
				} catch(Exception ex) {
				}
			}
			try {
				runner.interrupt();
				runner.join();
				runner = null;
			} catch (InterruptedException e) {
				//Thread.currentThread().interrupt();
			}
		}
	}
}
