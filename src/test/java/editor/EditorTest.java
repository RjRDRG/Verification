package editor;

import contract.IContract;
import contract.OpenApiContract;
import contract.structures.Endpoint;
import contract.structures.Property;
import io.ResultIO;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.Message;
import structures.Method;
import structures.Parameter;
import structures.Result;

import java.io.FileNotFoundException;

class EditorTest {

    static String basePath = "./src/test/resources/editor/";

    Result result;
    IContract contract;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        result = ResultIO.readFromYaml(basePath + "result.yml");

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI api = new OpenAPIParser().readLocation(basePath + "contract.yml", null, parseOptions).getOpenAPI();

        contract = new OpenApiContract(api);
    }

    @Test
    void testResult() {
        for(Endpoint endpoint : contract.getEndpoints()) {
            Method method = result.getMethods().stream().filter(m->m.endpoint.equals(endpoint.toString())).findFirst().orElse(null);
            assert method != null;

            Message requestMessage = method.getMessages().stream().filter(m->m.type.equals("Request")).findFirst().orElse(null);
            assert requestMessage != null;
            for (Property property : contract.getRequestProperties(endpoint)) {
                Parameter parameter = requestMessage.parameters.stream().filter(p-> p.key.equals(property.key.toString())).findFirst().orElse(null);
                assert parameter != null;
                assert parameter.resolution != null;
            }
            for(String response : contract.getResponses(endpoint)){
                Message responseMessage = method.getMessages().stream().filter(m->m.type.equals(response)).findFirst().orElse(null);
                assert responseMessage != null;
                for (Property property : contract.getResponseProperties(endpoint,response)) {
                    Parameter parameter = responseMessage.parameters.stream().filter(p-> p.key.equals(property.key.toString())).findFirst().orElse(null);
                    assert parameter != null;
                    assert parameter.resolution != null;
                }
            }
        }
    }
}