package utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import validation.result.Endpoint;

import java.util.LinkedList;
import java.util.List;

public class APIUtils {

    public static List<Endpoint> extractEndpoints(OpenAPI api) {
        List<Endpoint> endpoints = new LinkedList<>();
        for(PathItem pathItem : api.getPaths().values()) {
            endpoints.addAll(extractEndpoints(pathItem));
        }
        return endpoints;
    }

    public static List<Endpoint> extractEndpoints(PathItem pathItem) {

    }

}
