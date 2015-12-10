public class Test1 {

	public static void main(String[] args) {
		String url = "http://www.any-site.com/";
		conn = (HttpConnection)Connector.open(url, Connector.READ_WRITE);
		if (conn.getResponseCode( ) == HttpConnection.HTTP_OK) {
		  is = conn.openInputStream( );
		  final int MAX_LENGTH = 128;
		  byte[] buf = new byte[MAX_LENGTH];
		  int total = 0;
		  while (total < MAX_LENGTH) {
		    int count = is.read(buf, total, MAX_LENGTH - total);
		    if (count < 0) {
		      break;
		    }
		    total += count;
		  }
		  is.close( );
		  String reply = new String(buf, 0, total);

	   BufferedWriter out = conn.openOutputStream( );
	   
	   TextView et3 = (TextView) findViewById(R.id.editText3);
       String password;
	   password = "aaa";
	   password = et3.getText().toString();
	   out.write("Hello");
	   out.write(password);

	   //Retrieving Device ID
	   TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE); 
	   String uid = tManager.getDeviceId(); 

	   // Vulnerability - sending device ID on the network
	   // May be used for authentication
	   out.write(uid.getBytes());

	}

 }

 public void foo(){
 		String url = "https://www.any-site.com/";
		conn = (HttpConnection)Connector.open(url, Connector.READ_WRITE);
			   BufferedWriter out = conn.openOutputStream( );
	   // NOT  Vulnerability , https is used
	   // Get password from input and send it over network over https
	   TextView et3 = (TextView) findViewById(R.id.editText3);
       String password  = et3.getText().toString();
	   out.write("Good Bye!");
	   out.write(password);

 }
}
