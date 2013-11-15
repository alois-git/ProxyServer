/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.http.ConnectionType;
import server.http.Request;
import server.http.Response;
import server.web.WebClient;

/**
 *
 * @author alo
 */
public class ProxyThread extends Thread {

    private Socket client;

    private static HashMap<URL, Response> Cache = new HashMap<>();

    public ProxyThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        BufferedReader fromClient = null;
        try {
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Request request = new Request(fromClient);

            // build a new HTTP request avoiding encoding char
            String header = "Host:" + request.getHost() + "\r\n";
            if (request.getConnectionType() == ConnectionType.Close) {
                header = header + "Connection: close\r\n";
            } else {
                header = header + "Connection: keep-alive\r\n";
            }

            Request newRequest = new Request(request.getMethod(), request.getURL(), request.getHTTPVersion(), header);
            if (Cache.containsKey(newRequest.getURL())) {
                OutputStream toClient = client.getOutputStream();
                Response response = Cache.get(newRequest.getURL());
                toClient.write(response.toString().getBytes());
                toClient.flush();
                client.close();
            } else {
                //Forward the request to the real server.
                if (newRequest.getHost() != null) {
                    WebClient webClient = new WebClient();
                    Response response = webClient.SendRequest(newRequest);
                    response.setConnectionType(ConnectionType.Close);
                    OutputStream toClient = client.getOutputStream();
                    toClient.write(response.toString().getBytes());
                    toClient.flush();
                    client.close();
                    Cache.put(newRequest.getURL(), response);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fromClient != null) {
                try {
                    fromClient.close();
                } catch (IOException ex) {
                    Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
