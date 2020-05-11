package Heuristics;

import at.fhooe.ai.rushhour.Heuristic;
import at.fhooe.ai.rushhour.Puzzle;
import at.fhooe.ai.rushhour.State;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a template for the class corresponding to your original
 * advanced heuristic.  This class is an implementation of the
 * <tt>Heuristic</tt> interface.  After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.
 */
public class AdvancedRecursiveHeuristic implements Heuristic
{
	private final int fixedPositionIndexCar;
	private final boolean orientationIndexCar;
	
	private final int maxRecursionDepth;
	
	/**
	 * This is the required constructor, which must be of the given form.
	 */
	public AdvancedRecursiveHeuristic(Puzzle puzzle)
	{
		fixedPositionIndexCar = puzzle.getFixedPosition(0);
		orientationIndexCar = puzzle.getCarOrient(0);
		
		maxRecursionDepth = puzzle.getNumCars() / 4;
	}
	
	/**
	 * This method returns the value of the heuristic function at the
	 * given state.
	 */
	public int getValue(State state)
	{
		if (state.isGoal()) { return 0; }
		
		int indexCarPosition = state.getVariablePosition(0);
		return GetCarsInWay(state, fixedPositionIndexCar, orientationIndexCar, indexCarPosition, true, 0, new ArrayList<>()).size();
	}
	
	private List<Integer> GetCarsInWay(State state, int row, boolean rowOrientation, int startPosition, boolean lookDirection, int recursionDepth, List<Integer> alreadyFoundCars)
	{
		int[][] grid = state.getGrid();
		
		int startIndex = lookDirection ? startPosition : 0;
		int endIndex = lookDirection ? state.getPuzzle().getGridSize() : startPosition;
		int initialCarIndex = rowOrientation ? grid[row][startPosition] : grid[startPosition][row];
		
		List<Integer> newlyFoundCars = new ArrayList<>();
		if (!alreadyFoundCars.contains(initialCarIndex)) { newlyFoundCars.add(initialCarIndex); }
		
		for (int i = startIndex; i < endIndex; i++)
		{
			int currentCar = rowOrientation ? grid[row][i] : grid[i][row];
			
			if (currentCar != -1 && currentCar != initialCarIndex && !alreadyFoundCars.contains(currentCar) && !newlyFoundCars.contains(currentCar))
			{
				newlyFoundCars.add(currentCar);
				
				if (recursionDepth < maxRecursionDepth)
				{
					int newRow = state.getPuzzle().getCarOrient(currentCar) == rowOrientation ? row : i;
					int newStartPosition = state.getPuzzle().getCarOrient(currentCar) == rowOrientation ? i : row;
					
					List<Integer> foundCarsLookingForward = GetCarsInWay(state, newRow, state.getPuzzle().getCarOrient(currentCar), newStartPosition, true, recursionDepth + 1, newlyFoundCars);
					List<Integer> foundCarsLookingBackward = GetCarsInWay(state, newRow, state.getPuzzle().getCarOrient(currentCar), newStartPosition, false, recursionDepth + 1, newlyFoundCars);
					
					newlyFoundCars.addAll(foundCarsLookingForward.size() < foundCarsLookingBackward.size() ? foundCarsLookingForward : foundCarsLookingBackward);
				}
			}
		}
		
		return newlyFoundCars;
	}
}
