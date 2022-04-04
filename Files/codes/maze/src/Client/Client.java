package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private IClientStrategy clientStrategy;
    private int serverPort;
    private InetAddress serverIP;

    public Client(InetAddress serverIP, int serverPort, IClientStrategy clientStrategy ) {
        this.clientStrategy = clientStrategy;
        this.serverPort = serverPort;
        this.serverIP = serverIP;
    }

    public void communicateWithServer() {
        try{
            Socket toServer = new Socket(this.serverIP, this.serverPort);
            clientStrategy.clientStrategy(toServer.getInputStream(),toServer.getOutputStream());
            toServer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
