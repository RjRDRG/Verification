package generator;

import contract.OpenApiContract;
import contract.structures.Endpoint;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.ParseOptions;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class MainFrame extends JFrame {

    public static void main(String[] args) {
        new MainFrame();
    }

    private MainFrame() {
        super();

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenApiContract oldV = new OpenApiContract(
                new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI());
        OpenApiContract newV = new OpenApiContract(
                new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI()
        );

        setNimbusStyle();

        Set<String> newEndpoints = newV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toSet());
        Set<String> oldEndpoints = oldV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toSet());

        Set<String> intersection = new HashSet<>(newEndpoints);
        intersection.retainAll(oldEndpoints);

        newEndpoints.removeAll(intersection);
        oldEndpoints.removeAll(intersection);

        DualPairPickerPanel panel = new DualPairPickerPanel(
                "Contract Endpoints: " + "./src/main/resources/new.yaml", newEndpoints,
                "Prior Contract Endpoints: " + "./src/main/resources/old.yaml", oldEndpoints,
                intersection,

        );

        int borderPad = 30;

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(Box.createRigidArea(new Dimension(0,borderPad)), BorderLayout.PAGE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(0,borderPad)), BorderLayout.PAGE_END);
        getContentPane().add(Box.createRigidArea(new Dimension(borderPad,0)), BorderLayout.LINE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(borderPad,0)), BorderLayout.LINE_END);
        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(new Dimension(1000, 1000));
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setNimbusStyle() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", new Color(128, 128, 128));
            UIManager.put("info", new Color(128, 128, 128));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException exc) {
            System.err.println("Nimbus: Unsupported Look and feel!");
        }
    }
}