package validation.http;

import java.util.LinkedList;
import java.util.List;

public class Response {

    List<Parameter> parameters;

    public Response() {
        parameters = new LinkedList<>();
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
