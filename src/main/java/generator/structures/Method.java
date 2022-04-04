package generator.structures;

import contract.structures.Endpoint;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Method {

    public Endpoint endpoint;
    public Endpoint priorEndpoint;

    public Set<Message> messages;

    public Method(Endpoint endpoint, Endpoint priorEndpoint) {
        this.endpoint = endpoint;
        this.priorEndpoint = priorEndpoint;
        this.messages = new HashSet<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Endpoint getPriorEndpoint() {
        return priorEndpoint;
    }

    public void setPriorEndpoint(Endpoint priorEndpoint) {
        this.priorEndpoint = priorEndpoint;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
