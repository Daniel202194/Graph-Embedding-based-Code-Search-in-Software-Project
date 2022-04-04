package algorithms.search;

public interface ISearchingAlgorithm {
    Solution solve(ISearchable s);
    int getNumberOfNodesEvaluated();
    String getName();
}
