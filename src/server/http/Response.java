package server.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class Response extends Transaction {

    private String status;
    private String reason;
    private int contentLength=-1;
    private boolean chunked = false;

    /**
     * The HTTP response returned by this method should be sent as an
     * an answer to the browser when the request HTTP server cannot be reached.
     *
     * @return HTTP response correspdonding to an unreachable HTTP server
     * @throws Exception if an error occurs when constructing the HTTP response
     */
    static Response createGatewayTimeout() throws Exception {
        String content = new String("<html><head>\n" +
                "<title>504 Gateway Timeout</title>\n" +
                "</head><body>\n" +
                "<h1>504 Gateway Timeout</h1>\n" +
                "<p>The requested host cannot be reached.</p>\n" +
                "<hr>\n" +
                "</body></html>\n");
        String response = new String("HTTP/1.1 504 Gateway Timeout\r\n" +
                "Content-Length: " + content.getBytes().length + "\r\n" +
                "Connection: close\r\n" +
                "Content-Type: text/html; charset=iso-8859-1\r\n\r\n" +
                content);

        return new Response(response.getBytes());
    }

    /**
     * The HTTP response returned by this method should be sent as an
     * an answer of a request accessing a forbidden URL.
     *
     * @return HTTP response correspdonding to a request accessing
     *         a forbidden URL.
     * @throws Exception if an error occurs when constructing the HTTP response
     */
    static Response createForbiddenResponse() throws Exception {
        String content = new String("<html><head>\n" +
                "<title>403 Forbidden</title>\n" +
                "</head><body>\n" +
                "<h1>403 Forbidden</h1>\n" +
                "<p>The requested URL cannot be accessed.</p>\n" +
                "<hr>\n" +
                "</body></html>\n");
        String response = new String("HTTP/1.1 403 Forbidden\r\n" +
                "Content-Length: " + content.getBytes().length + "\r\n" +
                "Connection: close\r\n" +
                "Content-Type: text/html; charset=iso-8859-1\r\n\r\n" +
                content);

        return new Response(response.getBytes());
    }

    /**
     * The HTTP response returned by this method should be sent as an
     * an answer of a request using a method different than GET or HEAD.
     *
     * @return HTTP response correspdonding to a request (1) using
     *         a method different than GET or HEAD, or (2) considering an HTTP version
     *         different than 1.1 .
     * @throws Exception if an error occurs when constructing the HTTP response
     */
    static Response createNotImplementedResponse() throws Exception {
        String content = new String("<html><head>\n" +
                "<title>501 Not implemented</title>\n" +
                "</head><body>\n" +
                "<h1>501 Not implemented</h1>\n" +
                "<p>The request method or the http version is not implemented.</p>\n" +
                "<hr>\n" +
                "</body></html>\n");
        String response = new String("HTTP/1.1 501 Not implemented\r\n" +
                "Content-Length: " + content.getBytes().length + "\r\n" +
                "Connection: close\r\n" +
                "Content-Type: text/html; charset=iso-8859-1\r\n\r\n" +
                content);
        return new Response(response.getBytes());
    }
    
    public boolean isChunked() {
        return chunked;
    }

    /**
     * @param content the content of the response send from the server to the proxy.
     * @throws Exception if the content of the response is not well-formed.
     */
    public Response(byte[] content) {
        BufferedReader bin = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(content)));

        // Parse the response from the result
        try {
            String line = bin.readLine();
            line = line.replace("\t", "");
            if ((line != null) && (line.trim().length() != 0)) {
                // First line
                // Parse the command
                int index = line.indexOf(" ");
                if (index != -1) {
                    this.httpVersion = line.substring(0, index).trim();
                }

                if (index != -1) {
                    String remainder = line.substring(index).trim();
                    index = remainder.indexOf(" ");
                    if (index != -1) {
                    // Parse the URL
                    this.status = remainder.substring(0, index).trim();
                    // Parse the Version
                    this.reason = remainder.substring(index).trim();
                    }
                }

                // Parse the headers
                StringBuffer sBuf = new StringBuffer();
                while ((line = bin.readLine()) != null) {
                    if (line.length() == 0)
                        break;

                    sBuf.append(line + "\r\n");

                    if (line.startsWith("Content-Length:")){
                        String[] tmp = line.split(":");
                        try
                        {
                            contentLength = Integer.parseInt(tmp[1].replace("\r", "").trim());
                        }catch (Exception ex){

                        }
                    }

                    if (line.contains("Transfer-Encoding: chunked")){
                        String[] tmp = line.split(":");
                        chunked = true;
                    }
                }
                this.headers = sBuf.toString();
            }

            this.fullContent = content;
        } catch (Exception ioe) {
            System.out.println(ioe);
        }


    }

    /**
     * Return the status code attached to the response.
     *
     * @return the status code attached to the response.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Return the reason explaining the status code attached to the response.
     *
     * @return the reason explaining the status code attached to the response.
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Return the string corresponding to the request without its content
     * (return only the status line and the headers).
     *
     * @return the string corresponding to the request without its content.
     */
    public String StatusLineAndHeaders() {
        StringBuilder result = new StringBuilder();
        result.append(httpVersion + " " + status + " " + reason + "\r\n");
        result.append(headers);
        result.append("\r\n");
        return result.toString();
    }

    public String toString() {
        return new String(fullContent);
    }


    public int getContentLength() {
        return contentLength;
    }
}
