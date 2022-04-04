package algorithms.search;

import java.util.LinkedList;

public interface ISearchable {
    AState getStartState();
    AState getGoalState();
    LinkedList<AState> getAllPossibleStates(AState s);
}
