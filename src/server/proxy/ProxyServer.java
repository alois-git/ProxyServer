package server.proxy;

import server.Server;
import server.http.Request;
import server.http.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import server.http.ConnectionType;
import server.web.WebClient;

/**
 * Created with IntelliJ IDEA. User: alo Date: 10/22/13 Time: 4:36 PM To change
 * this template use File | Settings | File Templates.
 */
public class ProxyServer extends Server {

    int remotePort = 80;

    public ProxyServer() {
        serverSocker = 1111;
    }

    public ProxyServer(int port) {
        serverSocker = port;
    }

    public void Start() throws IOException {
        ServerSocket listenSocket;
        listenSocket = new ServerSocket(serverSocker, 50);
        while (true) {
            Socket clientSocket = listenSocket.accept();

            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Request request = new Request(fromClient);
            
            // build a new HTTP request avoiding encoding char
            String header = "Host:" + request.getHost() + "\r\n";
            if (request.getConnectionType() == ConnectionType.Close) {
                header = header + "Connection: close\r\n";
            } else {
                header = header + "Connection: keep-alive\r\n";
            }
            Request newRequest = new Request(request.getMethod(), request.getURL().toString(), request.getHTTPVersion(), header);
            
            //Forward the request to the real server.
            if (request.getHost() != null) {
                WebClient webClient = new WebClient();
                Response response = webClient.SendRequest(newRequest);

                OutputStream toClient = clientSocket.getOutputStream();
                if (response.getHeaders().contains("Connection: Keep-Alive")) {
                    header = response.getHeaders().replace("Connection: Keep-Alive", "Connection: Close");
                } else {
                    header = response.getHeaders() + "Connection: Close\r\n";
                }
                header = header.replace("Vary: Accept-Encoding\r\n", "");
                header = header.replace("Content-Encoding: gzip\r\n", "");
                response.setHeaders(header);

                toClient.write(response.toString().getBytes());
                toClient.flush();
                clientSocket.close();
            }

        }

    }
}
