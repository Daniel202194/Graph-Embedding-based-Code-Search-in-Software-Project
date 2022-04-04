package algorithms.search;

import java.util.Hashtable;
import java.util.Stack;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    private String Name;
    protected int numberOfNodesEvaluated;
    protected Solution solution;
    protected Hashtable<String, AState> grayStates;

    public ASearchingAlgorithm() {
        this.Name = this.getClass().toString();
        this.numberOfNodesEvaluated = 0;
    }

    public String getName(){return this.Name;}

    public int getNumberOfNodesEvaluated() {
        return numberOfNodesEvaluated;
    }

    protected Solution createSolution (AState state){
        Solution solution = new Solution();
        Stack<AState> states = new Stack<>();
        while (state != null){
            states.add(state);
            state = state.getComeFrom();
        }
        while (!states.isEmpty())
            solution.addState(states.pop());
        return solution;
    }

    /**
     * @param state - state
     * @return True-if this state is visited, false-else.
     */
    protected boolean isWhite(AState state) {
        return !grayStates.containsKey(state.toString());
    }

    protected void paintGray(AState state) {
        grayStates.put(state.toString(), state);
    }
}
