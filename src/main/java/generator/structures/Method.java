package generator.structures;

import contract.structures.Endpoint;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Method {

    public final Endpoint endpoint;
    public final Endpoint priorEndpoint;

    public Set<Message> messages;

    public Method(Endpoint endpoint, Endpoint priorEndpoint) {
        this.endpoint = endpoint;
        this.priorEndpoint = priorEndpoint;
        this.messages = new HashSet<>();
    }

    void addMessage(Message message) {
        messages.add(message);
    }

}
