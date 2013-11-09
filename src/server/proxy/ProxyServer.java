package server.proxy;

import server.Server;

import java.io.*;
import java.net.ServerSocket;

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
            new ProxyThread(listenSocket.accept()).start();
        }
    }
}
