import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import validation.CompatibilityValidator;
import validation.result.Result;
import validation.evolutions.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yml", null, null).getOpenAPI();
        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yml", null, null).getOpenAPI();

        CompatibilityValidator validator = new CompatibilityValidator(
                new ParameterAddition(),
                new ParameterRemoval(),
                new ParameterRename()
        );

        Result result = validator.process(oldV, newV);

        writeResultToFile(result);
    }



    private static void writeResultToFile(Result result) {
        try {
            PrintWriter writer = new PrintWriter("./src/main/resources/result.yml");
            DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(result, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
