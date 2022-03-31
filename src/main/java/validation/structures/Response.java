package validation.structures;

import java.util.List;

public class Response {

    public final String newStatus;
    public final String oldStatus;
    public final List<Parameter> parameters;

    public Response(String newStatus, String oldStatus, List<Parameter> parameters) {
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
        this.parameters = parameters;
    }
}
