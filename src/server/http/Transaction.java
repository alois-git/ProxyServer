package server.http;

/**
 * Created with IntelliJ IDEA.
 * User: alo
 * Date: 10/24/13
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Transaction {
    protected String httpVersion;

    public String getHeaders() {
        return headers;
    }

    protected String headers;
    protected String host;
    protected int port;
    protected  byte[] fullContent;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    protected String content;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**

     * Return the version of HTTP considered by the request.
     *
     * @return the version of HTTP considered by the request.
     */
    public String getHTTPVersion() {
        return this.httpVersion;
    }

    /**
     * Return the value of the header identified by the name passed in parameter.
     *
     * @param headerName the name of the header.
     * @return the value of the header.
     */
    public String getHeaderValue(String headerName) {

        // Go through each line of header and return the value corresponding to headerName
        for (String headerLine : headers.split("\r\n")) {
            String headerLineName = headerLine.split(":")[0];
            if (headerLineName.equals(headerName.toLowerCase())) {
                return headerLine.split(":")[1];
            }
        }
        return null;
    }

    /**
     * Return a byte array (which can be sent through the network) corresponding to the HTTP response.
     *
     * @return the byte array corresponding to the HTTP response.
     */
    public byte[] getBytes() {
        return this.fullContent;
    }
}
