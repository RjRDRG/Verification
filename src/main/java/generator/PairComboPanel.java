package generator;

import contract.OpenApiContract;
import contract.structures.Endpoint;
import generator.ui.JColoredList;
import generator.ui.JGridPanel;
import generator.ui.RoundBorder;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.ParseOptions;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PairComboPanel extends JPanel {

    public Map<String,JComboBox<String>> pairs;

    public PairComboPanel(
            List<String> elementsList0,
            List<String> elementsList1,
            Consumer<Boolean> okConsumer
    ) {
        setLayout(new BorderLayout());

        pairs = new HashMap<>();

        JGridPanel gp0 = new JGridPanel();

        final List<String> elementsList1WithNone = new ArrayList<>(elementsList1.size()+1);
        elementsList1WithNone.add("none");
        elementsList1WithNone.addAll(elementsList1);

        for (int i = 0; i < elementsList0.size(); i++) {
            JLabel la2 = new JLabel(elementsList0.get(i), SwingConstants.CENTER);
            la2.setFont(new Font("Serif", Font.PLAIN, 20));

            JComboBox<String> c0 = new JComboBox<>(elementsList1WithNone.toArray(new String[0]));
            ((JLabel)c0.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            c0.setFont(new Font("Serif", Font.PLAIN, 20));
            c0.setSelectedItem(elementsList0.get(i));
            c0.addActionListener(e -> {
                for(JComboBox<String> jComboBox : pairs.values()) {
                    if(Objects.equals(jComboBox.getSelectedItem(), "none")) {
                        okConsumer.accept(false);
                        return;
                    }
                }
                okConsumer.accept(true);
            });

            gp0.load(0, i, la2).setItemBorder(new RoundBorder(Color.black)).add();
            gp0.load(1, i, c0).add();

            pairs.put(elementsList0.get(i),c0);
        }

        add(gp0);
    }
}

class Test0 extends JFrame {

    public static void main(String[] args) {
        new Test0();
    }

    private Test0() {
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

        PairComboPanel panel = new PairComboPanel(
                newV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toList()),
                oldV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toList()),
                b-> {
                    if(b) System.out.println("ok");
                    else System.out.println("no no");
                }
        );

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //setPreferredSize(new Dimension(600, 600)); pack();
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