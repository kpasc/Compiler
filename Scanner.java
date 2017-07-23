package Scanner;

import java.util.LinkedList;
import java.util.Queue;

import Utilities.ErrorHandler;
import Utilities.SymbolTable;

public final class Scanner
{
	/**
	 * true if currently building a keyword
	 */
	private static boolean keywordStatus = false;

	/**
	 * true if currently building an identifier
	 */
	private static  boolean idStatus = false;

	/**
	 * true if currently building a constant
	 */
	private static boolean constStatus = false;

	/**
	 * true if currently a special symbol
	 */
	private static boolean ssymbolStatus = false;

	/**
	 * true if currently a white space
	 */
	private static boolean whiteSpaceStatus = false;

	/**
	 * Holds current token value
	 */
	private static String currentToken = "";

	/**
	 * Holds tokens that were scanned
	 */
	public static Queue<String> scanned = new LinkedList<String>();

	/**
	 * Empty constructor for SymbolTable. Private since this class is static.
	 * Constructor will never be called.
	 */
	private Scanner() {}

	/**
	 * Scan a character and append it to the current token. Send the token to through the DFA and return the appropriate value to the Compiler call.
	 * @param character
	 * @return
	 */
	public static int scan(char character)
	{
		resetStatus();

		currentToken = currentToken + character; // append current token

		char firstChar = currentToken.charAt(0); // take first character of string. Depending on its value go to next state
		switch(firstChar)
		{
			// Keyword Routine
			case 'A':
			case 'B':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'K':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'V':
			case 'W':
			case 'Y':
				setKeywordStatus(); // building keyword
				int keyword = keywordRoutine(currentToken); // 1 if valid, 0 if need more characters, -1 if error
				if(keyword == -1) ErrorHandler.keywordLexical(); // call error handler
				return keyword;

			// Identifier Routine
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				setIdStatus(); // building id
				int id = identifierRoutine(currentToken); // value to be returned to compiler
				if(id == -1)
					ErrorHandler.idLexical(); // if id -1, call error handler
				return id;

			// Constant Routine
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				setConstStatus(); // building constant
				int constant = constantRoutine(currentToken); // value to be returned to compiler
				if(constant == -1)
					ErrorHandler.constLexical(); // if id -1, call error handler
				return constant;

			// Special Symbol Routine
			case ';':
				setSSStatus(); // building special symbol
				return specialSymbolRoutine(currentToken);

			// White Space Routine
			case ' ':
			case '	':
				setWhiteSpaceStatus(); // found white space
				return 1;

			// Error Routine
			default: return -2; // Error
		}
	}

	//******************************************
	// Scanner - DFA Routines (Determine Tokens)
	//******************************************

	//////////////////////////////////
	// Keyword Routine
	//////////////////////////////////

	/**
	 * Keyword routine will take the first letter in a keyword and send a substring to the next state. This substring is the second character
	 * of the string to the end.
	 * This keeps on occuring based on what the next character in the string is. If an impossible state is attempted to be reached, a -1 is propagated to
	 * keyword routine and keyword routine returns -1 as well, thus the error handler will be called.
	 * If no error occurs, but the string ends on a non-accepting state, then a 0 is propagated to keyword routine and keyword routine will return a 0. This
	 * will indicate another character needs to be scanned for this token to be recognized.
	 * If an accepting state is reached and the string has no more characters, a 1 will be returned to keyword routine and keyword routine will return a 1.
	 * In this case keyword is accepted.
	 * @param line
	 * @return
	 */
	private static int keywordRoutine(String line)
	{
		char c = line.charAt(0);
		switch(c)
		{
			// no C J L U X Z cases
			// GOTO A B D E F G H I K M N O P Q R S T V W Y
			case 'A': return keywordA(line.substring(1));
			case 'B': return keywordB(line.substring(1));
			case 'D': return keywordD(line.substring(1));
			case 'E': return keywordE(line.substring(1));
			case 'F': return keywordF(line.substring(1));
			case 'G': return keywordG(line.substring(1));
			case 'H': return keywordH(line.substring(1));
			case 'I': return keywordI(line.substring(1));
			case 'K': return keywordK(line.substring(1));
			case 'M': return keywordM(line.substring(1));
			case 'N': return keywordN(line.substring(1));
			case 'O': return keywordO(line.substring(1));
			case 'P': return keywordP(line.substring(1));
			case 'Q': return keywordQ(line.substring(1));
			case 'R': return keywordR(line.substring(1));
			case 'S': return keywordS(line.substring(1));
			case 'T': return keywordT(line.substring(1));
			case 'V': return keywordV(line.substring(1));
			case 'W': return keywordW(line.substring(1));
			case 'Y': return keywordY(line.substring(1));
			default: return -1; // Error adapt accordingly
		}
	}

		//////////
		// START A
		//////////

	private static int keywordA(String line)
	{
		// Start - A
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'N': return keywordAN(line.substring(1));
			default:  return -1; // error message
		}

	}
	private static int keywordAN(String line)
	{
		// Start - A - AN -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START B
		//////////

	private static int keywordB(String line)
	{
		// Start - B
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordBT(line.substring(1));
			case 'O': return keywordBO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBT(String line)
	{
		// Start - B - BT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'W': return keywordBTW(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBTW(String line)
	{
		// Start - B - BT -> BTW Accepting state - comment (need to ignore)
		if(line.isEmpty()) return 2; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordBO(String line)
	{
		// Start - B - BO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordBOT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOT(String line)
	{
		// Start - B - BO - BOT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'H': return keywordBOTH(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH(String line)
	{
		// Start - B - BO - BOT - BOTH
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordBOTH_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH_(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordBOTH_O(line.substring(1));
			case 'S': return keywordBOTH_S(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH_O(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_ - BOTH_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordBOTH_OF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH_OF(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_ - BOTH_O - BOTH_OF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordBOTH_S(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_ - BOTH_S
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordBOTH_SA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH_SA(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_ - BOTH_S - BOTH_SA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'E': return keywordBOTH_SAE(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH_SAE(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_ - BOTH_S - BOTH_SA - BOTH_SAE
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'M': return keywordBOTH_SAEM(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordBOTH_SAEM(String line)
	{
		// Start - B - BO - BOT - BOTH - BOTH_ - BOTH_S - BOTH_SA - BOTH_SAE - BOTH_SAEM -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START D
		//////////

	private static int keywordD(String line)
	{
		// Start - D
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordDI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDI(String line)
	{
		// Start - D - DI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordDIF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIF(String line)
	{
		// Start - D - DI - DIF
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordDIFF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIFF(String line)
	{
		// Start - D - DI - DIF - DIFF
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordDIFFR(line.substring(1));
			case ' ': return keywordDIFF_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIFFR(String line)
	{
		// Start - D - DI - DIF - DIFF - DIFFR
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordDIFFRI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIFFRI(String line)
	{
		// Start - D - DI - DIF - DIFF - DIFFR - DIFFRI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'N': return keywordDIFFRIN(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIFFRIN(String line)
	{
		// Start - D - DI - DIF - DIFF - DIFFR - DIFFRI - DIFFRIN
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordDIFFRINT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIFFRINT(String line)
	{
		// Start - D - DI - DIF - DIFF - DIFFR - DIFFRI - DIFFRIN - DIFFRINT -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordDIFF_(String line)
	{
		// Start - D - DI - DIF - DIFF - DIFF_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordDIFF_O(line.substring(1));
			default: return -1; // error message
		}

	}
	private static int keywordDIFF_O(String line)
	{
		// Start - D - DI - DIF - DIFF - DIFF_ - DIFF_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordDIFF_OF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordDIFF_OF(String line)
	{
		// Start - D - DI - DIF - DIFF _ DIFF_ - DIFF_O - DIFF_OF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				 // Error, more characters but no more possible keywords
	}

		//////////
		// START E
		//////////

	private static int keywordE(String line)
	{
		// Start - E
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordEI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEI(String line)
	{
		// Start - E - EI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordEIT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEIT(String line)
	{
		// Start - E - EI - EIT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'H': return keywordEITH(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEITH(String line)
	{
		// Start - E - EI - EIT - EITH
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'E': return keywordEITHE(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEITHE(String line)
	{
		// Start - E - EI - EIT - EITH - EITHE
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordEITHER(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEITHER(String line)
	{
		// Start - E - EI - EIT - EITH - EITHE - EITHER
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordEITHER_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEITHER_(String line)
	{
		// Start - E - EI - EIT - EITH - EITHE - EITHER - EITHER_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordEITHER_O(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEITHER_O(String line)
	{
		// Start - E - EI - EIT - EITH - EITHE - EITHER - EITHER_ - EITHER_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordEITHER_OF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordEITHER_OF(String line)
	{
		// Start - E - EI - EIT - EITH - EITHE - EITHER - EITHER_ - EITHER_O - EITHER_OF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START F
		//////////

	private static int keywordF(String line)
	{
		// Start - F
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordFA(line.substring(1));
			case 'O': return keywordFO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFA(String line)
	{
		// Start - F - FA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordFAI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFAI(String line)
	{
		// Start - F - FA - FAI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'L': return keywordFAIL(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFAIL(String line)
	{
		// Start - F - FA - FAI - FAIL -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordFO(String line)
	{
		// Start - F - FO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordFOU(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFOU(String line)
	{
		// Start - F - FO - FOU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'N': return keywordFOUN(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFOUN(String line)
	{
		// Start - F - FO - FOU - FOUN
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'D': return keywordFOUND(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFOUND(String line)
	{
		// Start - F - FO - FOU - FOUN - FOUND
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordFOUND_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFOUND_(String line)
	{
		// Start - F - FO - FOU - FOUN - FOUND - FOUND_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordFOUND_Y(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFOUND_Y(String line)
	{
		// Start - F - FO - FOU - FOUN - FOUND - FOUND_ - FOUND_Y
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordFOUND_YR(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordFOUND_YR(String line)
	{
		// Start - F - FO - FOU - FOUN - FOUND - FOUND_ - FOUND_Y - FOUND_YR -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START G
		//////////

	private static int keywordG(String line)
	{
		// Start - G
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordGT(line.substring(1));
			case 'I': return keywordGI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGT(String line)
	{
		// Start - G - GT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordGTF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGTF(String line)
	{
		// Start - G - GT - GTF
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordGTFO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGTFO(String line)
	{
		// Start - G - GT - GTF - GTFO -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords

	}
	private static int keywordGI(String line)
	{
		// Start - G - GI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'M': return keywordGIM(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGIM(String line)
	{
		// Start - G - GI - GIM
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'M': return keywordGIMM(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGIMM(String line)
	{
		// Start - G - GI - GIM - GIMM
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'E': return keywordGIMME(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGIMME(String line)
	{
		// Start - G - GI - GIM - GIMM - GIMME
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'H': return keywordGIMMEH(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordGIMMEH(String line)
	{
		// Start - G - GI - GIM - GIMM - GIMME - GIMMEH -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;	  			 // Error, more characters but no more possible keywords
	}

		//////////
		// START H
		//////////

	private static int keywordH(String line)
	{
		// Start - H
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordHA(line.substring(1));
			case 'O': return keywordHO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHA(String line)
	{
		// Start - H - HA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordHAI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHAI(String line)
	{
		// Start - H - HA - HAI -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordHO(String line)
	{
		// Start - H - HO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'W': return keywordHOW(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHOW(String line)
	{
		// Start - H - HO - HOW
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordHOW_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHOW_(String line)
	{
		// Start - H - HO - HOW - HOW_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordHOW_I(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHOW_I(String line)
	{
		// Start - H - HO - HOW - HOW_ - HOW_I
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Z': return keywordHOW_IZ(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHOW_IZ(String line)
	{
		// Start - H - HO - HOW - HOW_ - HOW_I - HOW_IZ
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordHOW_IZ_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHOW_IZ_(String line)
	{
		// Start - H - HO - HOW - HOW_ - HOW_I - HOW_IZ - HOW_IZ_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordHOW_IZ_I(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordHOW_IZ_I(String line)
	{
		// Start - H - HO - HOW - HOW_ - HOW_I - HOW_IZ - HOW_IZ_ - HOW_IZ_I -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START H
		//////////

	private static int keywordI(String line)
	{
		// Start - I
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordI_(line.substring(1));
			case 'F': return keywordIF(line.substring(1));
			case 'M': return keywordIM(line.substring(1));
			case 'T': return keywordIT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_(String line)
	{
		// Start - I - I_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordI_I(line.substring(1));
			case 'H': return keywordI_H(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_I(String line)
	{
		// Start - I - I_ - I_I
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Z': return keywordI_IZ(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_IZ(String line)
	{
		// Start - I - I_ - I_I - I_IZ -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordI_H(String line)
	{
		// Start - I - I_ - I_H
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordI_HA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_HA(String line)
	{
		// Start - I - I_ - I_HA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'S': return keywordI_HAS(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_HAS(String line)
	{
		// Start - I - I_ - I_HAS
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordI_HAS_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_HAS_(String line)
	{
		// Start - I - I_ - I_HAS - I_HAS_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordI_HAS_A(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordI_HAS_A(String line)
	{
		// Start - I - I_ - I_HAS - I_HAS_ - I_HAS_A -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordIT(String line)
	{
		// Start - I - IT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Z': return keywordITZ(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordITZ(String line)
	{
		// Start - I - IT - ITZ
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordITZ_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordITZ_(String line)
	{
		// Start - I - IT - ITZ - ITZ_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordITZ_A(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordITZ_A(String line)
	{
		// Start - I - IT - ITZ - ITZ_ - ITZ_A -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordIF(String line)
	{
		// Start - I - IF
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordIF_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_(String line)
	{
		// Start - I - IF - IF_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordIF_U(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U(String line)
	{
		// Start - I - IF - IF_ - IF_U
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordIF_U_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'S': return keywordIF_U_S(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_S(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_ - IF_U_S
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordIF_U_SA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_SA(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_ - IF_U_S - IF_U_SA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordIF_U_SAY(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_SAY(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_ - IF_U_S - IF_U_SA - IF_U_SAY
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordIF_U_SAY_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_SAY_(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_ - IF_U_S - IF_U_SA - IF_U_SAY - IF_U_SAY_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'S': return keywordIF_U_SAY_S(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_SAY_S(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_ - IF_U_S - IF_U_SA - IF_U_SAY - IF_U_SAY_ - IF_U_SAY_S
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordIF_U_SAY_SO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIF_U_SAY_SO(String line)
	{
		// Start - I - IF - IF_ - IF_U - IF_U_ - IF_U_S - IF_U_SA - IF_U_SAY - IF_U_SAY_ - IF_U_SAY_S - IF_U_SAY_SO -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordIM(String line)
	{
		// Start - I - IM
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordIM_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_(String line)
	{
		// Start - I - IM - IM_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordIM_I(line.substring(1));
			case 'O': return keywordIM_O(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_I(String line)
	{
		// Start - I - IM - IM_ - IM_I
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'N': return keywordIM_IN(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_IN(String line)
	{
		// Start - I - IM - IM_ - IM_I - IM_IN
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordIM_IN_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_IN_(String line)
	{
		// Start - I - IM - IM_ - IM_I - IM_IN - IM_IN_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordIM_IN_Y(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_IN_Y(String line)
	{
		// Start - I - IM - IM_ - IM_I - IM_IN - IM_IN_ - IM_IN_Y
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordIM_IN_YR(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_IN_YR(String line)
	{
		// Start - I - IM - IM_ - IM_I - IM_IN - IM_IN_ - IM_IN_Y - IM_IN_YR -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordIM_O(String line)
	{
		// Start - I - IM - IM_ - IM_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordIM_OU(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OU(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordIM_OUT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OUT(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU - IM_OUT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordIM_OUTT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OUTT(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU - IM_OUT - IM_OUTT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordIM_OUTTA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OUTTA(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU - IM_OUT - IM_OUTT - IM_OUTTA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordIM_OUTTA_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OUTTA_(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU - IM_OUT - IM_OUTT - IM_OUTTA - IM_OUTTA_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordIM_OUTTA_Y(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OUTTA_Y(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU - IM_OUT - IM_OUTT - IM_OUTTA - IM_OUTTA_ - IM_OUTTA_Y
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordIM_OUTTA_YR(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordIM_OUTTA_YR(String line)
	{
		// Start - I - IM - IM_ - IM_O - IM_OU - IM_OUT - IM_OUTT - IM_OUTTA - IM_OUTTA_ - IM_OUTTA_Y - IM_OUTTA_YR -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START K
		//////////

	private static int keywordK(String line)
	{
		// Start - K
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordKT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordKT(String line)
	{
		// Start - K - KT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'H': return keywordKTH(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordKTH(String line)
	{
		// Start - K - KT - KTH
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'X': return keywordKTHX(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordKTHX(String line)
	{
		// Start - K - KT - KTH - KTHX
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'B': return keywordKTHXB(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordKTHXB(String line)
	{
		// Start - K - KT - KTH - KTHX - KTHXB
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordKTHXBY(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordKTHXBY(String line)
	{
		// Start - K - KT - KTH - KTHX - KTHXB - KTHXBY
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'E': return keywordKTHXBYE(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordKTHXBYE(String line)
	{
		// Start - K - KT - KTH - KTHX - KTHXB - KTHXBY - KTHXBYE -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START M
		//////////

	private static int keywordM(String line)
	{
		// Start - M
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'K': return keywordMK(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordMK(String line)
	{
		// Start - M - MK
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordMKA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordMKA(String line)
	{
		// Start - M - MK - MKA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordMKAY(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordMKAY(String line)
	{
		// Start - M - MK - MKA - MKAY -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START N
		//////////

	private static int keywordN(String line)
	{
		// Start - N
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordNU(line.substring(1));
			case 'O': return keywordNO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNU(String line)
	{
		// Start - N - NU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'M': return keywordNUM(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNUM(String line)
	{
		// Start - N - NU - NUM
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'B': return keywordNUMB(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNUMB(String line)
	{
		// Start - N - NU - NUM - NUMB
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordNUMBR(line.substring(1));
			case 'A': return keywordNUMBA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNUMBR(String line)
	{
		// Start - N - NU - NUM - NUMB - NUMBR -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordNUMBA(String line)
	{
		// Start - N - NU - NUM - NUMB - NUMBA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordNUMBAR(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNUMBAR(String line)
	{
		// Start - N - NU - NUM - NUMB - NUMBA - NUMBAR -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordNO(String line)
	{
		// Start - N - NO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordNOT(line.substring(1));
			case ' ': return keywordNO_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNOT(String line)
	{
		// Start - N - NO - NOT -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordNO_(String line)
	{
		// Start - N - NO - NO_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'W': return keywordNO_W(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNO_W(String line)
	{
		// Start - N - NO - NO_ - NO_W
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'A': return keywordNO_WA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNO_WA(String line)
	{
		// Start - N - NO - NO_ - NO_W - NO_WA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordNO_WAI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordNO_WAI(String line)
	{
		// Start - N - NO - NO_ - NO_W - NO_WA - NO_WAI -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}


		//////////
		// START O
		//////////

	private static int keywordO(String line)
	{
		// Start - O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordOI(line.substring(1));
			case 'M': return keywordOM(line.substring(1));
			case ' ': return keywordO_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordOI(String line)
	{
		// Start - O - OI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'C': return keywordOIC(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordOIC(String line)
	{
		// Start - O - OI - OIC -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordOM(String line)
	{
		// Start - O - OM
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'G': return keywordOMG(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordOMG(String line)
	{
		// Start - O - OM - OMG -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordO_(String line)
	{
		// Start - O - O_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordO_R(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordO_R(String line)
	{
		// Start - O - O_ - O_R
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'L': return keywordO_RL(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordO_RL(String line)
	{
		// Start - O - O_ - O_R - O_RL
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordO_RLY(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordO_RLY(String line)
	{
		// Start - O - O_ - O_R - O_RL - O_RLY
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case '?': return keywordO_RLYQ(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordO_RLYQ(String line)
	{
		// Start - O - O_ - O_R - O_RL - O_RLY - O_RLY? -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START P
		//////////

	private static int keywordP(String line)
	{
		// Start - P
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordPR(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPR(String line)
	{
		// Start - P - PR
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordPRO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRO(String line)
	{
		// Start - P - PR - PRO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'D': return keywordPROD(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPROD(String line)
	{
		// Start - P - PR - PRO - PROD
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordPRODU(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRODU(String line)
	{
		// Start - P - PR - PRO - PROD - PRODU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'K': return keywordPRODUK(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRODUK(String line)
	{
		// Start - P - PR - PRO - PROD - PRODU - PRODUK
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordPRODUKT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRODUKT(String line)
	{
		// Start - P - PR - PRO - PROD - PRODU - PRODUK - PRODUKT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordPRODUKT_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRODUKT_(String line)
	{
		// Start - P - PR - PRO - PROD - PRODU - PRODUK - PRODUKT - PRODUKT_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordPRODUKT_O(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRODUKT_O(String line)
	{
		// Start - P - PR - PRO - PROD - PRODU - PRODUK - PRODUKT - PRODUKT_ - PRODUKT_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordPRODUKT_OF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordPRODUKT_OF(String line)
	{
		// Start - P - PR - PRO - PROD - PRODU - PRODUK - PRODUKT - PRODUKT_ - PRODUKT_O - PRODUKT_OF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START Q
		//////////

	private static int keywordQ(String line)
	{
		// Start - Q
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordQU(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQU(String line)
	{
		// Start - Q - QU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordQUO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUO(String line)
	{
		// Start - Q - QU - QUO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'S': return keywordQUOS(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOS(String line)
	{
		// Start - Q - QU - QUO - QUOS
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'H': return keywordQUOSH(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSH(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordQUOSHU(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSHU(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH - QUOSHU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'N': return keywordQUOSHUN(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSHUN(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH - QUOSHU - QUOSHUN
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'T': return keywordQUOSHUNT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSHUNT(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH - QUOSHU - QUOSHUN - QUOSHUNT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordQUOSHUNT_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSHUNT_(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH - QUOSHU - QUOSHUN - QUOSHUNT - QUOSHUNT_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordQUOSHUNT_O(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSHUNT_O(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH - QUOSHU - QUOSHUN - QUOSHUNT - QUOSHUNT_ - QUOSHUNT_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordQUOSHUNT_OF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordQUOSHUNT_OF(String line)
	{
		// Start - Q - QU - QUO - QUOS - QUOSH - QUOSHU - QUOSHUN - QUOSHUNT - QUOSHUNT_ - QUOSHUNT_O - QUOSHUNT_OF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START R
		//////////

	private static int keywordR(String line)
	{
		// Start - R -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
		//////////
		// START S
		//////////

	private static int keywordS(String line)
	{
		// Start - S
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'U': return keywordSU(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordSU(String line)
	{
		// Start - S - SU
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'M': return keywordSUM(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordSUM(String line)
	{
		// Start - S - SU - SUM
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordSUM_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordSUM_(String line)
	{
		// Start - S - SU - SUM - SUM_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordSUM_O(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordSUM_O(String line)
	{
		// Start - S - SU - SUM - SUM_ - SUM_O
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordSUM_OF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordSUM_OF(String line)
	{
		// Start - S - SU - SUM - SUM_ - SUM_O - SUM_OF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START T
		//////////

	private static int keywordT(String line)
	{
		// Start - T
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordTR(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordTR(String line)
	{
		// Start - T - TR
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordTRO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordTRO(String line)
	{
		// Start - T - TR - TRO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'O': return keywordTROO(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordTROO(String line)
	{
		// Start - T - TR - TRO - TROO
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordTROOF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordTROOF(String line)
	{
		// Start - T - TR - TRO - TROO - TROOF -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START V
		//////////

	private static int keywordV(String line)
	{
		// Start - V
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordVI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordVI(String line)
	{
		// Start - V - VI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'S': return keywordVIS(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordVIS(String line)
	{
		// Start - V - VI - VIS
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordVISI(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordVISI(String line)
	{
		// Start - V - VI - VIS - VISI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'B': return keywordVISIB(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordVISIB(String line)
	{
		// Start - V - VI - VIS - VISI - VISIB
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'L': return keywordVISIBL(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordVISIBL(String line)
	{
		// Start - V - VI - VIS - VISI - VISIB - VISIBL
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'E': return keywordVISIBLE(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordVISIBLE(String line)
	{
		// Start - V - VI - VIS - VISI - VISIB - VISIBL - VISIBLE -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START W
		//////////

	private static int keywordW(String line)
	{
		// Start - W
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'I': return keywordWI(line.substring(1));
			case 'T': return keywordWT(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordWI(String line)
	{
		// Start - W - WI
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'N': return keywordWIN(line.substring(1));
			case 'L': return keywordWIL(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordWIN(String line)
	{
		// Start - W - WI - WIN -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordWIL(String line)
	{
		// Start - W - WI - WIL
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'E': return keywordWILE(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordWILE(String line)
	{
		// Start - W - WI - WIL - WILE -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordWT(String line)
	{
		// Start - W - WT
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'F': return keywordWTF(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordWTF(String line)
	{
		// Start - W - WT - WTF
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case '?': return keywordWTFQ(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordWTFQ(String line)
	{
		// Start - W - WT - WTF - WTF? -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

		//////////
		// START Y
		//////////

	private static int keywordY(String line)
	{
		// Start - Y
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordYR(line.substring(1));
			case 'A': return keywordYA(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordYR(String line)
	{
		// Start - Y - YR -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}
	private static int keywordYA(String line)
	{
		// Start - Y - YA
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case ' ': return keywordYA_(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordYA_(String line)
	{
		// Start - Y - YA - YA_
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'R': return keywordYA_R(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordYA_R(String line)
	{
		// Start - Y - YA - YA_ - YA_R
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'L': return keywordYA_RL(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordYA_RL(String line)
	{
		// Start - Y - YA - YA_ - YA_R - YA_RL
		if(line.isEmpty()) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case 'Y': return keywordYA_RLY(line.substring(1));
			default: return -1; // error message
		}
	}
	private static int keywordYA_RLY(String line)
	{
		// Start - Y - YA - YA_ - YA_R - YA_RL - YA_RLY -> Accept
		if(line.isEmpty()) return 1; // Accept, no more characters
		else return -1;				// Error, more characters but no more possible keywords
	}

	//////////////////////////////////
	// Identifier Routine
	//////////////////////////////////

	/**
	 * Returns a 1 or -1. Takes first letter of string and passes a substring to idAllSymbols.
	 * If returns 1 it is a valid id, -1 is invalid
	 * @param line
	 * @return
	 */
	private static int identifierRoutine(String line)
	{
		char c = line.charAt(0);
		switch(c)
		{
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				return identifierAllSymbols(line.substring(1));
			default: return -1; // if it starts with something else it's an error
		}
	}

	/**
	 * Returns a 1 or -1. Takes first letter of string and passes a substring to idAllSymbols.
	 * If returns 1 it is a valid id, -1 is invalid
	 * @param line
	 * @return
	 */
	private static int identifierAllSymbols(String line)
	{
		if(line.isEmpty()) return 1;
		else
		{
			char c = line.charAt(0);
			switch(c)
			{
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
				case 'g':
				case 'h':
				case 'i':
				case 'j':
				case 'k':
				case 'l':
				case 'm':
				case 'n':
				case 'o':
				case 'p':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'u':
				case 'v':
				case 'w':
				case 'x':
				case 'y':
				case 'z':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '_':
					return identifierAllSymbols(line.substring(1));
				default: return -1; // error!!!
			}
		}

	}

	//////////////////////////////////
	// Constant Routine
	//////////////////////////////////

	/**
	 * Returns a 1 or -1. Takes first number of string and passes a substring to constAll or constantNeedDigit
	 * If returns 1 it is a valid id, -1 is invalid
	 * @param line
	 * @return
	 */
	private static int constantRoutine(String line)
	{
		char c = line.charAt(0);
		switch(c)
		{
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return constantAll(line.substring(1));
			case '.':
				return constantNeedDigit(line.substring(1));
			default: return -1; // error!
		}

	}

	/**
	 * Returns a 1 or -1. Takes first letter of string and passes a substring to constantAll or constatDigits.
	 * If returns 1 it is a valid id, -1 is invalid
	 * @param line
	 * @return
	 */
	private static int constantAll(String line)
	{
		if(line.length() == 0) return 1;
		char c = line.charAt(0);
		switch(c)
		{
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return constantAll(line.substring(1));
			case '.':
				return constantDigits(line.substring(1));
			default: return -1; // error!
		}
	}

	/**
	 * Returns a 1 or -1. Takes first number of string and passes a substring to constatDigits. Can only be digits.
	 * If returns 1 it is a valid id, -1 is invalid
	 * @param line
	 * @return
	 */
	private static int constantDigits(String line)
	{
		if(line.length() == 0) return 1;
		char c = line.charAt(0);
		switch(c)
		{
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return constantDigits(line.substring(1));
			default: return -1; // error!
		}

	}

	/**
	 * Returns a 1 or -1. Takes first number of string and passes a substring to constatDigits. Has to be a digit next. If in this state the first character
	 * was a decimal.
	 * If returns 1 it is a valid id, -1 is invalid
	 * @param line
	 * @return
	 */
	private static int constantNeedDigit(String line)
	{
		if(line.length() == 0) return 0;
		char c = line.charAt(0);
		switch(c)
		{
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return constantDigits(line.substring(1));
			default: return -1; // error!
		}

	}

	//////////////////////////////////
	// Special Symbol Routine
	//////////////////////////////////

	/**
	 * If we get here it is a special symbol. Return 1.
	 * @param line
	 * @return
	 */
	private static int specialSymbolRoutine(String line)
	{
		// Start - ; -> Accept
		if(line.charAt(0) == ';') return 1;
		else return -1;
	}

	//////////////////////////////////
	// Scanner - Utilities
	//////////////////////////////////

	/**
	 * Return keywordStatus
	 * @return
	 */
	public static boolean keywordStatus()
	{
		return keywordStatus;
	}

	/**
	 * Return idStatus
	 * @return
	 */
	public static boolean idStatus()
	{
		return idStatus;
	}

	/**
	 * Return constStatus
	 * @return
	 */
	public static boolean constStatus()
	{
		return constStatus;
	}

	/**
	 * return ssymbolStatus
	 * @return
	 */
	public static boolean ssymbolStatus()
	{
		return ssymbolStatus;
	}

	/**
	 * return whiteSpaceStatus
	 * @return
	 */
	public static boolean whiteSpaceStatus()
	{
		return whiteSpaceStatus;
	}

	/**
	 * Set all status to false
	 */
	public static void resetStatus()
	{
		keywordStatus = false;
		idStatus = false;
		constStatus = false;
		ssymbolStatus = false;
		whiteSpaceStatus = false;
	}

	/**
	 * Set all status to false, but keyword to true
	 */
	public static void setKeywordStatus()
	{
		keywordStatus = true;
		idStatus = false;
		constStatus = false;
		ssymbolStatus = false;
		whiteSpaceStatus = false;
	}

	/**
	 * Set all status to false, but id to true
	 */
	public static void setIdStatus()
	{
		keywordStatus = false;
		idStatus = true;
		constStatus = false;
		ssymbolStatus = false;
		whiteSpaceStatus = false;
	}

	/**
	 * Set all status to false, but const to true
	 */
	public static void setConstStatus()
	{
		keywordStatus = false;
		idStatus = false;
		constStatus = true;
		ssymbolStatus = false;
		whiteSpaceStatus = false;
	}

	/**
	 * Set all status to false, but ss to true
	 */
	public static void setSSStatus()
	{
		keywordStatus = false;
		idStatus = false;
		constStatus = false;
		ssymbolStatus = true;
		whiteSpaceStatus = false;
	}

	/**
	 * Set all status to false, but whitespace to true
	 */
	public static void setWhiteSpaceStatus()
	{
		keywordStatus = false;
		idStatus = false;
		constStatus = false;
		ssymbolStatus = false;
		whiteSpaceStatus = true;
	}

	/**
	 * Reset currentToken to empty
	 */
	public static void resetToken()
	{
		currentToken = "";
	}

	/**
	 * Add a new value (constant or identifier)
	 * @param type
	 */
	public static void newValue(String type)
	{
		if(!SymbolTable.checkIfExists(currentToken, type))
			SymbolTable.add(currentToken, type);
	}

	/**
	 * Returns the next token (lookahead token)
	 */
	public static Integer getLookAhead()
	{
		boolean id = SymbolTable.checkIfExists(scanned.peek(), "identifier");
		boolean constant = SymbolTable.checkIfExists(scanned.peek(), "constant");

		if(id == true && constant == false) return 1;
		else if(id == false && constant == true) return 2;
		return IntegerCodes.findCode(scanned.peek());
	}
}
