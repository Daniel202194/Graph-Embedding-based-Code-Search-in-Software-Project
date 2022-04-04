package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.util.Properties;

public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void serverStrategy(InputStream inputStream, OutputStream outputStream) {
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        int rows, cols;
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String s = prop.getProperty("generator");
            if(s.equals("EmptyMazeGenerator"))
                mazeGenerator = new EmptyMazeGenerator();
            else if(s.equals("SimpleMazeGenerator"))
                mazeGenerator = new SimpleMazeGenerator();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (ObjectInputStream fromClient = new ObjectInputStream(inputStream)) {
            ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
            toClient.flush();
            int[] msg = (int[]) fromClient.readObject();
            rows = msg[0];
            cols = msg[1];
            Maze maze = mazeGenerator.generate(rows, cols);
            ByteArrayOutputStream ByteOutputStream = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(ByteOutputStream);
            byte[] byteMaze = maze.toByteArray();
            compressor.write(byteMaze);
            compressor.flush();
            toClient.writeObject(ByteOutputStream.toByteArray());
            toClient.flush();

            fromClient.close();
            toClient.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}