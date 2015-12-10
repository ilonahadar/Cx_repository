package javapadawan.android;

import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NextScreen extends Activity implements OnClickListener {
	private Button btnBack;
	private TextView tvMessage;
	private String message;
	
	private SQLiteAdapter mySQLiteAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nextscreen);
		initComponents();
	}

	public void initComponents() {
		message = getIntent().getStringExtra(NetworkConnection.NC_RESPONSE);
		tvMessage = (TextView) findViewById(R.id.tvMessage);
		
		Console console = System.console();
        if (console == null) {
            System.err.println("No console.");
            System.exit(1);
        }
      
         Pattern pattern = 
         Pattern.compile(console.readLine("%nEnter your regex: "));
         Matcher matcher = 
         pattern.matcher(console.readLine("Enter input string to search: "));

         boolean found = false;
            while (matcher.find()) {
                console.format("I found the text \"%s\" starting at " +
                   "index %d and ending at index %d.%n",
                    matcher.group(), matcher.start(), matcher.end());
                found = true;
            }
            if(!found){
                console.format("No match found.%n");
         }
      
		
		 mySQLiteAdapter = new SQLiteAdapter(this);
	     mySQLiteAdapter.openToWrite();
	     mySQLiteAdapter.deleteAll();
	       
	     mySQLiteAdapter.insert(message);
	     mySQLiteAdapter.close();
	        
		if (message != null) {
			tvMessage.setText(message);
		} else {
			tvMessage.setText("Message is null");
		}
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}

	
	public void onClick(View v) {
		if (v == btnBack) {
			finish();
		}
	}
}
