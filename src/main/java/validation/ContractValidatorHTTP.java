package validation;

import contract.ContractHTTP;
import validation.http.*;
import validation.resolution.IResolutionAdviser;
import validation.resolution.Resolution;
import validation.result.*;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static validation.http.Method.MISSING;

public class ContractValidatorHTTP {

    ContractHTTP oldContract;
    ContractHTTP newContract;

    IResolutionAdviser resolutionBuilder;

    Result result;

    public ContractValidatorHTTP(ContractHTTP oldContract, ContractHTTP newContract, IResolutionAdviser resolutionBuilder) {
        this.oldContract = oldContract;
        this.newContract = newContract;
        this.resolutionBuilder = resolutionBuilder;
        this.result = new Result();
    }

    public Result process() throws FileNotFoundException {
        processEndpoints();

        for (Endpoint endpoint: result.getEndpoints()) {
            processRequest(endpoint);
        }

        for (Endpoint endpoint: result.getEndpoints()) {
            processResponse(endpoint);
        }

        return result;
    }

    // ENDPOINT --------------------------------------------------------------------------------------------------------

    public void processEndpoints() throws FileNotFoundException {
        Set<EndpointKey> newEndpoints = newContract.extractEndpointsKeys();
        Set<EndpointKey> oldEndpoints = oldContract.extractEndpointsKeys();

        boolean missingEndpoints = false;
        for (EndpointKey key: newEndpoints) {
            if(oldEndpoints.contains(key)) {
                result.addEndpoint(new Endpoint(key, key));
            }
            else {
                missingEndpoints = true;
                result.addEndpoint(new Endpoint(key, new EndpointKey("MISSING", MISSING)));
            }
        }

        if(!missingEndpoints) {
            result.setSoundness(Soundness.COMPATIBLE);
        }
        else {
            result.setSoundness(Soundness.UNKNOWN);
            ResultIO.requestIntervention(result);
        }
    }

    // REQUEST ---------------------------------------------------------------------------------------------------------

    private void processRequest(Endpoint endpoint) {
        Request request = endpoint.request;

        Set<Property> newP = newContract.extractRequestParameterProperties(endpoint.key);
        Set<Property> oldP = oldContract.extractRequestParameterProperties(endpoint.oldKey);

        Set<Parameter> parameters = new HashSet<>();

        Set<Property> intersection = new HashSet<>(newP);
        intersection.retainAll(oldP);

        for (Property p : intersection) {
            Parameter parameter = new Parameter(p.key, p.location);
            parameter.setResolution(Resolution.keyResolution(p.key, p.location));
            parameters.add(parameter);
        }

        Set<Property> unmapped = new HashSet<>(newP);
        unmapped.removeAll(intersection);

        Set<Property> unused = new HashSet<>(oldP);
        unused.removeAll(intersection);

        for(Property p : unmapped) {
            List<Resolution> suggestions = resolutionBuilder.solve(p, unused);

            Parameter parameter = new Parameter(p.key, p.location);
            parameter.setSuggestions(suggestions);

            parameters.add(parameter);
        }

        request.setParameters(parameters);
    }

    // RESPONSE --------------------------------------------------------------------------------------------------------

    private void processResponse(Endpoint endpoint) {

    }


}
