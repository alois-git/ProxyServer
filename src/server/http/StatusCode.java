package server.http;

/**
 * Created with IntelliJ IDEA.
 * User: alo
 * Date: 10/24/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public enum StatusCode {

    Accepted(202),
    Ambiguous(300),

    BadGateway(502),
    BadRequest(400),
    Conflict(409),
    Continue(100),
    Created(201),
    ExpectationFailed(417),
    Forbidden(403),
    Found(302),
    GatewayTimeout(504),
    Gone(410),
    HttpVersionNotSupported(505),
    InternalServerError(500),
    LengthRequired(411),
    MethodNotAllowed(405),
    Moved(301),
    MovedPermanently(301),
    NoContent(204),
    NonAuthoritativeInformation(203),
    NotAcceptable(406),
    NotFound(404),
    NotModified(304),
    OK(200),
    PartialContent(206),
    ProxyAuthenticationRequired(407),
    Redirect(302),
    RequestTimeout(408),
    RequestUriTooLong(414),
    ResetContent(205),
    SeeOther(303),
    ServiceUnavailable(503),
    SwitchingProtocols(101),
    TemporaryRedirect(307),
    Unauthorized(401),
    UnsupportedMediaType(415),
    Unused(306),
    UseProxy(305),
    ;

    private int code;

    private StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
