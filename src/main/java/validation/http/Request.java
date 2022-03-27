package validation.http;

import java.util.Set;

public class Request {

    Set<Parameter> parameters;
    String mediaType;

    public Request() {
        this.parameters = null;
        this.mediaType = null;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }
}
