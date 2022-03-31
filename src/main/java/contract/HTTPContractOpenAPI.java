package contract;

import contract.structures.Endpoint;
import contract.structures.Property;
import contract.structures.PropertyKey;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.*;
import java.util.stream.Collectors;

public class HTTPContractOpenAPI implements IHTTPContract {

    OpenAPI api;

    public HTTPContractOpenAPI(OpenAPI api) {
        this.api = api;
    }

    @Override
    public Set<Endpoint> getEndpoints() {
        Set<Endpoint> endpoints = new HashSet<>();

        for(Map.Entry<String,PathItem> item : api.getPaths().entrySet()) {
            endpoints.addAll(extractEndpointsKeys(item.getKey(), item.getValue()));
        }

        return endpoints;
    }

    @Override
    public Set<Property> getRequestProperties(Endpoint endpoint) {
        Set<Property> properties = getRequestParameterProperties(endpoint);
        properties.addAll(getRequestBodyProperties(endpoint));
        return properties;
    }

    @Override
    public List<String> getResponses(Endpoint endpoint) {
        Operation operation = extractOperation(endpoint);
        return new ArrayList<>(operation.getResponses().keySet());
    }

    @Override
    public Set<Property> getResponseProperties(Endpoint endpoint, String responseStatus) {
        Operation operation = extractOperation(endpoint);

        ApiResponse response = operation.getResponses().get(responseStatus);

        Map.Entry<String, MediaType> entry = operation.getResponses().get(responseStatus).getContent().entrySet().stream().findFirst().orElse(null);

        Set<Property> propertySet = new HashSet<>();

        if(entry != null) {
            String mediaType = entry.getKey();
            Schema schema = entry.getValue().getSchema();

            if(mediaType.equals("application/json")) {
                addPropertiesFromJsonSchema(propertySet, new LinkedList<>(), schema, operation.getRequestBody().getRequired());
            }
        }

        return propertySet;
    }

    //------------------------------------------------------------------------------------------------------------------

    private Set<Endpoint> extractEndpointsKeys(String path, PathItem item) {
        Set<Endpoint> keys = new HashSet<>();
        if(item.getGet()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.GET));
        }
        if(item.getPost()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.POST));
        }
        if(item.getPut()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.PUT));
        }
        if(item.getDelete()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.DELETE));
        }
        if(item.getHead()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.HEAD));
        }
        if(item.getOptions()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.OPTIONS));
        }
        if(item.getPatch()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.PATCH));
        }
        if(item.getTrace()!=null) {
            keys.add(new Endpoint(path, Endpoint.Method.TRACE));
        }
        return keys;
    }

    private Operation extractOperation(Endpoint endpoint) {
        PathItem path = api.getPaths().get(endpoint.path);
        Operation operation;
        switch (endpoint.method) {
            case GET:
                operation = path.getGet();
                break;
            case PUT:
                operation = path.getPut();
                break;
            case HEAD:
                operation = path.getHead();
                break;
            case POST:
                operation = path.getPost();
                break;
            case PATCH:
                operation = path.getPatch();
                break;
            case TRACE:
                operation = path.getTrace();
                break;
            case DELETE:
                operation = path.getDelete();
                break;
            case OPTIONS:
                operation = path.getOptions();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + endpoint.method);
        }
        return operation;
    }

    public Set<Property> getRequestParameterProperties(Endpoint key) {
        Operation operation = extractOperation(key);

        Set<Property> propertySet = new HashSet<>();

        for(Parameter p : operation.getParameters()) {
            propertySet.add(
                new Property(
                        PropertyKey.Type.PARAMETER,
                        PropertyKey.Location.valueOf(p.getIn().toUpperCase()),
                        Collections.emptyList(),
                        p.getName(),
                        false,
                        p.getSchema().getType(),
                        p.getSchema().getFormat(),
                        p.getRequired(),
                        !p.getRequired() ? String.valueOf(p.getSchema().getDefault()) : null
                )
            );
        }

        return propertySet;
    }

    public Set<Property> getRequestBodyProperties(Endpoint key) {
        Operation operation = extractOperation(key);

        Map.Entry<String, MediaType> entry = operation.getRequestBody().getContent().entrySet().stream().findFirst().orElse(null);

        Set<Property> propertySet = new HashSet<>();

        if(entry != null) {
            String mediaType = entry.getKey();
            Schema schema = entry.getValue().getSchema();

            if(mediaType.equals("application/json")) {
                addPropertiesFromJsonSchema(propertySet, new LinkedList<>(), schema, operation.getRequestBody().getRequired());
            }
        }

        return propertySet;
    }

    private void addPropertiesFromJsonSchema(Set<Property> propertySet, List<String> precursors, Schema schema, boolean required) {
        if(schema.getType().equals("object")) {
            Map<String, Schema> schemaProperties = schema.getProperties();
            for (Map.Entry<String, Schema> entry : schemaProperties.entrySet()) {
                List<String> newPrecursors = new LinkedList<>(precursors);
                precursors.add(entry.getKey());
                addPropertiesFromJsonSchema(propertySet, newPrecursors, entry.getValue(), schema.getRequired().contains(entry.getKey()));
            }
        }
        else if(schema.getType().matches("integer|string|number|boolean")) {
            propertySet.add(
                    new Property(
                            PropertyKey.Type.BODY,
                            PropertyKey.Location.JSON,
                            precursors,
                            schema.getName(),
                            false,
                            schema.getType(),
                            schema.getFormat(),
                            required,
                            !required ? String.valueOf(schema.getDefault()) : null
                    )
            );
        }
        else if(schema.getType().matches("array")) {
            propertySet.add(
                    new Property(
                            PropertyKey.Type.BODY,
                            PropertyKey.Location.JSON,
                            precursors,
                            schema.getName(),
                            true,
                            "UNKNOWN", //TODO. see where array item schema is stored in Schema class? properties? additional properties? it should have a field called items.
                            "UNKNOWN",
                            required,
                            !required ? String.valueOf(schema.getDefault()) : null
                    )
            );
        }
        // TODO. dictionary type. key is present in schema.type, value type is present in schema.additionalProperties.?
    }

}
