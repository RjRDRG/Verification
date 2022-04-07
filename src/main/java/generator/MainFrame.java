package generator;

import contract.OpenApiContract;
import contract.structures.Endpoint;
import generator.old.ContractViewPanel;
import generator.old.BBucket;
import generator.utils.TriConsumer;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.ParseOptions;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
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

        TriConsumer<JList<String>> pair = (ls0,ls1,ls2) -> {
            if(!(ls0.getSelectedIndex()>=0 && ls1.getSelectedIndex()>=0))
                return;

            PairComboPanel pairComboPanel = new PairComboPanel(
                    "Endpoint Responses", new ArrayList<>(newV.getResponses(Endpoint.fromString(ls0.getSelectedValue()))),
                    "Prior Endpoint Responses", new ArrayList<>(oldV.getResponses(Endpoint.fromString(ls1.getSelectedValue())))
            );

            SinglePanelFrame frame = new SinglePanelFrame(pairComboPanel,new Dimension(500,500),30);

            pairComboPanel.submit.addActionListener(e -> {
                DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
                DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
                DefaultListModel<String> lm2 = (DefaultListModel<String>) ls2.getModel();

                lm2.add(lm2.size(), ls0.getSelectedValue() + "  <->  " + ls1.getSelectedValue());
                if(ls2.getSelectedIndex()<0) ls2.setSelectedIndex(0);

                lm0.remove(ls0.getSelectedIndex());
                if(!lm0.isEmpty()) ls0.setSelectedIndex(0);
                lm1.remove(ls1.getSelectedIndex());
                if(!lm1.isEmpty()) ls1.setSelectedIndex(0);

                frame.setVisible(false);
                frame.dispose();
            });
        };

        TriConsumer<JList<String>> unpair = (ls0,ls1,ls2) -> {
            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
            DefaultListModel<String> lm2 = (DefaultListModel<String>) ls2.getModel();

            if(ls2.getSelectedIndex()>=0) {
                String[] parts = ls2.getSelectedValue().split(" <-> ");

                lm0.add(lm0.size(), parts[0]);
                if(ls0.getSelectedIndex()<0) ls0.setSelectedIndex(0);

                lm1.add(lm1.size(), parts[1]);
                if(ls1.getSelectedIndex()<0) ls1.setSelectedIndex(0);

                lm2.remove(ls2.getSelectedIndex());
                if(!lm2.isEmpty()) ls2.setSelectedIndex(0);
            }
        };

        PairPickerPanel panel = new PairPickerPanel(pair, unpair,
                "Contract Endpoints: " + "./src/main/resources/new.yaml", newV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toList()),
                "Prior Contract Endpoints: " + "./src/main/resources/old.yaml", oldV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toList()),
                Collections.emptyList()
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