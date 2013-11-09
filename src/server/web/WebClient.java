package server.web;

import server.http.Request;
import server.http.Response;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import server.http.ConnectionType;

public class WebClient {

    private int remotePort = 80;

    public Response SendRequest(Request request) throws IOException {

        Socket hostSocket = new Socket(request.getHost(), request.getPort());

        DataOutputStream toHost = new DataOutputStream(hostSocket.getOutputStream());
        toHost.write(request.getBytes());
        toHost.flush();

        InputStream inHost = hostSocket.getInputStream();

        String header = GetHeader(inHost);
        Response response = new Response(header.getBytes());
        int nbRead = 0;
        String content;
        // Check if it is a chunked response or not
        if (!response.isChunked()) {
            // check if it is a persistent connection or not.
            if (response.getConnectionType() == ConnectionType.Persistent) {
                long startTime = System.currentTimeMillis();
                content = "";
                if (response.getContentLength() != -1) {
                    byte[] contentValue = new byte[response.getContentLength()];
                    while (nbRead < response.getContentLength()) {
                        try {
                            nbRead += inHost.read(contentValue);
                            content += new String(contentValue);
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                    }
                }
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println("persistent connection" + elapsedTime);

            } else {
                long startTime = System.currentTimeMillis();
                content = "";
                byte[] buffer = new byte[2000];
                nbRead = inHost.read(buffer);
                while (nbRead > 0) {
                    content += new String(buffer);
                    nbRead = inHost.read(buffer);
                }
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println("non persistente connection" + elapsedTime);
            }
        } else {
            long startTime = System.currentTimeMillis();
            byte[] contentValue = new byte[2000];

            content = "";
            int length = 0;
            try {
                String number = readLine(inHost);
                length = Integer.parseInt(number, 16);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            while (length != 0) {
                nbRead = 0;
                while (nbRead < length) {
                    try {
                        nbRead += inHost.read(contentValue);
                        content += new String(contentValue);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }

                // skip the break line
                readLine(inHost);
                try {
                    String line = readLine(inHost);
                    length = Integer.parseInt(line, 16);
                } catch (NumberFormatException ex) {
                    System.out.println(ex);
                }
            }
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("chunked connection" + elapsedTime);
        }
        response.setContent(content);
        hostSocket.close();
        return response;
    }

    private String GetHeader(InputStream inFromHost) throws IOException {
        String header = "";
        String line = readLine(inFromHost);
        while (!line.equals("")) {
            header += line + "\r\n";
            line = readLine(inFromHost);
        }
        return header;
    }

    public static String readLine(InputStream s) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;

        //lecture caractère par caractère
        while ((b = s.read()) >= 0 && b != '\n') {
            //on a trouvé un retour à la ligne, on arrête		
            baos.write(b);
        }
        String ret = baos.toString().substring(0, baos.size() - 1);
        return ret;
    }
}
