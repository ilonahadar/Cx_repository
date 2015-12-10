package javapadawan.android;

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nextscreen);
		initComponents();
	}

	public void initComponents() {
		message = getIntent().getStringExtra(NetworkConnection.NC_RESPONSE);
		tvMessage = (TextView) findViewById(R.id.tvMessage);
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
