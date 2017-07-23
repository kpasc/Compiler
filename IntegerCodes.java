package Scanner;

import java.util.HashMap;


public class IntegerCodes
{
	public static HashMap<String, Integer> codes = new HashMap<String, Integer>();

	public static HashMap<Integer, String> tokens = new HashMap<Integer, String>();

	public static void addCode(String token, int code)
	{
		codes.put(token, code);
		tokens.put(code, token);
	}

	public static int findCode(String token)
	{
		return codes.get(token);
	}

	public static String findToken(int code)
	{
		return tokens.get(code);
	}
}
