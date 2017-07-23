package Parser;

import java.util.ArrayList;
import java.util.Stack;

import Driver.Compiler;
import Scanner.IntegerCodes;
import Scanner.Scanner;
import Utilities.Rule;
import Utilities.Rules;


public final class Parser
{
	/**
	 * Stack that holds all the integer codes of terminal and non-terminals.
	 */
	private static Stack<Integer> stack = new Stack<Integer>();

	/**
	 * Current state the compiler is in.
	 * 0 - initial
	 * 1 - working
	 * 2 - accept
	 * -1 - error
	 */
	private static int state = 0;

	/**
	 * Holds the amount of steps the parser has taken
	 */
	private static int steps = 0;

	/**
	 * List that holds each token as they are consumed.
	 * Not sure I need this, but use it for now.
	 */
	public static ArrayList<String> consumed = new ArrayList<String>();

	public static void parse()
	{
		// First, create the integer codes for each terminal, non-terminal, start symbol, and special symbols
		setIntegerCodes();

		// Second, turn the syntax rules into an equivalent PDA.
		setPDA();

		// Next, push the start symbol onto the stack
		stack.push(IntegerCodes.findCode("Z_o"));

		if(Compiler.printParserFindings) System.out.println("Step: " + steps + " Stacktop: " + stack.peek() + " Lookahead: HAI use rule: " + 0 + " Action: Push <lol> (1)");
		steps++;

		// Call the scanner so that it loads in the file to be scanned and parsed
		callScanner();

		// Now begin parsing
		// Do this loop at least once -- will start with start symbol on top -- end when it ses it on top again.
		do
		{
			// Get stack top value
			Integer peek = stack.peek();

			// We have a special situation if the peek value is the start symbol, 'Z_o'
			if(peek == 0)
			{
				startSymbol();
				continue;
			}

			// Get deterministic rule corresponding to this stack top symbol
			Rule rule = getRule(peek);

			// Execute the PDA transition given by the rule
			executeRule(rule);

		} while(state != 2);

		if(Compiler.printParserFindings) System.out.println("Step: " + steps + " Stacktop: " + stack.peek() + " Lookahead: null use rule: " + 0 + " Action: Accept");
	}

	////////////////////
	// Scanner Call Functions
	////////////////////

	private static void callScanner()
	{
		// 2. Run the file through the scanner, one character at a time
				//    Print each token as it is recognized, with its type and its line number.
				//    If illegal token is found, print error message and line number (this happens in error handler)
				String currentToken = "";
				outerloop:
				for(int currentLine = 0; currentLine < Compiler.file.size(); currentLine++)	// loop through each line in the input file
				{
					Compiler.linenumber = currentLine + 1; // update the current line number
					for(int currentChar = 0; currentChar < Compiler.file.get(currentLine).length(); currentChar++)	// loop through each char in the current line
					{
						int status = Scanner.scan(Compiler.file.get(currentLine).charAt(currentChar)); // Call scanner, send it the current character

						if(Scanner.whiteSpaceStatus())	// Current char is a whitespace
							Scanner.resetToken();	// reset the Scanners current token
						else if(Scanner.keywordStatus()) // Current char is a part of a keyword
						{
							if(status == 0)
							{
								// If 0, append current char to current token then continue
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);
								continue;
							}
							else if(status == 1)
							{
								// If 1, check if the current character is the last character in the line
								// Else if, check if the next character is either whitespace or a special symbol
								// Else, continue feeding the scanner characters for this current token
								if(currentChar == Compiler.file.get(currentLine).length()-1)
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

									// Print findings of this keyword
									if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Keyword found: - " + currentToken);

									// Add current token to scanned list
									Scanner.scanned.add(currentToken);

									// complier - Reset current Token to empty
									currentToken = "";

									// Scanner - reset current token to empty
									Scanner.resetToken();
								}
								else if((Compiler.file.get(currentLine).charAt(currentChar+1) == ' ' || Compiler.file.get(currentLine).charAt(currentChar+1) == ';'))
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

									// Print findings of this keyword
									if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Keyword found: - " + currentToken);

									// Add current token to scanned list
									Scanner.scanned.add(currentToken);

									// complier - Reset current Token to empty
									currentToken = "";

									// Scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);
									continue;
								}
							}
							else if(status == 2)
							{
								// If status is 2, it is a comment.

								// Append current char to current token
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

								// Print findings of this keyword
								if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Comment found: - " + currentToken);

								// complier - Reset current Token to empty
								currentToken = "";

								// scanner - Reset current Token to empty
								Scanner.resetToken();

								// Since this is a comment, skip this current line
								continue outerloop;
							}
							else if(status == -1)
							{
								// If -1, this is an error. We need to conduct panic mode recovery - skip to next token

								// Append current char to current token
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

								int length = Compiler.file.get(currentLine).length(); // length is current lines length
								if(currentChar == length-1)
								{
									// if currentChar == length-1, then this is the last char no more recovery needed
									if(Compiler.printScannerFindings) System.out.print(currentToken + "\n");

									// compiler - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else
								{
									// Else, we are not at the end of the line yet

									int newCharLoc = currentChar + 1; // newCharLoc is the location of the next character in the line
									char newChar = Compiler.file.get(currentLine).charAt(newCharLoc); // newChar is the next character in the line

									// Loop that will break if newChar is ever whitespace or special symbol
									while((newChar != ' ' && newChar != ';'))
									{
										// Append newChar to current token
										currentToken = currentToken + newChar;

										newCharLoc = newCharLoc + 1; // increment newCharLoc

										// As long as newCharLoc isn't beyond the line end, set newChar to the next character
										// Else, we are at the end, break loop.
										if(newCharLoc < length)
											newChar = Compiler.file.get(currentLine).charAt(newCharLoc);
										else break;
									}

									if(Compiler.printScannerFindings) System.out.print(" - " + currentToken + "\n");

									// compiler - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();

									// Set currentChar to the beginning of next token that we just found
									currentChar = newCharLoc - 1;
								}
							}
						}
						else if(Scanner.idStatus())
						{
							if(status == 0)
							{
								// If 0, append current char to current token then continue
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);
								continue;
							}
							else if(status == 1)
							{
								// If 1, check if the current character is the last character in the line
								// Else if, check if the next character is either whitespace or a special symbol
								// Else, continue feeding the scanner characters for this current token
								if(currentChar == Compiler.file.get(currentLine).length()-1)
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

									// Print findings of this id
									if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Identifier found: - " + currentToken);

									// Tell scanner to check if this id exists in the symbol table.
									// If not, scanner will call symbol table to add it
									Scanner.newValue("identifier");

									// Add current token to scanned list
									Scanner.scanned.add(currentToken);

									// complier - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else if((Compiler.file.get(currentLine).charAt(currentChar+1) == ' ' || Compiler.file.get(currentLine).charAt(currentChar+1) == ';'))
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

									// Print findings of this id
									if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Identifier found: - " + currentToken);

									// Tell scanner to check if this id exists in the symbol table.
									// If not, scanner will call symbol table to add it
									Scanner.newValue("identifier");

									// Add current token to scanned list
									Scanner.scanned.add(currentToken);

									// complier - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);
									continue;
								}
							}
							else if(status == -1)
							{
								// If -1, this is an error. We need to conduct panic mode recovery - skip to next token

								// Append current char to current token then continue
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

								int length = Compiler.file.get(currentLine).length();	// length is current lines length
								if(currentChar == length-1)
								{
									// if currentChar == length-1, then this is the last char no more recovery needed
									if(Compiler.printScannerFindings) System.out.print(" - " + currentToken + "\n");

									// complier - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else
								{
									// Else, we are not at the end of the line yet
									int newCharLoc = currentChar + 1;	// newCharLoc is the location of the next character in the line
									char newChar = Compiler.file.get(currentLine).charAt(newCharLoc);	// newChar is the next character in the line

									// Loop that will break if newChar is ever whitespace or special symbol
									while((newChar != ' ' && newChar != ';'))
									{
										// Append newChar to current token
										currentToken = currentToken + newChar;

										// increment newCharLoc
										newCharLoc = newCharLoc + 1;

										// As long as newCharLoc isn't beyond the line end, set newChar to the next character
										// Else, we are at the end, break loop.
										if(newCharLoc < length)
											newChar = Compiler.file.get(currentLine).charAt(newCharLoc);
										else break;
									}

									if(Compiler.printScannerFindings) System.out.print(" - " + currentToken + "\n");

									// complier - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();

									// Set currentChar to the beginning of next token that we just found
									currentChar = newCharLoc - 1;
								}
							}
						}
						else if(Scanner.constStatus())
						{
							if(status == 1)
							{
								if(currentChar == Compiler.file.get(currentLine).length()-1)
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

									// Print findings of this const
									if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Constant found: - " + currentToken);

									// Tell scanner to check if this const exists in the symbol table.
									// If not, scanner will call symbol table to add it
									Scanner.newValue("constant");

									// Add current token to scanned list
									Scanner.scanned.add(currentToken);

									// compiler - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else if((Compiler.file.get(currentLine).charAt(currentChar+1) == ' ' || Compiler.file.get(currentLine).charAt(currentChar+1) == ';'))
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

									// Print findings of this const
									if(Compiler.printScannerFindings) System.out.println("Line#: " + Compiler.linenumber + " - Constant found: - " + currentToken);

									// Tell scanner to check if this const exists in the symbol table.
									// If not, scanner will call symbol table to add it
									Scanner.newValue("constant");

									// Add current token to scanned list
									Scanner.scanned.add(currentToken);

									// compiler - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();
								}
								else
								{
									// Append current char to current token then continue
									currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);
									continue;
								}
							}
							else if(status == -1)
							{
								// Append current char to current token then continue
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

								int length = Compiler.file.get(currentLine).length();	// length is current lines length
								if(currentChar == length-1)
								{
									// if currentChar == length-1, then this is the last char no more recovery needed
									if(Compiler.printScannerFindings) System.out.print(" - " + currentToken + "\n");

									// complier - Reset current Token to empty
									currentToken = "";

									// complier - Reset current Token to empty
									Scanner.resetToken();
								}
								else
								{
									// Else, we are not at the end of the line yet
									int newCharLoc = currentChar + 1;	// newCharLoc is the location of the next character in the line
									char newChar = Compiler.file.get(currentLine).charAt(newCharLoc);	// newChar is the next character in the line

									// Loop that will break if newChar is ever whitespace or special symbol
									while((newChar != ' ' && newChar != ';'))
									{
										// Append newChar to current token
										currentToken = currentToken + newChar;

										// increment newCharLoc
										newCharLoc = newCharLoc + 1;

										// As long as newCharLoc isn't beyond the line end, set newChar to the next character
										// Else, we are at the end, break loop.
										if(newCharLoc < length)
											newChar = Compiler.file.get(currentLine).charAt(newCharLoc);
										else break;
									}

									if(Compiler.printScannerFindings) System.out.print(" - " + currentToken + "\n");

									// compiler - Reset current Token to empty
									currentToken = "";

									// scanner - Reset current Token to empty
									Scanner.resetToken();

									// Set currentChar to the beginning of next token that we just found
									currentChar = newCharLoc - 1;
								}
							}
						}
						else if(Scanner.ssymbolStatus())
						{
							if(status == 1)
							{
								// Append current char to current token then continue
								currentToken = currentToken + Compiler.file.get(currentLine).charAt(currentChar);

								// Add current token to scanned list
								Scanner.scanned.add(currentToken);

								// compiler - Reset current Token to empty
								currentToken = "";

								// scanner - Reset current Token to empty
								Scanner.resetToken();
							}
							else if(status == -1);
						}
					}
				}
	}

	////////////////////
	// Utility Functions
	////////////////////

	/**
	 * Top of the stack is start symbol Z_o
	 */
	private static void startSymbol()
	{
		// If peek is 0 then it is start symbol
		// We don't have to worry about look ahead since these are
		// Special cases. If parser is in state 0 - or state 1 we know which rule to return.
		if(state == 0)
		{
			updateState(1);
			stack.push(42);
		}
		else if(state == 1)
			updateState(2);
	}

	/**
	 * Finds the current rule that is needed
	 */
	private static Rule getRule(Integer peek)
	{
		Integer lookahead = Scanner.getLookAhead();

		if(peek >= 1 && peek <= 41)
		{
			// If peek is between 1 & 41 (inclusive) it is a terminal symbol
			// Get the rule corresponding to it
			// Since these are terminal symbols, they only have one rule.
			// So we know which rule to use.
			if(Rules.rules.get(peek).get(0).lookahead.get(0) == lookahead)
				return Rules.rules.get(peek).get(0);
			else
				return null;
		}
		else if(peek >= 42 && peek <= 64)
		{
			// If peek is between 42 & 64 (inclusive) it is a non-terminal symbol.
			// Get the rule corresponding to it using the look ahead token.
			// Loop through all of the rules corresponding to this symbol
			for(Rule rule : Rules.getRules(peek))
			{
				for(int la : rule.lookahead)
				{
					if(lookahead == la)
						return rule;
				}
			}
		}
		else
			System.out.println("WTF getRule!!!");

		return null;
	}

	/**
	 * Takes a rule and executes it
	 * @param rule
	 */
	private static void executeRule(Rule rule)
	{
		// Change the state of the parser from init_state to final_state
		updateState(rule.final_state);

		// Next, if the rule doesn't not have an array of things to push onto the stack (i.e. it's null)
		// It is a terminal. Thus, match and pop from stack. (consume)
		if(rule.toPush == null)
		{
			// Just pop stack - epsilon rule
			if(IntegerCodes.findToken(stack.peek()).charAt(0) == '<')
			{
				if(Compiler.printParserFindings) System.out.println("Step: " + steps + " Stacktop: " + stack.peek() + " Lookahead: " + Scanner.scanned.peek() + " use rule: " + rule.id + " - " + rule.sub_id);
				stack.pop();
			}
			else if(rule.id == 1 || rule.id == 2)
			{
				if(Compiler.printParserFindings) System.out.println("Step: " + steps + " Stacktop: " + stack.peek() + " Lookahead: " + Scanner.scanned.peek() + " match " + Scanner.scanned.peek() + " (" +IntegerCodes.findToken(rule.lookahead.get(0)) + ")" + " (" +rule.lookahead.get(0) + ")");
				// Consume
				consumed.add(IntegerCodes.findToken(stack.pop()));
				Scanner.scanned.poll();
			}
			else
			{
				if(Compiler.printParserFindings) System.out.println("Step: " + steps + " Stacktop: " + stack.peek() + " Lookahead: " + Scanner.scanned.peek() + " match " + IntegerCodes.findToken(rule.lookahead.get(0)) + " (" +rule.lookahead.get(0) + ")");
				// Consume
				consumed.add(IntegerCodes.findToken(stack.pop()));
				Scanner.scanned.poll();
			}
		}
		else
		{
			if(Compiler.printParserFindings) System.out.println("Step: " + steps + " Stacktop: " + stack.peek() + " Lookahead: " + Scanner.scanned.peek() + " use rule: " + rule.id + " - " + rule.sub_id);

			// pop the top
			stack.pop();

			// Push next symbols onto the stack in reverse order
			for(int i = rule.toPush.size()-1; i >= 0; i--)
				stack.push(rule.toPush.get(i));
		}
		steps++;
	}

	/**
	 * Changes the current state to the new state
	 */
	private static void updateState(int newState)
	{
		state = newState;
	}

	/**
	 * Method that initializes all of the terminal codes.
	 */
	public static void setIntegerCodes()
	{
		for(int i = 0; i < 65; i++)
		{
			String token = "";
			switch (i)
			{
				// 0: Stack top symbol
				case 0: token = "Z_o"; 			break;

				// 1 & 2: id and const
				case 1: token = "[id]"; 		break;
				case 2: token = "[const]"; 		break;

				// 3-40: Keywords (Terminals)
				case 3:  token = "HAI"; 		break;
				case 4:  token = "KTHXBYE"; 	break;
				case 5:  token = "GIMMEH"; 		break;
				case 6:  token = "VISIBLE"; 	break;
				case 7:  token = "I HAS A"; 	break;
				case 8:  token = "ITZ A"; 		break;
				case 9:  token = "NUMBR"; 		break;
				case 10: token = "NUMBAR"; 		break;
				case 11: token = "TROOF"; 		break;
				case 12: token = "WIN"; 		break;
				case 13: token = "FAIL"; 		break;
				case 14: token = "R"; 			break;
				case 15: token = "IM IN YR";	break;
				case 16: token = "WILE"; 		break;
				case 17: token = "IM OUTTA YR"; break;
				case 18: token = "O RLY?"; 		break;
				case 19: token = "YA RLY"; 		break;
				case 20: token = "NO WAI"; 		break;
				case 21: token = "OIC"; 		break;
				case 22: token = "WTF?";		break;
				case 23: token = "OMG";			break;
				case 24: token = "FOUND YR";	break;
				case 25: token = "GTFO";		break;
				case 26: token = "HOW IZ I";	break;
				case 27: token = "IF U SAY SO"; break;
				case 28: token = "YR";			break;
				case 29: token = "I IZ";		break;
				case 30: token = "MKAY";		break;
				case 31: token = "SUM OF";		break;
				case 32: token = "AN";			break;
				case 33: token = "DIFF OF";		break;
				case 34: token = "PRODUKT OF";	break;
				case 35: token = "QUOSHUNT OF"; break;
				case 36: token = "BOTH OF";		break;
				case 37: token = "EITHER OF";	break;
				case 38: token = "NOT";			break;
				case 39: token = "BOTH SAEM";	break;
				case 40: token = "DIFFRINT";	break;
				//case 41: token = "BTW";			break;	// INCLUDE COMMENT LINE?


				// 41: ;
				case 41: token = ";"; 			break;

				// 42 - 64: Non-terminals
				case 42: token = "<lol>";		break;
				case 43: token = "<body>";		break;
				case 44: token = "<stmt>";		break;
				case 45: token = "<input>";		break;
				case 46: token = "<output>";	break;
				case 47: token = "<decl>";		break;
				case 48: token = "<type>";		break;
				case 49: token = "<asmt>";		break;
				case 50: token = "<loop>";		break;
				case 51: token = "<if>";		break;
				case 52: token = "<case>";		break;
				case 53: token = "<omgs>";		break;
				case 54: token = "<omg>";		break;
				case 55: token = "<value>";		break;
				case 56: token = "<return>";	break;
				case 57: token = "<function>";	break;
				case 58: token = "<args>";		break;
				case 59: token = "<arg>";		break;
				case 60: token = "<call>";		break;
				case 61: token = "<expr>";		break;
				case 62: token = "<arith>";		break;
				case 63: token = "<bool>";		break;
				case 64: token = "<comp>";		break;
			}

			IntegerCodes.addCode(token, i);
		}
	}

	/**
	 * Method that initializes all of the syntax rules for Mini-LOL
	 */
	public static void setPDA()
	{
		// TODO - how to know which int or const to push?!?!?!?!
		for(int i = 0; i <= 64; i++)
		{
			Rules.rules.put(i,new ArrayList<Rule>());
			switch (i)
			{

				// TODO - how to deal with stack marker (zo)
				case 0:
					Rules.rules.get(i).add(new Rule(i, 0, -1, i, 1, 42, 0, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 2, 0, 'b'));
					break;

				// TODO - how to do id
				// 1: [id]
				case 1:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 1, 'a'));
					break;

				// TODO - how to do const
				// 2: [const]
				case 2:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 2, 'a'));
					break;

				// 3-40: Keywords on stack top (Terminals)
				case 3: 	// HAI
				case 4:  	// KTHXBYE
				case 5:  	// GIMMEH
				case 6:  	// VISIBLE
				case 7:  	// I HAS A
				case 8:  	// ITZ A
				case 9:  	// NUMBR
				case 10: 	// NUMBAR
				case 11: 	// TROOF
				case 12: 	// WIN
				case 13: 	// FAIL
				case 14: 	// R
				case 15: 	// IM IN YR
				case 16: 	// WILE
				case 17: 	// IM OUTTA YR
				case 18: 	// O RLY?
				case 19: 	// YA RLY
				case 20: 	// NO WAI
				case 21: 	// OIC
				case 22: 	// WTF?
				case 23: 	// OMG
				case 24: 	// FOUND YR
				case 25: 	// GTFO
				case 26: 	// HOW IZ I
				case 27: 	// IF U SAY SO
				case 28: 	// YR
				case 29: 	// I IZ
				case 30: 	// MKAY
				case 31: 	// SUM OF
				case 32: 	// AN
				case 33: 	// DIFF OF
				case 34: 	// PRODUKT OF
				case 35: 	// QUOSHUNT OF
				case 36: 	// BOTH OF
				case 37: 	// EITHER OF
				case 38: 	// NOT
				case 39: 	// BOTH SAEM
				case 40: 	// DIFFRINT
				case 41: 	// ;
					Rules.rules.get(i).add(new Rule(i, 1, i, i, 1, i, 'a'));
					break;

				// 42: <lol>
				case 42:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{3,43,4}, 3, 'a'));
					break;

				// 43: <body>
				case 43:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{44,41,43}, new int[]{5,6,7,1,15,18,22,24,25,26,29}, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{4,20,17,21,23,27}, 'b'));
					break;

				// 44: <stmt>
				case 44:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 45, 5, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 46, 6, 'b'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 47, 7, 'c'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 49, 1, 'd'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 50, 15, 'e'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 51, 18, 'f'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 52, 22, 'g'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 56, new int[]{24,25}, 'h'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 57, 26, 'i'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 60, 29, 'j'));
					break;

				// 45: <input>
				case 45:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{5,1}, 5, 'a'));
					break;

				// 46: <output>
				case 46:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{6,61}, 6, 'a'));
					break;

				// 47: <decl>
				case 47:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{7,1,8,48}, 7, 'a'));
					break;

				// 48: <type>
				case 48:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 9, 9, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 10, 10, 'b'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 11, 11, 'c'));
					break;

				// 49: <asmt>
				case 49:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{1,14,61}, 1, 'a'));
					break;

				// 50: <loop>
				case 50:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{15,1,16,63,43,17,1}, 15, 'a'));
					break;

				// 51: <if>
				case 51:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{18,63,19,43,20,43,21}, 18, 'a'));
					break;

				// 52: <case>
				case 52:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{22,61,53,21}, 22, 'a'));
					break;

				// 53: <omgs>
				case 53:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{54,53}, 23, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 21, 'b'));
					break;

				// 54: <omg>
				case 54:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{23,55,43}, 23, 'a'));
					break;

				// 55: <value>
				case 55:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 2, 2, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 12, 12, 'b'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 13, 13, 'c'));
					break;

				// 56: <return>
				case 56:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{24,61}, 24, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 25, 25, 'b'));
					break;

				// 57: <function>
				case 57:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{26,1,58,43,27}, 26, 'a'));
					break;

				// 58: <args>
				case 58:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{59,58}, 28, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{27,30,5,6,7,1,15,18,22,24,25,26,29}, 'b'));
					break;

				// 59: <arg>
				case 59:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{28,1}, 28, 'a'));
					break;

				// 60: <call>
				case 60:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{29,1,58,30}, 29, 'a'));
					break;

				// 61: <expr>
				case 61:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 62, new int[]{31,33,34,35,1,2}, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 63, new int[]{36,37,38,39,40,12,13}, 'b'));
					break;

				// 62: <arith>
				case 62:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{31,62,32,62}, 31, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{33,62,32,62}, 33, 'b'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{34,62,32,62}, 34, 'c'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{35,62,32,62}, 35, 'd'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 1, 1, 'e'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 2, 2, 'f'));
					break;

				// 63: <bool>
				case 63:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{36,63,32,63}, 36, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{37,63,32,63}, 37, 'b'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{38,63}, 38, 'c'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 64, new int[]{39,40}, 'd'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 12, 12, 'e'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, 13, 13, 'f'));
					break;

				// 64: <comp>
				case 64:
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{39,61,32,61}, 39, 'a'));
					Rules.rules.get(i).add(new Rule(i, 1, -1, i, 1, new int[]{40,61,32,61}, 40, 'b'));
					break;


			}
		}
	}
}
