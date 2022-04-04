package algorithms.search;

import java.util.*;

public class DepthFirstSearch extends ASearchingAlgorithm {
    private Stack<AState> states;

    public DepthFirstSearch() {
        this.solution = new Solution();
        this.states = new Stack<>();
        this.grayStates = new Hashtable<>();
    }

    @Override
    public Solution solve(ISearchable s) {
        if(s != null) {
            AState start = s.getStartState();
            AState goal = s.getGoalState();
            states.push(start);
            while (!states.isEmpty()) {
                AState state = states.peek();
                if (state.equals(goal)) {
                    solution = createSolution(state);
                    break;
                }
                if(isWhite(state)) {
                    this.numberOfNodesEvaluated++;
                    paintGray(state);
                }
                else {
                    LinkedList<AState> possible = s.getAllPossibleStates(state);
                    boolean newStateFound = false;
                    for (AState st : possible) {
                        if (isWhite(st)) {
                            st.setComeFrom(state);
                            states.push(st);
                            newStateFound = true;
                            break;
                        }
                    }
                    if(!newStateFound)
                        states.pop();
                }
            }
        }
        return solution;
    }
}
