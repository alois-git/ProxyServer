package server.main;

import server.proxy.ProxyServer;
import java.io.IOException;

public class Proxy {

    public static void main(String[] args) throws IOException {
         ProxyServer proxyServer = new ProxyServer();
         proxyServer.Start();

    }

}


