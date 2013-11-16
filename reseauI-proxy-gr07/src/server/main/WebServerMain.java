package server.main;

import server.web.WebServer;

import java.io.IOException;

public class WebServerMain {

    public static void main(String[] args) throws IOException {
        WebServer webServer = new WebServer();
        webServer.Start();
    }

}


