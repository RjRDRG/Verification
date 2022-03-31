package contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PrepareHTTPContractOpenAPITest {

    public static void main(String[] args) throws IOException { //Generate logs
        HTTPContractOpenAPITest.setup();

        String getEndpoints = HTTPContractOpenAPITest.getEndpoints();
        Files.writeString(
                Paths.get(HTTPContractOpenAPITest.basePath + "getEndpoints.txt"),
                getEndpoints,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        );

        String getRequestProperties = HTTPContractOpenAPITest.getRequestProperties();
        Files.writeString(
                Paths.get(HTTPContractOpenAPITest.basePath + "getRequestProperties.txt"),
                getRequestProperties,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        );

        String getResponses = HTTPContractOpenAPITest.getResponses();
        Files.writeString(
                Paths.get(HTTPContractOpenAPITest.basePath + "getResponses.txt"),
                getResponses,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        );

        String getResponseProperties = HTTPContractOpenAPITest.getResponseProperties();
        Files.writeString(
                Paths.get(HTTPContractOpenAPITest.basePath + "getResponseProperties.txt"),
                getResponseProperties,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        );
    }

}
