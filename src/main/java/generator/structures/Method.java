package generator.structures;

import contract.structures.Endpoint;

import java.util.List;

public class Method {

    public final Endpoint newEndpoint;
    public final Endpoint oldEndpoint;

    public Request request;
    public List<Response> responses;

    public Method(Endpoint newEndpoint, Endpoint oldEndpoint) {
        this.newEndpoint = newEndpoint;
        this.oldEndpoint = oldEndpoint;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }
}
