package generator.structures;

import java.util.List;

public class Message {

    public final static String REQUEST = "request";
    public final static String RESPONSE = "response";

    public String type;
    public String priorType;
    public List<Parameter> parameters;

    public Message(String type, String priorType, List<Parameter> parameters) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriorType() {
        return priorType;
    }

    public void setPriorType(String priorType) {
        this.priorType = priorType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
