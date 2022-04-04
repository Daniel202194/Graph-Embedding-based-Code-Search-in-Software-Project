package algorithms.mazeGenerators;

public interface IMazeGenerator {
    Maze generate(int rows, int cols);
    long measureAlgorithmTimeMillis(int rows, int cols);

}
