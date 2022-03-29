package validation.http;

import contract.structures.Endpoint;

public class Method {

    public final Endpoint newEndpoint;
    public final Endpoint oldEndpoint;

    public Request request;
    public Response response;

    public Method(Endpoint newEndpoint, Endpoint oldEndpoint) {
        this.newEndpoint = newEndpoint;
        this.oldEndpoint = oldEndpoint;
        this.request = new Request();
        this.response = new Response();
    }


}
