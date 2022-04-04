package Server;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;

public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private Hashtable<String, String> mazesSolved = new Hashtable<String, String>();
    private static int solutionCounter = 0;

    @Override
    public void serverStrategy(InputStream inputStream, OutputStream outputStream) {
        Maze maze;
        Solution solution;
        ISearchingAlgorithm solving = new BestFirstSearch();
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String s = prop.getProperty("searchAlgorithm");
            if (s.equals("BreadthFirstSearch"))
                solving = new BreadthFirstSearch();
            else if (s.equals("DepthFirstSearch"))
                solving = new DepthFirstSearch();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inputStream);
            ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
            maze = (Maze) fromClient.readObject();
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            String mazeName = maze.arrayToString();
            if (!mazesSolved.containsKey(mazeName)) {
//                System.out.println("new maze!!!!");
                String fileName = "" + solutionCounter + ".txt";
                mazesSolved.put(mazeName, fileName);
                solutionCounter++;
                SearchableMaze searchMaze = new SearchableMaze(maze);
                solution = solving.solve(searchMaze);
                //Now we save the solution (no idea what's going on here with this I/O)
                File newFile = new File(tempDirectoryPath, fileName);
                FileOutputStream outFile = new FileOutputStream(newFile);
                ObjectOutputStream out = new ObjectOutputStream(outFile);
                out.writeObject(solution);
                toClient.writeObject(solution);
                toClient.flush();
            } else {
//                System.out.println("exist maze!!!!");
                String fileName = mazesSolved.get(mazeName);
                //Now we get the solution from the file
                File newFile = new File(tempDirectoryPath, fileName);
                FileInputStream inputFile = new FileInputStream(newFile);
                ObjectInputStream returnFile = new ObjectInputStream(inputFile);

                solution = (Solution) returnFile.readObject();
                returnFile.close();
                toClient.writeObject(solution);
                toClient.flush();

            }

            toClient.close();
            fromClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
