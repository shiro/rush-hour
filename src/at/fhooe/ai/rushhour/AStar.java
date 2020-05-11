package at.fhooe.ai.rushhour;

import java.util.*;
import java.util.function.Function;

/**
 * This is the template for a class that performs A* search on a given rush hour
 * puzzle with a given heuristic. The main search computation is carried out by
 * the constructor for this class, which must be filled in. The solution (a path
 * from the initial state to a goal state) is returned as an array of
 * <tt>State</tt>s called <tt>path</tt> (where the first element
 * <tt>path[0]</tt> is the initial state). If no solution is found, the
 * <tt>path</tt> field should be set to <tt>null</tt>. You may also wish to
 * return other information by adding additional fields to the class.
 */
public class AStar {

    /**
     * The solution path is stored here
     */
    public State[] path;


    private PriorityQueue<Node> openList;
    // private TreeSet<Node> closedList;
    private HashSet<State> closedList;

    /**
     * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     */
    public AStar(Puzzle puzzle, Heuristic heuristic) {
        // evaluate the node cost based on the depth plus heuristic value
        Function<Node, Integer> getDepth = (Node n) -> n.getDepth() + heuristic.getValue(n.getState());

        // compare nodes based on their estimated cost
        Comparator<Node> cmp = (n1, n2) -> {
            int cost1 = getDepth.apply(n1);
            int cost2 = getDepth.apply(n2);

            return Integer.compare(cost1, cost2);
        };

        openList = new PriorityQueue<>(cmp);
        closedList = new HashSet<State>();
        // todo find out why TreeSet would be useful here
        // closedList = new TreeSet<State>();

        Node node = puzzle.getInitNode();

        while (!node.getState().isGoal()) {
            State currState = node.getState();

            State[] expStates = currState.expand();

            for (State s : expStates) {
                Node no = new Node(s, node.getDepth() + 1, node);

                if (closedList.contains(no.getState())) continue;

                // add the node to the queue
                closedList.add(no.getState());
                // todo add all at once for perf
                openList.add(no);
            }

            // todo check for null
            Node nodeToCheck = openList.poll();
            node = nodeToCheck;
        }

        this.path = new State[node.getDepth() + 1];
        while (node != null) {
            this.path[node.getDepth()] = node.getState();

            node = node.getParent();
        }
    }
}
