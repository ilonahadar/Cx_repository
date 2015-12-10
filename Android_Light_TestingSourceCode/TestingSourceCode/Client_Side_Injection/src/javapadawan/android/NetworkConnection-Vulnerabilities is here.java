package javapadawan.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.ProgressBar;
import javax.mail.*;


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

	
	private SQLiteAdapter mySQLiteAdapter;
	
	
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

	public void transfer(String urlStr) throws IOException, MessagingException {
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
		
		// Client side injection
		// The network input without any sanitation writed down to DB
		 mySQLiteAdapter = new SQLiteAdapter(null);
	     mySQLiteAdapter.openToWrite();
	     mySQLiteAdapter.deleteAll();       
	     mySQLiteAdapter.insert(response);
	     mySQLiteAdapter.close();
		
	    BluetoothSocket mmSocket=null;
	    int bufferSize = 1024;
	    byte[] buffer2 = new byte[bufferSize];
	    int bytesRead = -1;
	    InputStream instream = mmSocket.getInputStream();
	    String message="";

	    bytesRead = instream.read(buffer2);
	    if (bytesRead != -1) {
	    	while ((bytesRead == bufferSize) && (buffer2[bufferSize-1] != 0)){
	    	message = message + new String(buffer2, 0, bytesRead);
	    	bytesRead = instream.read(buffer2);
	    	}
	    }
	    
	     mySQLiteAdapter = new SQLiteAdapter(null);
	     mySQLiteAdapter.openToWrite();
	     mySQLiteAdapter.deleteAll();       
	     mySQLiteAdapter.insert(message);
	     mySQLiteAdapter.close();
	    
	     SmsMessage[] msgs = null; 
	     Bundle bundle = intent.getExtras();   
	     Object[] pdus = (Object[]) bundle.get("pdus");
	     String str = "";  
	     msgs = new SmsMessage[10];            
         for (int i=0; i<msgs.length; i++){
             msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
             str += "SMS from " + msgs[i].getOriginatingAddress();                     
             str += " :";
             str += msgs[i].getMessageBody().toString();
             str += "\n";        
         }

         mySQLiteAdapter = new SQLiteAdapter(null);
	     mySQLiteAdapter.openToWrite();
	     mySQLiteAdapter.deleteAll();       
	     mySQLiteAdapter.insert(str);
	     mySQLiteAdapter.close();
	     
	     
	     Properties props = new Properties();     
	     props.setProperty("mail.store.protocol", "imaps");     
	     props.setProperty("mail.store.socketFactory.class",  "com.imap.DummySSLSocketFactory"); 
	     props.setProperty("mail.pop3.socketFactory.fallback", "false"); 
	     Session session = Session.getDefaultInstance(props, null); 
	     Store store= session.getStore("imaps"); 
	     store.connect("mail.domain.com", "user", "pass"); 

	     Folder inbox = store.getFolder("Inbox"); 
	        inbox.open(Folder.READ_ONLY); 
	        javax.mail.Message messages2[] = inbox.getMessages();
	        
	        for (javax.mail.Message m : messages2) { 
	        	 mySQLiteAdapter = new SQLiteAdapter(null);
	    	     mySQLiteAdapter.openToWrite();
	    	     mySQLiteAdapter.deleteAll();       
	    	     mySQLiteAdapter.insert(m.toString());
	    	     mySQLiteAdapter.close(); 
	        } 

	     
	     
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
