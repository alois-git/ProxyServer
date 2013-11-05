package server.main;

import server.http.Request;
import server.http.Response;
import server.web.WebClient;

import java.io.IOException;
import java.net.URL;

public class WebClientMain {

    public static void main(String[] args) throws IOException {
        URL url = new URL(args[0]);
        String header =
                "Host:" + url.getHost() + "\r\n" +
                        "Connection: keep-alive\r\n";
        Request request = new Request("GET", args[0], "HTTP/1.1", header);
        WebClient webClient = new WebClient();
        Response resultFromRequest = webClient.SendRequest(request);
        System.out.println(resultFromRequest.StatusLineAndHeaders());
        System.out.println("--------- Content ------------");
        byte[] arr = resultFromRequest.getContent().getBytes("iso-8859-1");
        // Create a new String using the contents of the byte array.
        String newStr = new String(arr);

        System.out.println(newStr);
        System.out.println(newStr.length());
//        OutputStream out;
//        FileOutputStream stream = new FileOutputStream("test.html");
//        out = new BufferedOutputStream(stream);
//        out.write(arr);
//        out.close();
    }

}
