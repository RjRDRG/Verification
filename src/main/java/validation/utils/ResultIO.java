package validation.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import validation.structures.Result;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class ResultIO {

    static String RESULT_LOCATION = "./src/main/resources/result.yml";

    private static boolean isWindows = false;
    private static boolean isLinux = false;
    private static boolean isMac = false;

    static
    {
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
        isLinux = os.contains("nux") || os.contains("nix");
        isMac = os.contains("mac");
    }

    public static void requestIntervention(Result result) throws FileNotFoundException {
        writeToYaml(result);

        System.out.println("Multiple matches were detected, please complete the marked missing clauses manually");
        ResultIO.openInWindow();
        System.out.println("Continue? Y/n");

        Scanner in = new Scanner(System.in);
        boolean continueProcess = in.nextLine().equalsIgnoreCase("Y");

        if(continueProcess) {
            result.copyValues(readFromYaml());
        }
        else {
            System.exit(1);
        }
    }

    public static void output(Result result) throws FileNotFoundException {
        ResultIO.writeToYaml(result);
        ResultIO.openInWindow();
    }

    private static void openInWindow()
    {
        try {
            File file = new File(RESULT_LOCATION);

            if (isWindows) {
                Runtime.getRuntime().exec(new String[]
                        {"rundll32", "url.dll,FileProtocolHandler",
                                file.getAbsolutePath()});
            }
            else if (isLinux || isMac) {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", file.getAbsolutePath()});
            }
            else if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private static Result readFromYaml() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(RESULT_LOCATION);
        Yaml yaml = new Yaml(new Constructor(Result.class));
        return yaml.load(inputStream);
    }

    private static void writeToYaml(Result result) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(RESULT_LOCATION);
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        yaml.dump(result, writer);
    }
}
