package cx.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EmailActivityActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText edittextEmailAddress = (EditText)findViewById(R.id.email_address);
        final EditText edittextEmailSubject = (EditText)findViewById(R.id.email_subject);
        final EditText edittextEmailText = (EditText)findViewById(R.id.email_text);
        Button buttonSendEmail_intent = (Button)findViewById(R.id.sendemail_intent);
        
        buttonSendEmail_intent.setOnClickListener(new Button.OnClickListener(){			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String emailAddress = edittextEmailAddress.getText().toString();
				String emailSubject = edittextEmailSubject.getText().toString();
				

				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				String emailText = 	location.getLongitude();

				String emailAddressList[] = {emailAddress};
				
				Intent intent = new Intent(Intent.ACTION_SEND);  
				//intent.setType("plain/text");
				intent.setType("message/rfc822");
				
				intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);   
				intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);  
				// Vulnerability here - sending GPS info over e-mail
				intent.putExtra(Intent.EXTRA_TEXT, emailText);  
				startActivity(Intent.createChooser(intent, "Choice Appt send email:"));
				
			}});
    }
}