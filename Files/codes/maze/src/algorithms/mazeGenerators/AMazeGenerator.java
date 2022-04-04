package algorithms.mazeGenerators;

import java.util.Random;

public abstract class AMazeGenerator implements IMazeGenerator {
    @Override
    public long measureAlgorithmTimeMillis(int rows, int cols) {
        long startTime = System.currentTimeMillis();
        generate(rows, cols);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    /**
     * this function get the maze size
     * @param rows - amount of rows on a maze
     * @param cols - amount of columns on a maze
     * @return 2 special points: start and goal
     */
    protected Position[] getSpecialPositions(int rows, int cols){
        Position res[] = new Position[2];
        Random rand = new Random();
        Position start = new Position(rand.nextInt(rows),rand.nextInt(cols));
        Position goal = new Position(rand.nextInt(rows),rand.nextInt(cols));
        while(start.equals(goal))
            goal = new Position(rand.nextInt(rows),rand.nextInt(cols));
        res[0] = start;
        res[1] = goal;
        return res;
    }
}
