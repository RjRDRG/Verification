package generator.structures;

import java.util.List;

public class Message {

    public static String REQUEST = "request";
    public static String RESPONSE = "response";

    public final String type;
    public final String priorType;
    public final List<Parameter> parameters;

    private Message(String type, String priorType, List<Parameter> parameters) {
        this.type = type;
        this.priorType = priorType;
        this.parameters = parameters;
    }


    public static Message requestMessage(List<Parameter> parameters) {
        return new Message(REQUEST, REQUEST, parameters);
    }

    public static Message responseMessage(String status, String priorStatus, List<Parameter> parameters) {
        return new Message(RESPONSE + " " + status, RESPONSE + " " + priorStatus, parameters);
    }
}
