package contract;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import validation.http.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContractHTTP {

    OpenAPI api;

    public ContractHTTP(OpenAPI api) {
        this.api = api;
    }

    public Set<EndpointKey> extractEndpointsKeys() {
        Set<EndpointKey> endpoints = new HashSet<>();

        for(Map.Entry<String,PathItem> item : api.getPaths().entrySet()) {
            endpoints.addAll(extractEndpointsKeys(item.getKey(), item.getValue()));
        }

        return endpoints;
    }

    private Set<EndpointKey> extractEndpointsKeys(String path, PathItem item) {
        Set<EndpointKey> keys = new HashSet<>();
        if(item.getGet()!=null) {
            keys.add(new EndpointKey(path, Method.GET));
        }
        if(item.getPost()!=null) {
            keys.add(new EndpointKey(path, Method.POST));
        }
        if(item.getPut()!=null) {
            keys.add(new EndpointKey(path, Method.PUT));
        }
        if(item.getDelete()!=null) {
            keys.add(new EndpointKey(path, Method.DELETE));
        }
        if(item.getHead()!=null) {
            keys.add(new EndpointKey(path, Method.HEAD));
        }
        if(item.getOptions()!=null) {
            keys.add(new EndpointKey(path, Method.OPTIONS));
        }
        if(item.getPatch()!=null) {
            keys.add(new EndpointKey(path, Method.PATCH));
        }
        if(item.getTrace()!=null) {
            keys.add(new EndpointKey(path, Method.TRACE));
        }
        return keys;
    }

    private Operation extractOperation(EndpointKey key) {
        PathItem path = api.getPaths().get(key.path);
        Operation operation;
        switch (key.method) {
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
                throw new IllegalStateException("Unexpected value: " + key.method);
        }
        return operation;
    }

    public Set<Property> extractRequestParameterProperties(EndpointKey key) {
        Operation operation = extractOperation(key);

        Set<Property> propertySet = new HashSet<>();

        for(Parameter p : operation.getParameters()) {
            propertySet.add(
                new Property(
                        p.getName(),
                        p.getIn(),
                        p.getSchema().getType(),
                        p.getRequired(),
                        !p.getRequired() ? String.valueOf(p.getSchema().getDefault()) : null
                )
            );
        }

        return propertySet;
    }

    public Form extractRequestBodyForm(EndpointKey key) {
        Operation operation = extractOperation(key);

        Map.Entry<String, MediaType> entry = operation.getRequestBody().getContent().entrySet().stream().findFirst().orElse(null);

        if(entry != null) {
            String mediaType = entry.getKey();
            Schema schema = entry.getValue().getSchema();
            Form form = new Form(mediaType);

            String schemaType = entry.getValue().getSchema().getType();
            if(mediaType.equals("application/json")) {
                if(schemaType.equals("object")) {
                    for (Map.Entry<String, Schema> schemaEntry : schema.getProperties().entrySet()) {

                    }
                }
            }
        }
    }
}
