import contract.OpenApiContract;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import generator.old.InitPanel;

public class Main {

    public static void main(String[] args) {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI();
        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI();

        new InitPanel(new OpenApiContract(newV), new OpenApiContract(oldV));
    }

}
