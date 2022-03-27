package validation.http;

public class Endpoint {

    public final EndpointKey key;
    public final EndpointKey oldKey;

    public Request request;
    public Response response;

    public Endpoint(EndpointKey key, EndpointKey oldKey) {
        this.key = key;
        this.oldKey = oldKey;
        this.request = new Request();
        this.response = new Response();
    }


}
