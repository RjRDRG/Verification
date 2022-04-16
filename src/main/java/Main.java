import com.formdev.flatlaf.FlatDarculaLaf;
import contract.OpenApiContract;
import generator.EditorFrame;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        FlatDarculaLaf.setup();
        UIManager.put("Tree.paintLines", true);

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenAPI oldV = new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI();
        OpenAPI newV = new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI();

        new EditorFrame(new OpenApiContract(newV), new OpenApiContract(oldV));
    }

}
