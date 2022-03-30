package validation.http;

import contract.structures.Endpoint;

import java.util.LinkedList;
import java.util.List;

public class Method {

    public final Endpoint newEndpoint;
    public final Endpoint oldEndpoint;

    public Request request;
    public List<Response> responses;

    public Method(Endpoint newEndpoint, Endpoint oldEndpoint) {
        this.newEndpoint = newEndpoint;
        this.oldEndpoint = oldEndpoint;
        this.request = new Request();
        this.responses = new LinkedList<>();
    }


}
