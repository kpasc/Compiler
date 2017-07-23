package Utilities;

import Driver.Compiler;

/**
 * Static class. Holds all id's and constant's found by the scanner.
 * @author kobypascual
 *
 */
public final class SymbolTable
{

	/**
	 * nextEmpty stores the first null value of the symtab
	 */
	private static int nextEmpty = 0;

	/**
	 * Symbol Table is an array of size 100 of Tokens
	 */
	private static Token[] symtab = new Token[100];

	/**
	 * Empty constructor for SymbolTable. Private since this class is static.
	 * Constructor will never be called.
	 */
	private SymbolTable() {}

	///////////////////////////
	// Public - Accessors
	///////////////////////////

	///////////////////////////
	// Public - Mutators
	///////////////////////////

	/**
	 * Adds a constant or id to the symtab
	 * @param name
	 * @param type
	 */
	public static void add(String name, String type)
	{
		// If table is full we cannot add more
		if(nextEmpty == 100)
		{
			// Call error handler
			ErrorHandler.tableFull();
			return;
		}

		// Add new token to next null value
		symtab[nextEmpty] = new Token(name, type);

		// Print added value if printAdditions is true
		if(Compiler.getPrintAdditions()) System.out.println(type + " added: " + name);

		// Increment nextEmpty
		nextEmpty += 1;
	}

	///////////////////////////
	// Public - Utilities
	///////////////////////////

	/**
	 * Check if constant or id already exists
	 * @param currentToken
	 * @param type
	 * @return
	 */
	public static boolean checkIfExists(String currentToken, String type)
	{
		// Next check if id or const already exists return true if so
		for(int i = 0; i < nextEmpty; i++)
		{
			// Check if current token equals current token in symtab
			if(symtab[i].equals(currentToken, type)) return true;
		}

		return false;
	}

	/**
	 * Prints the symtab
	 */
	public static void printTable()
	{
		// Print the symbol table
		System.out.println("Symbol Table:");
		for(int i = 0; i < nextEmpty; i++)
		{
			symtab[i].print();
		}
	}
}
