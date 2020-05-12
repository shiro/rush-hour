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
    // private final TreeSet<Node> openList;
    // private final TreeMap<Integer, HashSet<Node>> openList;
    // private TreeSet<Node> closedList;
    private final HashSet<State> closedList;

    /**
     * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     */
    public AStar(Puzzle puzzle, Heuristic heuristic) {
        // evaluate the node cost based on the depth plus heuristic value
        Function<Node, Integer> nodeCost = (Node n) -> n.getDepth() + heuristic.getValue(n.getState());

        // compare nodes based on their estimated cost
        Comparator<Node> cmp = (n1, n2) -> {
            int cost1 = nodeCost.apply(n1);
            int cost2 = nodeCost.apply(n2);

            int val =  Integer.compare(cost1, cost2);
            if (val != 0) return val;

            // compare cost, then depth (preferring deeper nodes)
            return Integer.compare(n2.getDepth(), n1.getDepth());
        };

        openList = new PriorityQueue<>(cmp);
        closedList = new HashSet<>();

        Node node = puzzle.getInitNode();

        while (!node.getState().isGoal()) {
            State currState = node.getState();

            State[] expStates = currState.expand();

            // expand all child nodes and enqueue them (possibly multiple times on different depths)
            for (State s : expStates) {
                Node expandedNode = new Node(s, node.getDepth() + 1, node);

                if (closedList.contains(expandedNode.getState())) continue;

                openList.add(expandedNode);
            }

            while (closedList.contains(node.getState())) {
                node = openList.poll();
                assert node != null; // if we get here there is no solution and the puzzle is invalid
            }

            closedList.add((node.getState()));
        }

        this.path = new State[node.getDepth() + 1];
        while (node != null) {
            this.path[node.getDepth()] = node.getState();

            node = node.getParent();
        }
    }
}
