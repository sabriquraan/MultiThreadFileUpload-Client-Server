package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class FileUploader implements Uploadable {
    private ObjectOutputStream socketOutput;
    private ObjectInputStream socketInput;
    private Scanner input;
    private Path pathOfFileInClientSide;
    private Socket socket;

    FileUploader(Socket socket){
        this.socket=socket;
    }

    public void startUpload() {
        input = new Scanner(System.in);
        System.out.println("--------------------------------------");
        System.out.println("Welcome to File Uploader.");
        System.out.println("--------------------------------------");

        getFilePath();

        try {
            if (isFileExistsOnClient()) {
                getStream();
                uploadFile();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        } finally {
            closeConnection();
        }
    }
    private void getFilePath() {
        System.out.println("Please enter the path of file you want to upload it:\n" +
                " (example: C:\\Users\\USER\\Desktop\\New folder\\1.JPG)  ");
        System.out.println("--------------------------------------");
        pathOfFileInClientSide = Paths.get(input.nextLine());
        System.out.println("--------------------------------------");
    }
    private boolean isFileExistsOnClient() { return Files.exists(pathOfFileInClientSide)
            && !Files.isDirectory(pathOfFileInClientSide);
    }
    private int portNumberValidator(int portNum) {
        while (portNum >65535 || portNum <0) {
            System.out.println("Port number MUST be between 0 and 65535,Please enter the port number again:");
            Scanner sc = new Scanner(System.in);
            portNum=sc.nextInt();
        }

        return portNum;}
    private void getStream() throws IOException {
        socketOutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        socketOutput.flush();
        // flush output buffer to send header information
        socketInput = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    }
    private void sendRequest(String request) throws IOException {
        socketOutput.writeObject(request);
        socketOutput.flush(); // Write the data immediately to the socket stream
    }
    private void uploadFile() throws IOException, ClassNotFoundException {
        Path fileNameTmp=pathOfFileInClientSide.getFileName();
        String fileName=fileNameTmp.toString();
        sendRequest(fileName);

        if(isFileExistOnServer()) {
            System.out.println("This file is already exist on the server. ");
            System.out.println("--------------------------------------");
            return;
        }
        Files.copy(pathOfFileInClientSide, socketOutput);
        System.out.println("The file upload to the server has completed successfully.");
    }
    private boolean isFileExistOnServer() throws IOException, ClassNotFoundException {
        return (socketInput.readObject().equals("File are found in server")) ;
    }
    private void closeConnection() {
        try {
            socketOutput.close();
            socketInput.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        } catch (NullPointerException e) {
            System.err.println("File not found in the directory.");
        }
    }



}