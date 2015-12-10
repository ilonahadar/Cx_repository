public class TestEvilRegexPatternExamples
{
    public static void main(String[] args)
	{
		// Evil Regex pattern examples
		String[] patterns = {  "(a+)+", "([a-zA-Z]+)*", "(a|aa)+", "(a|a?)+", "(.*a){11}", "(a+)+$", "([a-zA-Z]+)*$", "(a|aa)+$", "(a|a?)+$", "(.*a){11}$" };
		String payload = "aaaaaaaaaaaaaaaaaaX";
		String payload2;
		payload2 = args[0];
		for (String pat : patterns) {
		  System.out.println("Replace in '" + payload
							 + "' pattern '" + pat
							 + "' with '*' => "
							 + payload.replaceAll(pat, "*")
							 + payload2.replaceAll(pat, "*"));
		}
		  System.out.println("Replace in '" + payload
							 + "' pattern ' (a+)+"
							 + "' with '*' => "
							 + payload.replaceAll("(a+)+", "*")
							 + payload2.replaceAll("(a+)+", "*"));
	}
}