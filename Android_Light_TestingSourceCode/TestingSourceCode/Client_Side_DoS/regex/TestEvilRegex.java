public class TestEvilRegex {
   
    public static void main(String[] args)
    {
        String[] patterns = { "(a+)+", "([a-zA-Z]+)*", "(a|aa)+", "(a|a?)+", "(.*a){11}", "(a+)+$", "([a-zA-Z]+)*$", "(a|aa)+$", "(a|a?)+$", "(.*a){11}$" };
        String payload = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaX" + args[0];
        String payload2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaX";
		String strPat = args[1];
		if (payload.matches(strPat))
		{
			System.out.println("Regex Injection");
		}
		if (payload.replaceAll(strPat,"*") != null)
		{
			System.out.println("Regex Injection");
		}
		if (payload2.matches(strPat))
		{
			System.out.println("Regex Injection");
		}
		if (payload2.replaceAll(strPat,"*") != null)
		{
			System.out.println("Regex Injection");
		}
        for (String pat : patterns) {
          System.out.println("Probing '" + payload
                             + "' with pattern '" + pat);
          if (payload2.matches(pat))
              System.out.println(payload2.replaceAll(pat, "*"));
          if (payload.matches(pat))
              System.out.println(payload.replaceAll(pat, "*"));
          else
                System.out.println(payload + " does not match");
        }
    }
}
