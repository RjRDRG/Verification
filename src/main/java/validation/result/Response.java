package validation.result;

import java.util.LinkedHashMap;
import java.util.Map;

public class Response {

    boolean satisfied;

    public Map<String, Parameter> parameters;

    public Response() {
        parameters = new LinkedHashMap<>();
    }

    public void addParameter(Parameter parameter) {
        parameters.put(parameter.key,parameter);
    }
}
