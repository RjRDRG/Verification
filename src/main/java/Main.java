import contract.ContractHTTP;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import validation.resolution.SimpleResolutionAdviser;
import validation.result.ResultIO;
import validation.ContractValidatorHTTP;
import validation.result.Result;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yml", null, parseOptions).getOpenAPI();
        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yml", null, parseOptions).getOpenAPI();

        ContractValidatorHTTP validator = new ContractValidatorHTTP(
                new ContractHTTP(oldV),
                new ContractHTTP(newV),
                new SimpleResolutionAdviser()
        );

        Result result = validator.process();
        ResultIO.output(result);
    }

}
