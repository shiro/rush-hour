package at.fhooe.ai.rushhour;

/**
 * This is a template for the class corresponding to your original
 * advanced heuristic.  This class is an implementation of the
 * <tt>Heuristic</tt> interface.  After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.
 */
public class AdvancedHeuristic implements Heuristic
{
	private final int fixedPositionIndexCar;
	private final boolean orientationIndexCar;
	
	/**
	 * This is the required constructor, which must be of the given form.
	 */
	public AdvancedHeuristic(Puzzle puzzle)
	{
		fixedPositionIndexCar = puzzle.getFixedPosition(0);
		orientationIndexCar = puzzle.getCarOrient(0);
	}
	
	/**
	 * This method returns the value of the heuristic function at the
	 * given state.
	 */
	public int getValue(State state)
	{
		int[][] grid = state.getGrid();
		int indexCarPosition = state.getVariablePosition(0);
		
		int heuristicsValue = 0;
		
		for (int i = indexCarPosition; i < grid.length; i++)
		{
			int currentCar = orientationIndexCar ? grid[fixedPositionIndexCar][i] : grid[i][fixedPositionIndexCar];
			
			if (currentCar != -1 && currentCar != 0) { heuristicsValue += MinAmountOfMovesToUnblock(state, currentCar, i, orientationIndexCar); }
		}
		
		return heuristicsValue + 1;
	}
	
	private int MinAmountOfMovesToUnblock(State state, int carIndex, int row, boolean rowOrientation)
	{
		int blockingCarPosition = state.getVariablePosition(carIndex);
		int blockingCarSize = state.getPuzzle().getCarSize(carIndex);
		
		return row - blockingCarPosition;
		//return (blockingCarSize <= row && blockingCarPosition < row) ? blockingCarPosition : state.getPuzzle().getGridSize() - blockingCarPosition - blockingCarSize;
	}
}
