package algorithms.mazeGenerators;

import java.util.*;

public class MyMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int rows, int cols) {
        Maze maze = new Maze(rows, cols);
        Random rand = new Random();
        Stack<Position> walls = new Stack<>();
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++)
                maze.setWall(row, col);
        }
        Position start = new Position(rand.nextInt(rows), rand.nextInt(cols));
        maze.setStart(start);
        addWall(maze, walls, start);

        while (!walls.isEmpty()) {
            Position next = walls.pop();
            Position path = walls.pop();
            if (maze.isWall(next)){
                maze.setPath(path);
                addWall(maze, walls, next);
            }
        }
        boolean AllWalls = true;
        for (int i=0; i<maze.getRows(); i++){
            for (int j=0; j<maze.getCols(); j++){
                if (maze.isPath(new Position(i, j)) && !(maze.getStartPosition().equals(new Position(i,j)))){
                    i=maze.getRows();
                    j=maze.getCols();
                    AllWalls = false;
                    break;
                }
            }
        }
        Position goal = new Position(rand.nextInt(rows), rand.nextInt(cols));
        if (AllWalls){
            return finishMaze(maze, goal);
        }
        else{
            while (goal.equals(start) || maze.isWall(goal))
                goal = new Position(rand.nextInt(rows), rand.nextInt(cols));
        }
        maze.setGoal(goal);
        return maze;
    }

    /**
     * this function add to walls stack all the wall positions around a position
     * @param maze     - a maze
     * @param walls    - stack of walls in a maze
     * @param position - the current Position, Where the route will start from
     */
    protected void addWall(Maze maze, Stack<Position> walls, Position position) {
        maze.setPath(position);

        LinkedList<Integer> directionsBucket = new LinkedList<>();
        for (int i = 0; i < 4; i++)
            directionsBucket.add(i);
        Collections.shuffle(directionsBucket);

        Position farPosition = new Position(0,0);
        Position nearPosition = new Position(0,0);
        for(int direction : directionsBucket){
            if (direction == 0) {
                farPosition = new Position(position.getRowIndex() - 2, position.getColumnIndex());
                nearPosition = new Position(position.getRowIndex() - 1, position.getColumnIndex());
            } else if (direction == 1) {
                farPosition = new Position(position.getRowIndex(), position.getColumnIndex() + 2);
                nearPosition = new Position(position.getRowIndex(), position.getColumnIndex() + 1);
            } else if (direction == 2) {
                farPosition = new Position(position.getRowIndex() + 2, position.getColumnIndex());
                nearPosition = new Position(position.getRowIndex() + 1, position.getColumnIndex());
            } else if (direction == 3) {
                farPosition = new Position(position.getRowIndex(), position.getColumnIndex() - 2);
                nearPosition = new Position(position.getRowIndex(), position.getColumnIndex() - 1);
            }
            walls.push(nearPosition);
            walls.push(farPosition);
        }
    }

    protected Maze finishMaze(Maze maze, Position goal){
        Position start = maze.getStartPosition();
        Random rand = new Random();
        while (goal.equals(start))
            goal = new Position(rand.nextInt(maze.getRows()), rand.nextInt(maze.getCols()));
        if (start.getRowIndex() > goal.getRowIndex()){
            for (int i=start.getRowIndex(); i>=goal.getRowIndex(); i--)
                maze.setPath(new Position(i, start.getColumnIndex()));
        }
        else{
            for (int i=start.getRowIndex(); i<=goal.getRowIndex(); i++)
                maze.setPath(new Position(i, start.getColumnIndex()));
        }
        if (start.getColumnIndex() > goal.getColumnIndex()){
            for (int i=start.getColumnIndex(); i>=goal.getColumnIndex(); i--)
                maze.setPath(new Position(goal.getRowIndex(), i));
        }
        else{
            for (int i=start.getColumnIndex(); i<=goal.getColumnIndex(); i++)
                maze.setPath(new Position(goal.getRowIndex(), i));
        }
        maze.setGoal(goal);
        return maze;
    }
}

