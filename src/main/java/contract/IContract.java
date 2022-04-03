package contract;

import contract.structures.Endpoint;
import contract.structures.Property;

import java.util.List;
import java.util.Set;

public interface IContract {

    Set<Endpoint> getEndpoints();

    List<String> getResponses(Endpoint endpoint);

    Set<Property> getRequestProperties(Endpoint endpoint);

    Set<Property> getResponseProperties(Endpoint endpoint, String status);
}
