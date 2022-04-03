package generator;

import contract.structures.Endpoint;
import contract.IContract;
import contract.structures.Property;
import resolution.LinkResolutionAdviser;
import resolution.ValueResolutionAdviser;
import resolution.structures.Resolution;
import generator.utils.*;
import generator.structures.*;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static contract.structures.Endpoint.Method.MISSING;

public class CompatibilityGenerator {

    IContract oldContract;
    IContract newContract;

    ValueResolutionAdviser valueResolutionAdviser;
    LinkResolutionAdviser linkResolutionAdviser;

    Result result;

    public CompatibilityGenerator(IContract oldContract, IContract newContract) {
        this.oldContract = oldContract;
        this.newContract = newContract;
        this.valueResolutionAdviser = new ValueResolutionAdviser();
        this.linkResolutionAdviser = new LinkResolutionAdviser();
        this.result = new Result();
    }

    public Result process() throws FileNotFoundException {
        processEndpoints();

        /*
        for (Method method : result.getEndpoints()) {
            processRequest(method);
        }

        for (Method method : result.getEndpoints()) {
            processResponse(method);
        }
         */
        return result;
    }

    // ENDPOINT --------------------------------------------------------------------------------------------------------

    public void processEndpoints() throws FileNotFoundException {
        Set<Endpoint> newEndpoints = newContract.getEndpoints();
        Set<Endpoint> oldEndpoints = oldContract.getEndpoints();

        Set<Endpoint> intersection = new HashSet<>(newEndpoints);
        intersection.retainAll(oldEndpoints);

        Set<Endpoint> unmapped = new HashSet<>(newEndpoints);
        unmapped.removeAll(intersection);

        for (Endpoint endpoint: intersection) {
            result.addEndpoint(new Method(endpoint, endpoint));
        }

        for (Endpoint endpoint: unmapped) {
            result.addEndpoint(new Method(endpoint, new Endpoint("MISSING", MISSING)));
        }

        if(!unmapped.isEmpty()) {
            ResultIO.requestIntervention(result);
        }
    }

    /*

    // REQUEST ---------------------------------------------------------------------------------------------------------

    public void processRequest(Method method) {
        Request request = method.request;

        Set<Property> newP = newContract.getRequestProperties(method.newEndpoint);
        Set<Property> oldP = oldContract.getRequestProperties(method.oldEndpoint);

        List<Parameter> parameters = processProperties(newP,oldP);

        method.setRequest(new Request(parameters));
    }

    // RESPONSE --------------------------------------------------------------------------------------------------------

    public void processResponse(Method method) {
        List<Message> respons = new LinkedList<>();

        for(String response : newContract.getResponses(method.newEndpoint)) {
            Set<Property> newP = newContract.getResponseProperties(method.newEndpoint, response);
            Set<Property> oldP = oldContract.getResponseProperties(method.oldEndpoint, response);
            List<Parameter> parameters = processProperties(newP,oldP);
            respons.add(new Message(response, response, parameters));
        }

        method.setResponses(respons);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private List<Parameter> processProperties(Set<Property> newP, Set<Property> oldP) {

        List<Parameter> parameters = new LinkedList<>();

        Set<Property> intersection = new HashSet<>(newP);
        intersection.retainAll(oldP);

        for (Property p : intersection) {
            Parameter parameter = new Parameter(p.key);
            if(!p.array) {
                parameter.setResolution(Resolution.linkResolution(p.key));
            }
            else {
                parameter.setSuggestions(List.of(Resolution.linkResolution(p.key)));
            }
            parameters.add(parameter);
        }

        for(Property p : newP) {
            List<Resolution> suggestions = resolutionBuilder.solve(p, oldP);

            Parameter parameter = new Parameter(p.key);
            parameter.setSuggestions(suggestions);

            parameters.add(parameter);
        }

        return parameters;
    }*/

}
