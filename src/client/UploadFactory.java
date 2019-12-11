package client;

import java.net.Socket;

public class UploadFactory {

    private Socket socket;


    public Uploadable upload(String Type,Socket socket){

        if(Type.equalsIgnoreCase("File")) {
            return new FileUploader(socket);
        } else if(Type.equalsIgnoreCase("Directory")) {
            return new DirectoryUploader(socket);
        }

        return null;

    }
}
