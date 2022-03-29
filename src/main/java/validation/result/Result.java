package validation.result;

import validation.http.Method;

import java.util.LinkedList;
import java.util.List;

public class Result {

    Soundness soundness;
    List<Method> methods;

    public Result() {
        soundness = Soundness.UNKNOWN;
        methods = new LinkedList<>();
    }

    public void copyValues(Result result) {
        this.soundness = result.soundness;
        this.methods = result.methods;
    }

    public Soundness getSoundness() {
        return soundness;
    }

    public void setSoundness(Soundness soundness) {
        this.soundness = soundness;
    }

    public List<Method> getEndpoints() {
        return methods;
    }

    public void addEndpoint(Method method) {
        this.methods.add(method);
    }
}
