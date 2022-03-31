package contract;

import contract.structures.Endpoint;
import contract.structures.Property;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

class HTTPContractOpenAPITest {

    static String basePath = "./src/test/resources/HTTPContractOpenAPITest/";

    static IHTTPContract contract;

    @BeforeAll
    static void setup() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI api = new OpenAPIParser().readLocation(basePath + "contract.yaml", null, parseOptions).getOpenAPI();

        contract = new HTTPContractOpenAPI(api);
    }

    static void assertAgainstFileContent(String content, Path filePath) {
        try {
            String fileContent = Files.readString(filePath, StandardCharsets.US_ASCII);
            assert fileContent.equals(content);
        } catch (IOException e) {
            System.err.println("Expected values where not found for test: " + filePath.getFileName().toString());
        }
    }

    @Test
    static String getEndpoints() {
        Set<Endpoint> endpointSet = contract.getEndpoints();

        StringBuilder stringBuilder = new StringBuilder();
        for (Endpoint endpoint : endpointSet) {
            stringBuilder.append(endpoint.toString()).append("\n");
        }

        String newContent = stringBuilder.toString();

        assertAgainstFileContent(newContent, Paths.get(basePath + "getEndpoints.txt"));

        return newContent;
    }

    @Test
    static String getRequestProperties() {
        Set<Endpoint> endpointSet = contract.getEndpoints();

        StringBuilder stringBuilder = new StringBuilder();
        for (Endpoint endpoint : endpointSet) {
            stringBuilder.append(endpoint.toString()).append("\n");
            for (Property p : contract.getRequestProperties(endpoint))
                stringBuilder.append("\t").append(p.toString()).append("\n");
            stringBuilder.append("\n");
        }

        String newContent = stringBuilder.toString();

        assertAgainstFileContent(newContent, Paths.get(basePath + "getRequestProperties.txt"));

        return newContent;
    }

    @Test
    static String getResponses() {
        Set<Endpoint> endpointSet = contract.getEndpoints();

        StringBuilder stringBuilder = new StringBuilder();
        for (Endpoint endpoint : endpointSet) {
            stringBuilder.append(endpoint.toString()).append("\n");
            for (String r : contract.getResponses(endpoint))
                stringBuilder.append("\t").append(r).append(" ");
            stringBuilder.append("\n\n");
        }

        String newContent = stringBuilder.toString();

        assertAgainstFileContent(newContent, Paths.get(basePath + "getResponses.txt"));

        return newContent;
    }

    @Test
    static String getResponseProperties() {
        Set<Endpoint> endpointSet = contract.getEndpoints();

        StringBuilder stringBuilder = new StringBuilder();
        for (Endpoint endpoint : endpointSet) {
            stringBuilder.append(endpoint.toString()).append("\n");
            for (String r : contract.getResponses(endpoint)) {
                stringBuilder.append("\t").append(r).append(" ");
                for (Property p : contract.getResponseProperties(endpoint,r))
                    stringBuilder.append("\t\t").append(p.toString()).append("\n");
            }
            stringBuilder.append("\n");
        }

        String newContent = stringBuilder.toString();

        assertAgainstFileContent(newContent, Paths.get(basePath + "getResponses.txt"));

        return newContent;
    }
}