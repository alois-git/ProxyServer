package server.http;


public abstract class Transaction {

    protected String httpVersion;
    protected ConnectionType connectionType = ConnectionType.Persistent;
    protected String headers;
    protected String host;
    protected int port;
    protected int contentLength;  
    
    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
    
    public void setHeaders(String headers) {
        this.headers = headers;
    }
    
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public String getHeaders() {
        return headers;
    }

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
     *
     * Return the version of HTTP considered by the request.
     *
     * @return the version of HTTP considered by the request.
     */
    public String getHTTPVersion() {
        return this.httpVersion;
    }

    /**
     * Return the value of the header identified by the name passed in
     * parameter.
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
}
