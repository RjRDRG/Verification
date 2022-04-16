package generator.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import generator.structures.Result;

import java.io.*;

public class ResultIO {

    static String RESULT_LOCATION = "./src/main/resources/result.yml";

    public static Result readFromYaml() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(RESULT_LOCATION);
        Yaml yaml = new Yaml(new Constructor(Result.class));
        return yaml.load(inputStream);
    }

    public static void writeToYaml(Result result) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(RESULT_LOCATION);
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        yaml.dump(result, writer);
    }
}
