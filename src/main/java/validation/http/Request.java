package validation.http;

import java.util.List;

public class Request {

    List<Parameter> parameters;

    public Request() {
        this.parameters = null;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
