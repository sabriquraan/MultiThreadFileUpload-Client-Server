package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class FileSaver implements Runnable {
    private Path pathOfFile;
    private Socket socket;
    private ObjectInputStream socketInput;
    private ObjectOutputStream socketOutput;
    private int similarFileNameCount=0;

    public FileSaver(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            getStreams();
            saveFile();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        } finally {
            closeConnection();
        }
    }

    private void similarFileName() {
        if(Files.exists(pathOfFile))
        {
            similarFileNameCount++;
            updateFileName();
        }
    }

    private void updateFileName(){
        Path fileNameTmp=pathOfFile.getFileName();
        String fileName=fileNameTmp.toString();
        String newFileName = similarFileNameCount+fileName;
        setFilePath(newFileName);    }

    private void getStreams() throws IOException {
        socketOutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        socketOutput.flush();

        socketInput = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    private void saveFile() throws IOException, ClassNotFoundException {
        setFilePath((String) socketInput.readObject()); // Get the path of file from client to server

        synchronized (this) {
            similarFileName();
        }

        if ( Files.exists( pathOfFile ) ) {
            fileExistOnServerResponse();
        } else {
            fileNotExistOnServerResponse();
        }

        Files.copy( socketInput, pathOfFile );
    }

    private void setFilePath(String fullFileName) {
        if(fullFileName ==null)
            throw new NullPointerException ();
        pathOfFile = Paths.get(fullFileName);
    }

    private void sendResponse(String response) throws IOException {
        if(response ==null)
            throw new NullPointerException ();
        socketOutput.writeObject(response);
        socketOutput.flush(); // Write the data are saved in buffer from write method to the socket stream
    }

    private void fileNotExistOnServerResponse() throws IOException {
        sendResponse("File not found in server");
    }

    private void fileExistOnServerResponse() throws IOException, ClassNotFoundException {
        sendResponse("File are found in server");
    }

    private void closeConnection() {
        System.out.println("Terminating connection\n");

        try {
            socketInput.close();
            socketOutput.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }


}