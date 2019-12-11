package server;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiThreadFileServer implements Server, Comparable<MultiThreadFileServer>{

    private int port=6666;
    private int numOfClient=3;
    private ServerSocket server;
    private Socket client;
    private Scanner input;

    MultiThreadFileServer(int port, int numOfClient){
        this.port=port;
        this.numOfClient=numOfClient;
    }

    public void startServer() {
        try {
            server = new ServerSocket(port);
            System.out.printf("- Server is listening on port : %d and will serve %d clients simultaneously %n",port,numOfClient);
            while (true) {
                waitAndAcceptConnection();
                uploadFile();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        finally {
            closeServer();
        }
    }
    private void waitAndAcceptConnection() throws IOException {
        client = server.accept();
        System.out.println("Connection received from: " +  client.getInetAddress().getHostAddress());
    }
    private void uploadFile () {
        ExecutorService connectionManager = Executors.newCachedThreadPool();
        connectionManager.execute(new FileSaver(client));
    }
    public void closeServer() {
        try {
            client.close();
            server.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    @Override
    public String toString() {
        return "Server.MultiThreadServer{" +
                "port=" + port +
                ", numOfClient=" + numOfClient +
                '}';
    }
    @Override
    public int compareTo(MultiThreadFileServer that) {
        if(this.port < that.port) return -1;
        if(this.port > that.port) return +1;
        if(this.numOfClient < that.numOfClient) return -1;
        if (this.numOfClient > that.numOfClient) return +1;
        return 0;

    }
}