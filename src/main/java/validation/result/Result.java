package validation.result;

import java.util.LinkedList;
import java.util.List;

public class Result {

    Soundness soundness;
    List<Endpoint> endpoints;

    public Result() {
        soundness = Soundness.UNKNOWN;
        endpoints = new LinkedList<>();
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public Soundness checkSoundness() {
        return null;
    }

}
