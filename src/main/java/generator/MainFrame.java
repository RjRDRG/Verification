package generator;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import contract.OpenApiContract;
import contract.structures.Endpoint;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.ParseOptions;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class MainFrame extends JFrame {

    public static void main(String[] args) {
        FlatDarculaLaf.setup();

        new MainFrame();
    }

    private MainFrame() {
        super();

        int borderPad = 20;

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(Box.createRigidArea(new Dimension(0,0)), BorderLayout.PAGE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(0,borderPad)), BorderLayout.PAGE_END);
        getContentPane().add(Box.createRigidArea(new Dimension(borderPad,0)), BorderLayout.LINE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(borderPad,0)), BorderLayout.LINE_END);
        getContentPane().add(getMainPanel(), BorderLayout.CENTER);
        setSize(new Dimension(1000, 1000));
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public JPanel getMainPanel() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        OpenApiContract oldV = new OpenApiContract(
                new OpenAPIParser().readLocation("./src/main/resources/old.yaml", null, parseOptions).getOpenAPI());
        OpenApiContract newV = new OpenApiContract(
                new OpenAPIParser().readLocation("./src/main/resources/new.yaml", null, parseOptions).getOpenAPI()
        );

        Set<Endpoint> e0 = newV.getEndpoints();
        Set<Endpoint> e1 = oldV.getEndpoints();

        Set<Endpoint> intersection = new HashSet<>(e0);
        intersection.retainAll(e1);

        e0.removeAll(intersection);
        e1.removeAll(intersection);

        Map<String,Set<String>> ele0 = new HashMap<>();
        for(Endpoint e : e0) {
            ele0.put(e.toString(), new HashSet<>(newV.getResponses(e)));
        }

        Map<String,Set<String>> ele1 = new HashMap<>();
        for(Endpoint e : e1) {
            ele1.put(e.toString(), new HashSet<>(oldV.getResponses(e)));
        }

        Map<String,Set<String>> ele2 = new HashMap<>();
        for(Endpoint e : intersection) {
            ele2.put(e.toString(), new HashSet<>(newV.getResponses(e)));
        }

        return new DualPairPickerPanel(
                "Contract Endpoints: " + "./src/main/resources/new.yaml", ele0,
                "Prior Contract Endpoints: " + "./src/main/resources/old.yaml", ele1,
                ele2
        );
    }
}