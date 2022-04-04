package algorithms.mazeGenerators;

import java.util.LinkedList;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {
    private LinkedList<Position> solution;
    private Maze maze;
    private Position start;
    private Position goal;

    public SimpleMazeGenerator() {
        this.solution = new LinkedList<>();
    }

    public Maze generate(int rows, int cols) {
        this.maze = new Maze(rows,cols);
        Position[] sp = getSpecialPositions(rows,cols);
        start = sp[0];
        goal = sp[1];
        this.maze.setStart(start);
        this.maze.setGoal(goal);
        paveSolution();
        for(int row = 0; row<rows; row++){
            for(int col=0; col<cols; col++){
                Position position = new Position(row,col);
                if((maze.isPath(position)) && !solution.contains(position))
                    setRandomType(position);
            }
        }
        return maze;
    }

    /**
     * this function ensure there's a solution of this maze
     */
    private void paveSolution(){
        LinkedList<Integer> rowIndexes = goToIndex(start.getRowIndex(), goal.getRowIndex());
        for(int row : rowIndexes){
            Position p = new Position(row, start.getColumnIndex());
            this.solution.add(p);
        }
        LinkedList<Integer> colIndexes = goToIndex(start.getColumnIndex(), goal.getColumnIndex());
        for(int col : colIndexes){
            int row = goal.getRowIndex();;
            if(!rowIndexes.isEmpty())
                row = rowIndexes.getLast();
            Position p = new Position(row, col);
            solution.add(p);
        }
    }

    private LinkedList<Integer> goToIndex(int currIndex, int destIndex){
        LinkedList<Integer> indexes = new LinkedList<>();
        int advance = 0;
        if(currIndex < destIndex)
            advance = 1;
        else if(currIndex > destIndex)
            advance = -1;
        while(currIndex != destIndex) {
            currIndex += advance;
            indexes.add(currIndex);
        }
        return indexes;
    }

    /**
     * this function get a position and sets this as wall or path by random
     * @param position
     */
    private void setRandomType(Position position){
        Random random = new Random();
        if(random.nextInt(3) < 2)
            maze.setWall(position);
    }

}