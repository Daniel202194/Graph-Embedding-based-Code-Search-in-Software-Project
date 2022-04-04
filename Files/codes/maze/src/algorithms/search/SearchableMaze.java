package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.LinkedList;

public class SearchableMaze implements ISearchable {
    private Maze maze;

    public SearchableMaze(Maze maze) {
        this.maze = maze;
    }

    @Override
    public AState getStartState() {
        Position start = maze.getStartPosition();
        return new MazeState(start, null, 0);
    }

    @Override
    public AState getGoalState() {
        Position goal = maze.getGoalPosition();
        return new MazeState(goal, null, 0);
    }

    /**
     * @param state - a source position
     * @return - list of all the successors (legal and path positions) positions from this source state
     */
    @Override
    public LinkedList<AState> getAllPossibleStates(AState state) {
        LinkedList<AState> successors = new LinkedList<>();
        MazeState ms = (MazeState) state;
        int sourceRow = ms.getState().getRowIndex();
        int sourceCol = ms.getState().getColumnIndex();

        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                if ((row != 0) || (col != 0)) {
                    Position position = new Position(sourceRow + row, sourceCol + col);
                    if ((maze.isLegalPosition(position)) && (maze.isPath(position))) {
                        int cost = ms.getCost();
                        if ((row != 0) && (col != 0)) { // diagonal = two side move
                            if(isDiagonalPathExist(position, row, col))
                                cost += 15;
                        }
                        else // one side move
                            cost += 10;
                        MazeState successor = new MazeState(position, state, cost);
                        successors.add(successor);
                    }
                }
            }
        }
        return successors;
    }

    /**
     * this function check if that diagonal path is legal at this maze
     * @param position - a specific cell in a maze
     * @param row - row number
     * @param col - column number
     * @return true if is a legal diagonal move
     */
    private boolean isDiagonalPathExist(Position position, int row, int col) {
        int sourceRow = position.getRowIndex();
        int sourceCol = position.getColumnIndex();
        Position position1 = new Position(sourceRow+row, sourceCol);
        Position position2 = new Position(sourceRow, sourceCol+col);
        boolean path1 = maze.isLegalPosition(position1) && maze.isPath(position1);
        boolean path2 = maze.isLegalPosition(position2) && maze.isPath(position2);
        return path1 || path2;
    }
}
