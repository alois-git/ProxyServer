package server.web;

import server.http.Request;
import server.http.Response;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import server.http.ConnectionType;
import sun.security.util.Length;

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
        int length = 0;
        String content;
        // Check if it is a chunked response or not
        if (!response.isChunked()) {
            // check if it is a persistent connection or not.
            if (response.getConnectionType() == ConnectionType.Persistent) {
                content = "";
                length = response.getContentLength();
                if (length != -1) {
                    byte[] contentValue = new byte[length];
                    while (nbRead < length) {
                        try {
                            nbRead += inHost.read(contentValue, nbRead, length - nbRead);
                            content += new String(contentValue);
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                    }
                }

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
            content = "";
            // read chunk-size 
            length = GetChunkedPacketLenght(inHost);
            //   while (chunk-size > 0)
            while (length != 0) {
                nbRead = 0;
                byte[] contentValue = new byte[length];
                //read chunk-data and CRLF
                while (nbRead < length) {
                    try {
                        nbRead += inHost.read(contentValue, nbRead, length - nbRead);
                        // append chunk-data to entity-body

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                inHost.skip(2);
                content += new String(contentValue);
                // read chunk-size 
                length = GetChunkedPacketLenght(inHost);
            }
   
        }
        response.setContent(content);
        hostSocket.close();
        return response;
    }

    private int GetChunkedPacketLenght(InputStream inHost) throws IOException {
        try {
            String chunk = readLine(inHost);
            String[] chunkSizeExtension = chunk.split(";");
            int length = Integer.parseInt(chunkSizeExtension[0], 16);
            return length;
        } catch (NumberFormatException ex) {
            return 0;
        }
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
