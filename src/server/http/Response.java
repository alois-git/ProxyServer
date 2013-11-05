package server.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Response extends Transaction {

    private String status;
    private String reason;
    private boolean chunked = false;

    public boolean isChunked() {
        return chunked;
    }

    /**
     * @param content the content of the response send from the server to the
     * proxy.
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
                StringBuilder sBuf = new StringBuilder();
                while ((line = bin.readLine()) != null) {
                    if (line.length() == 0) {
                        break;
                    }
                    if (line.startsWith("Content-Length:")) {
                        String[] tmp = line.split(":");
                        try {
                            contentLength = Integer.parseInt(tmp[1].replace("\r", "").trim());
                        } catch (NumberFormatException ex) {

                        }
                    }
                    // Get connection type.
                    if (line.startsWith("Connection:")) {
                        String[] tmp = line.split(":");
                        if (tmp[1].equals("close")) {
                            connectionType = ConnectionType.Close;
                        } else {
                            connectionType = ConnectionType.Persistent;
                        }
                    }
                    if (line.contains("Transfer-Encoding: chunked")) {
                        chunked = true;
                    }

                    sBuf.append(line).append("\r\n");
                }
                this.headers = sBuf.toString();
            }
        } catch (IOException ioe) {
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
        result.append(httpVersion).append(" ").append(status).append(" ").append(reason).append("\r\n");
        result.append(headers);
        result.append("\r\n");
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(StatusLineAndHeaders());
        result.append(this.content);
        return result.toString();
    }

    public int getContentLength() {
        return contentLength;
    }
}
