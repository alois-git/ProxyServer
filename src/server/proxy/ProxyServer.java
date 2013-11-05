package server.proxy;

import server.Server;
import server.http.Request;
import server.http.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: alo
 * Date: 10/22/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProxyServer extends Server {

    int remotePort = 80;

    public ProxyServer() {
        serverSocker = 1111;
    }

    public void Start() throws IOException {
        String requestMessageLine;
        ServerSocket listenSocket;
        listenSocket = new ServerSocket(serverSocker, 50);
        Socket hostSocket;
        while (true) {
            Socket clientSocket = listenSocket.accept();

            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Request request = new Request(fromClient);


            //Forward the request to the real server.
            if (request.getHost() != null){
                hostSocket = new Socket(request.getHost(), request.getPort());

                DataOutputStream toHost = new DataOutputStream(hostSocket.getOutputStream());
                toHost.write(request.getBytes());
                toHost.flush();
                System.out.println("Proxy Writing data from client to server: " + request.toString());


                // forward the response from the server to the client (browser)
                InputStream inHost = hostSocket.getInputStream();
                OutputStream toClient = clientSocket.getOutputStream();
                byte[] buffer = new byte[2000];
                int nbRead = inHost.read(buffer);
                Response response = new Response(buffer);
                while (nbRead > 0) {

                    try {
                        System.out.println("Proxy Writing data from server to client: " + new String(buffer));
                        toClient.write(buffer, 0, nbRead);
                        toClient.flush();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    nbRead = nbRead + inHost.read(buffer);
                }
                clientSocket.close();
                hostSocket.close();
            }

        }

    }
}
