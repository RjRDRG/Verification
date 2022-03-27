package validation.result;

import validation.http.Endpoint;

import java.util.LinkedList;
import java.util.List;

public class Result {

    Soundness soundness;
    List<Endpoint> endpoints;

    public Result() {
        soundness = Soundness.UNKNOWN;
        endpoints = new LinkedList<>();
    }

    public void copyValues(Result result) {
        this.soundness = result.soundness;
        this.endpoints = result.endpoints;
    }

    public Soundness getSoundness() {
        return soundness;
    }

    public void setSoundness(Soundness soundness) {
        this.soundness = soundness;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void addEndpoint(Endpoint endpoint) {
        this.endpoints.add(endpoint);
    }
}
