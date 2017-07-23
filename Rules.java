package Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public final class Rules
{
	//	public static ArrayList<ArrayList<Rule> rules = new ArrayList<Rule>();
	public static HashMap<Integer, ArrayList<Rule>> rules = new HashMap<Integer, ArrayList<Rule>>();

	/**
	 * Given a stacktop symbol (in integer form), return a list of rules that match it.
	 */
	public static ArrayList<Rule> getRules(int stacktop)
	{
		return rules.get(stacktop);
	}
}
