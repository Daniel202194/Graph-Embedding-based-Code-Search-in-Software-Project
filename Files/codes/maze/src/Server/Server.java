package Server;

import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private ExecutorService threadPoolExecutor;
    private int threads;

    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;

        this.threads = 5;
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            threads = Integer.parseInt(prop.getProperty("threads"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        this.threadPoolExecutor = (ThreadPoolExecutor) executor;
    }

    public void start() {
        new Thread(() -> {
            runServer();
        }).start();
    }

    private void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept(); // blocking call
                    Thread thread = new Thread(() -> {
                        handleClient(clientSocket);
                    });
                    threadPoolExecutor.execute(thread);

                } catch (SocketTimeoutException e) {
                    e.getStackTrace();
                }
            }
            serverSocket.close();
            threadPoolExecutor.shutdown();
        } catch (IOException e) {

        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void stop() {
        stop = true;
    }
}