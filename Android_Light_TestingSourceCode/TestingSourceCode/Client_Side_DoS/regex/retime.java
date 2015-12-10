// redos.java - Java test program for regular expression DoS attacks  
//

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class RETime
{

//
// Test parameters
//
// regex:            String containing the regular expression to be tested
// maketeststring:   A function which generates a test string from a length parameter
// maxiter:          Maximum number of test iterations to be performed (typical value is 50)
// maxtime:          Maximum execution time in seconds for one iteration before the test program
//                   is terminated (typical value is 2 seconds)
//

static String regex = "^(a+)+$";
public static String maketeststring(int n) {int i; String str; str = ""; while(str.length() < n)str += 'a'; return str+"!";}
static int maxiter = 50;
static int maxtime = 2;


//
// The main function
//

public static void main(String[] args)

{

	int i;
	float time;
	Pattern cregex;
	
	System.out.println("");
	System.out.println("Java Regular Expression DoS demo");
	System.out.println("from http://www.computerbytesman.com/redos");
	System.out.println("");
	System.out.println("Platform:             Java");
	System.out.println("Regular expression:   " + RETime.regex);
	System.out.println("Typical test string:  " + maketeststring(10));
	System.out.println(String.format("Max. iterations:      %d", RETime.maxiter));
	System.out.println(String.format("Max. match time:      %d secs", RETime.maxtime));
	System.out.println("");
	cregex = Pattern.compile(RETime.regex);
	for (i = 1; i <= RETime.maxiter; ++i)
	{
		time = RETime.runtest(cregex, i);
		if(time > RETime.maxtime)
			break;
	}
	return;
	
}


//
// Run one test function
//

public static float runtest(Pattern regex, int n)

{

	String teststr;
	long starttime;
	int elapsetime;
	Boolean matchf;
	Matcher matcher;

	teststr = maketeststring(n);
	starttime = System.currentTimeMillis();
	matcher = regex.matcher(teststr);
	matchf = matcher.find();
	elapsetime = (int)(System.currentTimeMillis() - starttime);
	System.out.println(String.format("For n=%d, match time=%d msec, match count=%s", n, elapsetime, matchf));
	return ((float)elapsetime) / 1000;

}

}