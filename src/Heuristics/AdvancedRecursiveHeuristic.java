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
	private final int maxRecursionDepth;
	
	/**
	 * This is the required constructor, which must be of the given form.
	 */
	public AdvancedRecursiveHeuristic(Puzzle puzzle)
	{
		maxRecursionDepth = puzzle.getNumCars();
	}
	
	/**
	 * This method returns the value of the heuristic function at the
	 * given state.
	 */
	public int getValue(State state)
	{
		if (state.isGoal()) { return 0; }
		
		int indexCarPosition = state.getVariablePosition(0);
		List<Integer> carsInTheWay = GetCarsInWay(state, state.getPuzzle().getFixedPosition(0), state.getPuzzle().getCarOrient(0), indexCarPosition, state.getPuzzle().getGridSize(), 0, new ArrayList<>());
		
		if (carsInTheWay == null) { return Integer.MAX_VALUE; }
		
		return carsInTheWay.size();
	}
	
	private List<Integer> GetCarsInWay(State state, int row, boolean rowOrientation, int startPosition, int endPosition, int recursionDepth, List<Integer> alreadyFoundCars)
	{
		int[][] grid = state.getGrid();
		boolean travellingDirection = false;
		
		int initialCarIndex = rowOrientation ? grid[row][startPosition] : grid[startPosition][row];
		
		// If the start & end index are in the wrong order, swap them
		if (startPosition > endPosition)
		{
			int temp = startPosition;
			startPosition = endPosition;
			endPosition = temp;
			
			travellingDirection = true;
		}
		
		
		List<Integer> newlyFoundCars = new ArrayList<>();
		if (initialCarIndex != -1 && !alreadyFoundCars.contains(initialCarIndex)) { newlyFoundCars.add(initialCarIndex); }
		
		for (int i = startPosition; i < endPosition; i++)
		{
			int currentCar = rowOrientation ? grid[row][i] : grid[i][row];
			
			if (currentCar != -1 && !alreadyFoundCars.contains(currentCar) && !newlyFoundCars.contains(currentCar))
			{
				newlyFoundCars.add(currentCar);
				
				if (recursionDepth < maxRecursionDepth)
				{
					// currently active car properties
					boolean currentCarOrientation = state.getPuzzle().getCarOrient(currentCar);
					int currentCarSize = state.getPuzzle().getCarSize(currentCar);
					int currentCarPosition = state.getVariablePosition(currentCar);
					
					
					boolean isNewCarOrientedSame = currentCarOrientation == rowOrientation;
					
					int newRow = isNewCarOrientedSame ? row : i;
					
					
					
					List<Integer> foundCarsLookingForward = new ArrayList<>();
					List<Integer> foundCarsLookingBackward = new ArrayList<>();
					
					boolean forwardMovePossible = isNewCarOrientedSame ?
							travellingDirection && currentCarPosition - (endPosition - startPosition) >= 0 :
							currentCarSize <= row;
					
					boolean backwardMovePossible = isNewCarOrientedSame ?
							!travellingDirection && currentCarPosition + currentCarSize + (endPosition - startPosition) < state.getPuzzle().getGridSize() :
							state.getPuzzle().getGridSize() - row > currentCarSize;
					
					if (!forwardMovePossible && !backwardMovePossible)
					{
						// Only possible, if there are two cars in the same direction and the first car requires the second to go further than it can
						return null;
					}
					
					ArrayList<Integer> totalListOfCars = new ArrayList<>(newlyFoundCars);
					totalListOfCars.addAll(alreadyFoundCars);
					
					// only compute the cars ahead, if the current car even fits between the wall and the current row
					if (forwardMovePossible)
					{
						int targetPosition = (isNewCarOrientedSame) ? currentCarPosition - (endPosition - startPosition) : row - currentCarSize;
						foundCarsLookingForward = GetCarsInWay(
								state,
								newRow,
								currentCarOrientation,
								currentCarPosition,
								targetPosition,
								recursionDepth + 1,
								totalListOfCars);
					}
					if (backwardMovePossible)
					{
						int targetPosition = (isNewCarOrientedSame) ? currentCarPosition + currentCarSize + (endPosition - startPosition) : row + currentCarSize + 1;
						foundCarsLookingBackward = GetCarsInWay(
								state,
								newRow,
								currentCarOrientation,
								currentCarPosition + currentCarSize,
								targetPosition,
								recursionDepth + 1,
								totalListOfCars);
					}
					
					
					// look where less cars were found (forwards or backwards) and add those found to the list of found cars
					if (forwardMovePossible && backwardMovePossible && foundCarsLookingForward != null && foundCarsLookingBackward != null)
					{
						if (foundCarsLookingForward.size() < foundCarsLookingBackward.size())
						{
							newlyFoundCars.addAll(foundCarsLookingForward);
						}
						else
						{
							newlyFoundCars.addAll(foundCarsLookingBackward);
						}
					}
					if (forwardMovePossible && !backwardMovePossible && foundCarsLookingForward != null) { newlyFoundCars.addAll(foundCarsLookingForward); }
					if (!forwardMovePossible && foundCarsLookingBackward != null) { newlyFoundCars.addAll(foundCarsLookingBackward); }
				}
			}
		}
		
		return newlyFoundCars;
	}
}
