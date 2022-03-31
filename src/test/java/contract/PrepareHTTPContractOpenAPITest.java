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
                StandardOpenOption.TRUNCATE_EXISTING
        );

        String getRequestProperties = HTTPContractOpenAPITest.getRequestProperties();
        Files.writeString(
                Paths.get(HTTPContractOpenAPITest.basePath + "getRequestProperties.txt"),
                getRequestProperties,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        String getResponses = HTTPContractOpenAPITest.getResponses();
        Files.writeString(
                Paths.get(HTTPContractOpenAPITest.basePath + "getResponses.txt"),
                getResponses,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

}
