/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.web;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import server.http.Request;
import server.http.Response;

/**
 *
 * @author alo
 */
public class WebClientThread implements Runnable {

    private String urlString;

    public WebClientThread(String url) {
        urlString = url;
    }

    @Override
    public void run() {
        try {
            // trying to build a argument from the string argument 
            URL url = new URL(urlString);
            String header = "Host:" + url.getHost() + "\r\n"
                    + "Connection: keep-alive\r\n";

            // build a GET HTTP request
            Request request = new Request("GET", url.toString(), "HTTP/1.1", header);

            // sending the request to the web server
            WebClient webClient = new WebClient();
            Response resultFromRequest = webClient.SendRequest(request);

            // Display the result
            System.out.println("--------- Header ------------");
            System.out.println(resultFromRequest.StatusLineAndHeaders());
            System.out.println("--------- Content ------------");
            System.out.println(resultFromRequest.getContent());
            System.out.println(resultFromRequest.getContent().length());
            
        } catch (MalformedURLException ex) {
            System.out.println(urlString + "is a malformedURL");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
