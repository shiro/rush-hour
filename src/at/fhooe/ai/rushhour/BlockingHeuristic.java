package at.fhooe.ai.rushhour;

/**
 * This is a template for the class corresponding to the blocking heuristic.
 * This heuristic returns zero for goal states, and otherwise returns one plus
 * the number of cars blocking the path of the goal car to the exit. This class
 * is an implementation of the <tt>Heuristic</tt> interface, and must be
 * implemented by filling in the constructor and the <tt>getValue</tt> method.
 */
public class BlockingHeuristic implements Heuristic
{
	private final int fixedPositionIndexCar;
	private final boolean orientationIndexCar;
	
	
	/**
	 * This is the required constructor, which must be of the given form.
	 */
	public BlockingHeuristic(Puzzle puzzle)
	{
		fixedPositionIndexCar = puzzle.getFixedPosition(0);
		orientationIndexCar = puzzle.getCarOrient(0);
	}
	
	/**
	 * This method returns the value of the heuristic function at the given state.
	 */
	public int getValue(State state)
	{
		int[][] grid = state.getGrid();
		int indexCarPosition = state.getVariablePosition(0);
		
		int blockingCarCounter = 0;
		
		for (int i = indexCarPosition; i < grid.length; i++)
		{
			int currentCar = orientationIndexCar ? grid[fixedPositionIndexCar][i] : grid[i][fixedPositionIndexCar];
			
			if (currentCar != -1 && currentCar != 0) { blockingCarCounter++; }
		}
		
		return blockingCarCounter;
	}
}
