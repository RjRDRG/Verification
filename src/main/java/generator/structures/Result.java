package generator.structures;

import java.util.LinkedList;
import java.util.List;

public class Result {
    List<Method> methods;

    public Result() {
        methods = new LinkedList<>();
    }

    public List<Method> getEndpoints() {
        return methods;
    }

    public void addEndpoint(Method method) {
        this.methods.add(method);
    }
}
