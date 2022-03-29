package validation;

import contract.structures.Endpoint;
import contract.IHTTPContract;
import contract.structures.Property;
import contract.structures.PropertyKey;
import validation.http.*;
import validation.resolution.IResolutionAdviser;
import validation.resolution.Resolution;
import validation.result.*;

import java.io.FileNotFoundException;
import java.util.HashSet;
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
        for (Endpoint key: newEndpoints) {
            if(oldEndpoints.contains(key)) {
                result.addEndpoint(new Method(key, key));
            }
            else {
                missingEndpoints = true;
                result.addEndpoint(new Method(key, new Endpoint("MISSING", MISSING)));
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

    private void processRequest(Method method) {
        Request request = method.request;

        Set<Property> newP = newContract.getRequestProperties(method.newEndpoint);
        Set<Property> oldP = oldContract.getRequestProperties(method.oldEndpoint);

        Set<Parameter> parameters = new HashSet<>();
        Set<Field> fields = new HashSet<>();

        Set<Property> intersection = new HashSet<>(newP);
        intersection.retainAll(oldP);

        for (Property p : intersection) {
            if(p.key.type == PropertyKey.Type.PARAMETER) {
                Parameter parameter = new Parameter(p.key);
                if(!p.array) {
                    parameter.setResolution(Resolution.keyResolution(p.key));
                }
                else {
                    parameter.setSuggestions(List.of(Resolution.keyResolution(p.key)));
                }
                parameters.add(parameter);
            }
            else if(p.key.type == PropertyKey.Type.BODY) {
                Field field = new Field(p.key);
                if(!p.array) {
                    field.setResolution(Resolution.keyResolution(p.key));
                }
                else {
                    field.setSuggestions(List.of(Resolution.keyResolution(p.key)));
                }
                fields.add(field);
            }
        }

        Set<Property> unmapped = new HashSet<>(newP);
        unmapped.removeAll(intersection);

        Set<Property> unused = new HashSet<>(oldP);
        unused.removeAll(intersection);

        for(Property p : unmapped) {
            List<Resolution> suggestions = resolutionBuilder.solve(p, unused);

            if(p.key.type == PropertyKey.Type.PARAMETER) {
                Parameter parameter = new Parameter(p.key);
                parameter.setSuggestions(suggestions);

                parameters.add(parameter);
            }
            else if(p.key.type == PropertyKey.Type.BODY) {

            }
        }

        //newContract.getPropertySuccessors(method.key, new PropertyKey(PropertyKey.Type.BODY, location, Collections.emptyList(), null));

        request.setParameters(parameters);
    }

    // RESPONSE --------------------------------------------------------------------------------------------------------

    private void processResponse(Method method) {

    }


}
