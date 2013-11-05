package server.http;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Request extends Transaction {
    private String method;
    private URL url;
    private String connectionType = "close";
    private final static String EOL = "\r\n";
    private final static int httpPort = 80;

    /**
     * Request constructor.
     *
     * @param method      the method attached to the request.
     * @param url         the url attached to the request.
     * @param httpVersion the version of HTTP considered by the request.
     * @param headers     all the headers attached to the request.
     */
    public Request(String method, String url, String httpVersion, String headers) throws MalformedURLException {
        this.method = method;
        this.url = new URL(url);
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.host = this.url.getHost();
        this.port = httpPort;
    }

    public Request(BufferedReader from) {
        try {
            String firstLine = from.readLine();
            String[] tmp = firstLine.split(" ");
            method = tmp[0];
            url = new URL(tmp[1]);
            httpVersion = tmp[2];
            headers = "";
            String line = from.readLine();
            while (line.length() != 0) {

                // Get connection type.
                if (line.startsWith("Connection:")) {
                    tmp = line.split(":");
                    connectionType = tmp[1].replace("\r", "").trim();
                }
                // get the host
                if (line.startsWith("Host:")) {
                    tmp = line.split(":");
                    host = tmp[1].replace("\r", "").trim();
                    port = httpPort;
                }
                headers += line + EOL;

                line = from.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error reading from socket: " + e);
        }
    }

    /**
     * Return the method attached to the request.
     *
     * @return the method attached to the request.
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Return the url attached to the request (host).
     *
     * @return the url attached to the request (host).
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * Return a byte array (which can be sent through the network) corresponding to the HTTP request.
     *
     * @return the byte array corresponding to the HTTP request.
     */
    public byte[] getBytes() {
        return this.toString().getBytes();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Request))
            return false;

        Request hr = (Request) o;

        return this.toString().equals(hr.toString());
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(method + " " + url + " " + httpVersion + "\r\n");
        result.append(headers);
        result.append("\r\n");
        return result.toString();
    }
}
