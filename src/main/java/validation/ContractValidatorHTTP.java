package validation;

import contract.structures.Endpoint;
import contract.IHTTPContract;
import contract.structures.Property;
import resolution.IResolutionAdviser;
import resolution.structures.Resolution;
import validation.utils.*;
import validation.structures.*;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static contract.structures.Endpoint.Method.MISSING;

public class ContractValidatorHTTP {

    IHTTPContract oldContract;
    IHTTPContract newContract;

    IResolutionAdviser resolutionBuilder;

    Result result;

    public ContractValidatorHTTP(IHTTPContract oldContract, IHTTPContract newContract, IResolutionAdviser resolutionBuilder) {
        this.oldContract = oldContract;
        this.newContract = newContract;
        this.resolutionBuilder = resolutionBuilder;
        this.result = new Result();
    }

    public Result process() throws FileNotFoundException {
        processEndpoints();

        for (Method method : result.getEndpoints()) {
            processRequest(method);
        }

        for (Method method : result.getEndpoints()) {
            processResponse(method);
        }

        return result;
    }

    // ENDPOINT --------------------------------------------------------------------------------------------------------

    public void processEndpoints() throws FileNotFoundException {
        Set<Endpoint> newEndpoints = newContract.getEndpoints();
        Set<Endpoint> oldEndpoints = oldContract.getEndpoints();

        boolean missingEndpoints = false;
        for (Endpoint endpoint: newEndpoints) {
            if(oldEndpoints.contains(endpoint)) {
                result.addEndpoint(new Method(endpoint, endpoint));
            }
            else {
                missingEndpoints = true;
                result.addEndpoint(new Method(endpoint, new Endpoint("MISSING", MISSING)));
            }
        }

        if(missingEndpoints) {
            ResultIO.requestIntervention(result);
        }
    }

    // REQUEST ---------------------------------------------------------------------------------------------------------

    private void processRequest(Method method) {
        Request request = method.request;

        Set<Property> newP = newContract.getRequestProperties(method.newEndpoint);
        Set<Property> oldP = oldContract.getRequestProperties(method.oldEndpoint);

        List<Parameter> parameters = processProperties(newP,oldP);

        method.setRequest(new Request(parameters));
    }

    // RESPONSE --------------------------------------------------------------------------------------------------------

    private void processResponse(Method method) {
        List<Response> responses = new LinkedList<>();

        for(String response : newContract.getResponses(method.newEndpoint)) {
            Set<Property> newP = newContract.getResponseProperties(method.newEndpoint, response);
            Set<Property> oldP = oldContract.getResponseProperties(method.oldEndpoint, response);
            List<Parameter> parameters = processProperties(newP,oldP);
            responses.add(new Response(response, response, parameters));
        }

        method.setResponses(responses);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private List<Parameter> processProperties(Set<Property> newP, Set<Property> oldP) {

        List<Parameter> parameters = new LinkedList<>();

        Set<Property> intersection = new HashSet<>(newP);
        intersection.retainAll(oldP);

        for (Property p : intersection) {
            Parameter parameter = new Parameter(p.key);
            if(!p.array) {
                parameter.setResolution(Resolution.keyResolution(p.key));
            }
            else {
                parameter.setSuggestions(List.of(Resolution.keyResolution(p.key)));
            }
            parameters.add(parameter);
        }

        Set<Property> unmapped = new HashSet<>(newP);
        unmapped.removeAll(intersection);

        Set<Property> unused = new HashSet<>(oldP);
        unused.removeAll(intersection);

        for(Property p : unmapped) {
            List<Resolution> suggestions = resolutionBuilder.solve(p, unused);

            Parameter parameter = new Parameter(p.key);
            parameter.setSuggestions(suggestions);

            parameters.add(parameter);
        }

        return parameters;
    }

}
