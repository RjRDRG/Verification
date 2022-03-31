package contract;

import contract.structures.Endpoint;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HTTPContractOpenAPITest {

    static IHTTPContract contract;

    @BeforeAll
    static void setup() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI api = new OpenAPIParser().readLocation("./src/test/resources/HTTPContractOpenAPITest.yaml", null, parseOptions).getOpenAPI();

        contract = new HTTPContractOpenAPI(api);
    }

    @Test
    void getEndpoints() {
        Set<Endpoint> endpointSet = contract.getEndpoints();

        assert endpointSet.size() == 3;
    }

    @Test
    void getRequestProperties() {
    }

    @Test
    void getResponses() {
    }

    @Test
    void getResponseProperties() {
    }
}