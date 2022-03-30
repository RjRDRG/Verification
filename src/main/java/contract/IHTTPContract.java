package contract;

import contract.structures.Endpoint;
import contract.structures.Property;
import contract.structures.PropertyKey;

import java.util.List;
import java.util.Set;

public interface IHTTPContract {

    Set<Endpoint> getEndpoints();

    Set<Property> getRequestProperties(Endpoint endpoint);

    List<String> getResponses(Endpoint endpoint);

    Set<Property> getResponseProperties(Endpoint endpoint, String status);
}
