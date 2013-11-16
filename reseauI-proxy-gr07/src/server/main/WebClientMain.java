package server.main;

import java.io.IOException;
import server.web.WebClientThread;

public class WebClientMain {

    public static void main(String[] args) throws IOException {
        
        if (args.length < 1) {
            System.out.println("Give at least one URL as an argument.");
            return;
        }
        // for every given argument start a new thread
        for (String urlString : args) {
            Runnable r = new WebClientThread(urlString);
            new Thread(r).start();   
        }
    }

}
