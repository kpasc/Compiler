package Utilities;

import java.util.ArrayList;

// TODO - stub code class
public class Rule
{
	/**
	 * Rule ID
	 */
	public int id;

	/**
	 * Integer representation of state before transition
	 */
	public int init_state;

	/**
	 * Integer representation of consumption symbol
	 */
	public int consume;

	/**
	 * Integer representation of stack top
	 */
	public int top;

	/**
	 * Integer representation of final state
	 */
	public int final_state;

	/**
	 * List of integers to be pushed onto the stack
	 */
	public ArrayList<Integer> toPush = new ArrayList<Integer>();

	/**
	 * List of integers that correspond to the look ahead values for this rule
	 */
	public ArrayList<Integer> lookahead = new ArrayList<Integer>();

	/**
	 *	Secondary id associated with the specific rule
	 */
	public char sub_id = ' ';

	/**
	 * Rule that pushes multiple items onto the stack
	 * and multiple lookahead values
	 * @param id
	 * @param init_state
	 * @param consume
	 * @param top
	 * @param final_state
	 * @param push
	 */
	public Rule(int id, int init_state, int consume, int top, int final_state, int[] push, int[] lookahead, char sub)
	{
		// TODO stubcode
		this.id = id;
		this.init_state = init_state;
		this.consume = consume;
		this.top = top;
		this.final_state = final_state;

		for(int i = 0; i < push.length; i++)
			this.toPush.add(push[i]);

		for(int i = 0; i < lookahead.length; i++)
			this.lookahead.add(lookahead[i]);

		this.sub_id = sub;
	}

	/**
	 * Rule that pushes multiple items onto the stack
	 * and only has one lookahead value
	 * @param id
	 * @param init_state
	 * @param consume
	 * @param top
	 * @param final_state
	 * @param push
	 */
	public Rule(int id, int init_state, int consume, int top, int final_state, int[] push, int lookahead, char sub)
	{
		// TODO stubcode
		this.id = id;
		this.init_state = init_state;
		this.consume = consume;
		this.top = top;
		this.final_state = final_state;

		for(int i = 0; i < push.length; i++)
			this.toPush.add(push[i]);

		this.lookahead.add(lookahead);

		this.sub_id = sub;
	}

	/**
	 * Rule that pushes only one item onto the stack
	 * and multiple lookahead values
	 * @param id
	 * @param init_state
	 * @param consume
	 * @param top
	 * @param final_state
	 */
	public Rule(int id, int init_state, int consume, int top, int final_state, int push, int[] lookahead, char sub)
	{
		// TODO stubcode
		this.id = id;
		this.init_state = init_state;
		this.consume = consume;
		this.top = top;
		this.final_state = final_state;
		this.toPush.add(push);

		for(int i = 0; i < lookahead.length; i++)
			this.lookahead.add(lookahead[i]);

		this.sub_id = sub;
	}

	/**
	 * Rule that pushes only one item onto the stack
	 * and only one look ahead values
	 * @param id
	 * @param init_state
	 * @param consume
	 * @param top
	 * @param final_state
	 */
	public Rule(int id, int init_state, int consume, int top, int final_state, int push, int lookahead, char sub)
	{
		// TODO stubcode
		this.id = id;
		this.init_state = init_state;
		this.consume = consume;
		this.top = top;
		this.final_state = final_state;
		this.toPush.add(push);
		this.lookahead.add(lookahead);
		this.sub_id = sub;
	}

	/**
	 * Rule that does not push anything onto the stack.
	 * and multiple lookaheads
	 * @param id
	 * @param init_state
	 * @param consume
	 * @param top
	 * @param final_state
	 */
	public Rule(int id, int init_state, int consume, int top, int final_state, int[] lookahead, char sub)
	{
		// TODO stubcode
		this.id = id;
		this.init_state = init_state;
		this.consume = consume;
		this.top = top;
		this.final_state = final_state;
		this.toPush = null;

		for(int i = 0; i < lookahead.length; i++)
			this.lookahead.add(lookahead[i]);

		this.sub_id = sub;
	}

	/**
	 * Rule that does not push anything onto the stack.
	 * and only one lookahead
	 * @param id
	 * @param init_state
	 * @param consume
	 * @param top
	 * @param final_state
	 */
	public Rule(int id, int init_state, int consume, int top, int final_state, int lookahead, char sub)
	{
		// TODO stubcode
		this.id = id;
		this.init_state = init_state;
		this.consume = consume;
		this.top = top;
		this.final_state = final_state;
		this.toPush = null;
		this.lookahead.add(lookahead);
		this.sub_id = sub;
	}
}
