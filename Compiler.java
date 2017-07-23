package Driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Parser.Parser;
import Utilities.SymbolTable;


/**
 *
 * @author kobypascual
 * This project is for CS 4323 Compiler Construction Course.
 * The goal is to create a compiler for a modified (simpler) LOL - language.
 * This class is the driver class, holds main()
 */
public class Compiler
{
	/**
	 * Holds the current line number
	 */
	public static int linenumber = 0;

	/**
	 * If we want to print input file
	 */
	private static boolean printInput = true;

	/**
	 * If we want to print findings
	 */
	public static boolean printScannerFindings = false;

	/**
	 * If we want to print parser findings
	 */
	public static boolean printParserFindings = true;

	/**
	 * If we want to print additions in real time to the symbol table
	 */
	private static boolean printAdditions = false;

	/**
	 * If we want to print symbol table
	 */
	private static boolean printSymtab = true;

	/**
	 * List of strings that holds each line of the input file
	 */
	public static ArrayList<String> file = null;

	/**
	 * Main method. Driver.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Delete if I don't want to use non-static methods in this class
		Compiler driver = new Compiler();

		// Open this file
		String fileName = "input.txt";

		// Store one line at a time
		file = driver.readFile(fileName);

		// 0. Compiler output
		System.out.println("COMPILER OUTPUT:");
		System.out.println("");

		// 1. Print the input file
		if(printInput)
		{
			System.out.println("Input File:");
			for(int currentLine = 0; currentLine < file.size(); currentLine++)
			{
				System.out.println(file.get(currentLine));
			}
			System.out.println("");
		}

		// 2. Call Parser to begin parsing
		Parser.parse();

		// 3. Print table
		if(printSymtab)
		{
			System.out.println(""); // white space for cleanliness
			SymbolTable.printTable();
		}
	}

	/**
	 * Reads the file and stores each line of the file into an ArrayList<String>
	 * @param fileName
	 * @return
	 */
	private ArrayList<String> readFile(String fileName)
	{
		ArrayList<String> file = new ArrayList<String>();
		String line;
		try
		{
		FileReader fileReader = new FileReader(fileName);

		BufferedReader bufferedReader = new BufferedReader(fileReader);


			while((line = bufferedReader.readLine()) != null)
			{
				file.add(line);
			}

			bufferedReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Returns the current linenumber
	 * @return
	 */
	public static int getLineNumber()
	{
		return linenumber;
	}

	/**
	 * Returns printFindings - used by error handler on whether or not to print errors to console
	 * @return
	 */
	public static boolean getScannerPrintFindings()
	{
		return printScannerFindings;
	}

	/**
	 * Returns printAdditions - used by symbol table on whether or not to print when additions to the table occur
	 * @return
	 */
	public static boolean getPrintAdditions()
	{
		return printAdditions;
	}

}
