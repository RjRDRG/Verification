package generator;

import contract.OpenApiContract;
import contract.structures.Endpoint;
import generator.ui.JGridPanel;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PairPickerPanel extends JPanel {

    public PairPickerPanel(String title0, List<String> elementsList0, String title1, List<String> elementsList1, List<String> pairList) {
        setLayout(new BorderLayout());

        JGridPanel gp0 = new JGridPanel();

        JLabel la0 = new JLabel(title0);

        gp0.load(0,0, la0).removeScaleY().removeItemBorder().add();

        JLabel la1 = new JLabel(title1);

        gp0.load(1,0, la1).removeScaleY().add();

        final JList<String> ls0 = new JList<>();
        DefaultListModel<String> m0 = new DefaultListModel<>();
        m0.addAll(elementsList0);
        ls0.setModel(m0);
        ls0.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane s0 = new JScrollPane(ls0);

        gp0.load(0,1, s0).removeItemBorder().add();

        final JList<String> ls1 = new JList<>();
        DefaultListModel<String> m1 = new DefaultListModel<>();
        m1.addAll(elementsList1);
        ls1.setModel(m1);
        ls1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane s1 = new JScrollPane(ls1);

        gp0.load(1,1, s1).removeItemBorder().add();

        final JList<String> ls2 = new JList<>();
        DefaultListModel<String> m2 = new DefaultListModel<>();
        m2.addAll(pairList);
        ls2.setModel(m2);
        JScrollPane s2 = new JScrollPane(ls2);

        JButton bt0 = new JButton("Make Pair");
        bt0.addActionListener(e -> {
            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
            DefaultListModel<String> lm2 = (DefaultListModel<String>) ls2.getModel();

            if(ls0.getSelectedIndex()>=0 && ls1.getSelectedIndex()>=0) {
                lm2.add(lm2.size(), ls0.getSelectedValue() + "  <->  " + ls1.getSelectedValue());
                lm0.remove(ls0.getSelectedIndex());
                lm1.remove(ls1.getSelectedIndex());
            }
        });

        gp0.load(0,2, bt0).setWidth(2).removeScaleY().add();

        JLabel la2 = new JLabel("Pairs");

        gp0.load(0,3, la2).setWidth(2).removeItemBorder().removeScaleY().add();

        gp0.load(0,4, s2).setWidth(2).add();

        JButton bt1 = new JButton("Remove");

        gp0.load(0,5, bt1).setWidth(2).removeScaleY().add();

        add(gp0);
    }
}

class Test extends JFrame {

    public static void main(String[] args) {
        new Test();
    }

    private Test() {
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

        PairPickerPanel panel = new PairPickerPanel(
                "Contract", newV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toList()),
                "Prior Contract", oldV.getEndpoints().stream().map(Endpoint::toString).collect(Collectors.toList()),
                Collections.emptyList()
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
