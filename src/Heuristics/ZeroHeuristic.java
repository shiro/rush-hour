package Heuristics;

import at.fhooe.ai.rushhour.Heuristic;
import at.fhooe.ai.rushhour.Puzzle;
import at.fhooe.ai.rushhour.State;

/**
 * This is a trivial heuristic function that always returns zero.
 */
public class ZeroHeuristic implements Heuristic
{
	/**
	 * A vacuous constructor, provided in this form for consistency with the other
	 * <tt>Heuristic</tt> implementations.
	 */
	public ZeroHeuristic(Puzzle puzzle)
	{
	
	}
	
	/**
	 * Returns the value of the heuristic, which is always zero.
	 */
	public int getValue(State state)
	{
		return 0;
	}
}
