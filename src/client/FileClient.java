package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class FileClient implements Client {
    private Socket socket;

    public void startClient() {
        try {
                connectToServer();

                Uploadable Uploader = new UploadFactory().upload("File",socket);
                Uploader.startUpload();

            } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            closeClient();
        }

    }
    private void connectToServer() throws IOException {
        System.out.println("Please enter the port number of server.");
        System.out.println("--------------------------------------");
        Scanner s = new Scanner(System.in);
        int port = s.nextInt();
        int portNum=portNumberValidator(port);
        System.out.println("--------------------------------------");
        socket = new Socket(InetAddress.getLocalHost(), portNum);
    }
    private int portNumberValidator(int portNum) {
        while (portNum >65535 || portNum <0) {
            System.out.println("Port number MUST be between 0 and 65535,Please enter the port number again:");
            Scanner sc = new Scanner(System.in);
            portNum=sc.nextInt();
        }

        return portNum;}
    public void closeClient() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e);

        }

    }


}