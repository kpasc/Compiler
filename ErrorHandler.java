package Utilities;

import Driver.Compiler;

/**
 * Error Handler prints messages according to which error occurs.
 * Static class.
 * @author kobypascual
 *
 */
public final class ErrorHandler
{
	/**
	 * Empty constructor for ErrorHandler. Private since this class is static.
	 * Constructor will never be called.
	 */
	private ErrorHandler() {}

	////////////////////
	// Lexical Errors - Print out semantic errors
	////////////////////

	public static void semantic()
	{
		System.out.println("Semantic Error");
	}

	////////////////////
	// Lexical Errors - Print out lexical errors with the line number and which error
	////////////////////

	/**
	 * Print lexical error message for invalid keyword if findings are wanted to be printed
	 */
	public static void keywordLexical()
	{
		if(Compiler.getScannerPrintFindings()) System.out.print("Line#: " + Compiler.getLineNumber() + " - Lexical Error: Invalid Keyword");
	}

	/**
	 * Print lexical error message for invalid identifier if findings are wanted to be printed
	 */
	public static void idLexical()
	{
		if(Compiler.getScannerPrintFindings()) System.out.print("Line#: " + Compiler.getLineNumber() + " - Lexical Error: Invalid Identifier");
	}

	/**
	 * Print lexical error message for invalid constant if findings are wanted to be printed
	 */
	public static void constLexical()
	{
		if(Compiler.getScannerPrintFindings()) System.out.print("Line#: " + Compiler.getLineNumber() + " - Lexical Error: Invalid Constant");
	}

	////////////////////
	// Overflow Errors - Print out overflow error - table only has size 100
	////////////////////

	/**
	 * Table has only size 100. Cannot add more constants or identifiers.
	 */
	public static void tableFull()
	{
		if(Compiler.getScannerPrintFindings()) System.out.print("Table full. Cannot add more constants or identifiers");
	}
}
