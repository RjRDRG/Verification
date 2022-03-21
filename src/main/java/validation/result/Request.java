package validation.result;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    boolean satisfied;

    public Map<String, Parameter> parameters;

    public Request() {
        parameters = new LinkedHashMap<>();
    }

    public void addParameter(Parameter parameter) {
        parameters.put(parameter.key,parameter);
    }
}
