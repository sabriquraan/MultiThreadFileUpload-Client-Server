package server;

import java.util.Scanner;

public class RunServer {

    private static int portNumberValidator(int portNum) {
        while (portNum >65535 || portNum <0) {
            System.out.println("Port number MUST be between 0 and 65535,Please enter the port number again:");
            Scanner sc = new Scanner(System.in);
            portNum=sc.nextInt();
        }

        return portNum;}

    private static int numberOfClientValidator(int num) {
        while (num <0) {
            System.out.println("Number of Client.Client must be positive ,try again:");
            Scanner s = new Scanner(System.in);
            num=s.nextInt();
        }

        return num;}


    private static MultiThreadFileServer createServer(int portNum, int numOfClient) {

        return new MultiThreadFileServer(portNum,numOfClient);
    }


    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);

        System.out.println("Please enter service port number,  between 0 and 65535 :");
        int portNum=in.nextInt();
        int port=portNumberValidator(portNum);

        System.out.println("Please enter how many clients to serve concurrently :");
        int num =in.nextInt();
        int numClient=numberOfClientValidator(num);

        MultiThreadFileServer server = createServer(port,numClient);
        System.out.println(server.toString());
        server.startServer();
    }
}