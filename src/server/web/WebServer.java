package server.web;

import server.Server;
import server.http.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Server {

    public void Start() throws IOException {
        ServerSocket listenSocket;
        listenSocket = new ServerSocket(serverSocker);

        while (true) {
            Socket connectionSocket = listenSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream());

                try {
                    outToClient.println("HTTP/1.0 200 OK");
                    outToClient.println("Server: SimpleWebServer");
                    outToClient.println("");
                    Request request = new Request(inFromClient);
                    outToClient.println(request.toString());
                    outToClient.flush();
                    outToClient.close();

                } catch (Exception ex) {
                    //if you could not open the file send a 404
                    outToClient.println("HTTP/1.0 404 Not Found");
                    outToClient.println("Server: SimpleWebServer");
                    outToClient.println("");
                    outToClient.println("404 guess what ? :D");
                    outToClient.flush();
                    //close the stream
                    outToClient.close();
                }

        }
    }
}
