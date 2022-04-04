package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int rows, int cols){
        Maze maze = new Maze(rows,cols);
        Position[] specialPositions = getSpecialPositions(rows,cols);
        maze.setStart(specialPositions[0]);
        maze.setGoal(specialPositions[1]);
        for (int row=0; row<rows; row++) {
            for (int col = 0; col < cols; col++)
                maze.setPath(row,col);
        }
        return maze;
    }

}
