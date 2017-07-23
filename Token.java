package Utilities;

/**
 * This class holds simple tokens containing a name and a type.
 * Example types of types are: Keywords, Identifiers, Constants, and Special Symbols
 * @author kobypascual
 *
 */
public class Token
{
	/**
	 * Token name (value)
	 */
	String name;

	/**
	 * Token type (id,const)
	 */
	String type;

	/**
	 * Token Constructor.
	 * Token has a name and a type.
	 * @param name
	 * @param type
	 */
	public Token(String name, String type)
	{
		this.name = name;
		this.type = type;
	}

	// Accessors

	/**
	 * Return token name
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Return token type
	 * @return
	 */
	public String getType()
	{
		return type;
	}

	// Utilities

	/**
	 * Prints this token in format: name, type
	 */
	public void print()
	{
		System.out.println(this.name + ", " + this.type);
	}

	/**
	 * Checks if this token is equal to another given name and type.
	 * @param name
	 * @param type
	 * @return
	 */
	public boolean equals(String name, String type)
	{
		if(this.name.equals(name) && this.type.equals(type)) return true;
		else return false;
	}
}


