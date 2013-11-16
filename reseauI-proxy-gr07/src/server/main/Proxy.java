package server.main;

import server.proxy.ProxyServer;
import java.io.IOException;

public class Proxy {

    public static void main(String[] args) throws IOException {
        // Check if user provide a port 
        if (args.length > 0) {
            try {
                int port = Integer.parseInt(args[0]);
                ProxyServer proxyServer = new ProxyServer(port);
                proxyServer.Start();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid port number");
            }
        } else {
            ProxyServer proxyServer = new ProxyServer();
            proxyServer.Start();
        }

    }

}
