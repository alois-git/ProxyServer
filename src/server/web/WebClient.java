package server.web;

import server.http.Request;
import server.http.Response;

import java.io.*;
import java.net.Socket;

public class WebClient {

    private int remotePort = 80;

    public Response SendRequest(Request request) throws IOException {

        Socket hostSocket = new Socket(request.getHost(), request.getPort());

        DataOutputStream toHost = new DataOutputStream(hostSocket.getOutputStream());
        toHost.write(request.getBytes());
        toHost.flush();

        InputStream inHost = hostSocket.getInputStream();
        BufferedReader inFromHost = new BufferedReader(
                new InputStreamReader(inHost)
        );


        String header = GetHeader(inFromHost);
        Response response = new Response(header.getBytes());
        int nbRead = 0;
        String content;
        if (!response.isChunked()) {
            // if it is not chunked we have to check the content Length to support persistent connection.
            // check if the content length is not -1 if it is we don't have a persistent connection.
            if (response.getContentLength() != -1){
                char[] contentValue = new char[response.getContentLength()];
                while (nbRead < response.getContentLength()) {
                    try {
                        contentValue[nbRead] = (char) inFromHost.read();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    nbRead++;
                }
                content = new String(contentValue);
            }
            else
            {
                content = "";
                char[] buffer = new char[2000];
                nbRead= inFromHost.read(buffer);
                while (nbRead > 0) {
                    content += new String(buffer);
                    nbRead = inFromHost.read(buffer);
                }
            }
        }
        else
        {
            // if it is chunked we need to check before each packet his length
            content = "";
            int length = Integer.parseInt(inFromHost.readLine(), 16);
            while (length != 0) {
                nbRead = 0;
                while (nbRead < length) {
                    try {
                        content += (char) inFromHost.read();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    nbRead++;
                }
                // skip the break line
                inFromHost.readLine();
                length = Integer.parseInt(inFromHost.readLine(), 16);
            }
        }
        response.setContent(content);
        hostSocket.close();
        return response;
    }

    private String GetHeader(BufferedReader inFromHost) throws IOException {
        String header = "";
        String line = inFromHost.readLine();
        while (!line.equals("")) {
            header += line + "\r\n";
            line = inFromHost.readLine();

        }
        return header;
    }
}
