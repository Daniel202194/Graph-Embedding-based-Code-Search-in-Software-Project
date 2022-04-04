package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm {
    protected Queue<AState> states;

    public BreadthFirstSearch() {
        this.solution = new Solution();
        this.grayStates = new Hashtable<>();
        this.states = new LinkedList<>();
    }

    public Solution solve(ISearchable s) {
        if (s != null) {
            AState start = s.getStartState();
            AState goal = s.getGoalState();
            paintGray(start);
            states.add(start);
            while (!states.isEmpty()) {
                AState state = states.poll();
                if (state.equals(goal)) {
                    solution = createSolution(state);
                    break;
                }
                else {
                    this.numberOfNodesEvaluated++;
                    LinkedList<AState> possible = s.getAllPossibleStates(state);
                    for (AState st : possible) {
                        if (isWhite(st)) {
                            paintGray(st);
                            states.add(st);
                        }
                    }
                }
            }
        }
        return solution;
    }


}