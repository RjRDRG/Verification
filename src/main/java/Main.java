import contract.OpenApiContract;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import generator.utils.ResultIO;
import generator.CompatibilityGenerator;
import generator.structures.Result;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI();
        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI();

        CompatibilityGenerator validator = new CompatibilityGenerator(
                new OpenApiContract(oldV),
                new OpenApiContract(newV)
        );

        Result result = validator.process();
        ResultIO.output(result);
    }

}
