package generator;

import contract.OpenApiContract;
import contract.structures.Endpoint;
import generator.ui.ColoredString;
import generator.ui.JColoredList;
import generator.ui.JGridPanel;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.parser.core.models.ParseOptions;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DualPairPickerPanel extends JPanel {

    private static String DIVIDER = "  <->  ";
    private static String SECOND_DIVIDER = "  |  ";

    public DualPairPickerPanel(
            String title0, final Map<String,Set<String>> elements0,
            String title1, final Map<String,Set<String>> elements1,
            final Map<String,Set<String>> pairs
    ) {
        setLayout(new BorderLayout());
        JGridPanel gp0 = new JGridPanel();
        add(gp0,BorderLayout.CENTER);

        //--------------------------------------------------------------------------------------------------------------

        final JLabel la0 = new JLabel();               final JLabel la1 = new JLabel();
        final JList<String> ls0 = new JList<>();       final JList<String> ls1 = new JList<>();
        final JButton bt0 = new JButton();

        final JColoredList ls2 = new JColoredList();   final ViewerPanel<PairComboPanel> viewerPanel = new ViewerPanel<>();
        JButton bt1 = new JButton();

        //--------------------------------------------------------------------------------------------------------------

        la0.setText(title0);
        la0.setAlignmentX(SwingConstants.CENTER);
        la0.setFont(new Font("Serif", Font.PLAIN, 20));

        gp0.load(0,0, la0).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<String> m0 = new DefaultListModel<>();
        m0.addAll(elements0.keySet());
        ls0.setModel(m0);
        ls0.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if(!elements0.isEmpty()) ls0.setSelectedIndex(0);
        JScrollPane s0 = new JScrollPane(ls0);

        gp0.load(0,1, s0).add();

        //--------------------------------------------------------------------------------------------------------------

        la1.setText(title1);
        la1.setAlignmentX(SwingConstants.CENTER);
        la1.setFont(new Font("Serif", Font.PLAIN, 20));

        gp0.load(1,0, la1).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<String> m1 = new DefaultListModel<>();
        m1.addAll(elements1.keySet());
        ls1.setModel(m1);
        ls1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if(!elements1.isEmpty()) ls1.setSelectedIndex(0);
        JScrollPane s1 = new JScrollPane(ls1);

        gp0.load(1,1, s1).add();

        //--------------------------------------------------------------------------------------------------------------

        bt0.setText("Make Pair");
        bt0.addActionListener(e -> {
            if(!(ls0.getSelectedIndex()>=0 && ls1.getSelectedIndex()>=0))
                return;

            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
            DefaultListModel<ColoredString> lm2 = (DefaultListModel<ColoredString>) ls2.getModel();

            String se0 = ls0.getSelectedValue();
            String se1 = ls1.getSelectedValue();

            lm2.addElement(ColoredString.red(se0 + DIVIDER + se1));
            if(ls2.getSelectedIndex()<0) ls2.setSelectedIndex(0);

            viewerPanel.add(new PairComboPanel(
                new ArrayList<>(elements0.get(se0)),
                new ArrayList<>(elements1.get(se1)),
                b -> {
                    if(b) ls2.getSelectedValue().setGreen();
                    else ls2.getSelectedValue().setRed();
                }
            ));

            lm0.remove(ls0.getSelectedIndex());
            lm1.remove(ls1.getSelectedIndex());
        });

        gp0.load(0,2, bt0).setWidth(2).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<ColoredString> m2 = new DefaultListModel<>();

        for (Map.Entry<String,Set<String>> pair : pairs.entrySet()) {
            m2.addElement(ColoredString.green(pair.getKey() + DIVIDER + pair.getKey()));

            viewerPanel.add(
                    new PairComboPanel(
                            new ArrayList<>(pair.getValue()),
                            new ArrayList<>(pair.getValue()),
                            b -> {
                                if(b) ls2.getSelectedValue().setGreen();
                                else ls2.getSelectedValue().setRed();
                            }
                    )
            );
        }

        ls2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ls2.setModel(m2);
        ls2.addListSelectionListener( e -> {
            viewerPanel.setActive(ls2.getSelectedIndex());
        });

        if(!m2.isEmpty()) ls2.setSelectedIndex(0);
        JScrollPane s2 = new JScrollPane(ls2);

        gp0.load(0,3, s2).add();

        //--------------------------------------------------------------------------------------------------------------

        gp0.load(1,3, viewerPanel).setWeight(0.3f,1.0f).add();

        //--------------------------------------------------------------------------------------------------------------

        bt1.setText("Unpair");
        bt1.addActionListener(e -> {
            if(ls2.getSelectedIndex()<0)
                return;

            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
            DefaultListModel<ColoredString> lm2 = (DefaultListModel<ColoredString>) ls2.getModel();

            String[] parts = ls2.getSelectedValue().string.split(DIVIDER);

            lm0.addElement(parts[0]);
            if(ls0.getSelectedIndex()<0) ls0.setSelectedIndex(0);

            lm1.addElement(parts[1]);
            if(ls1.getSelectedIndex()<0) ls1.setSelectedIndex(0);

            viewerPanel.remove(ls2.getSelectedIndex());
            lm2.remove(ls2.getSelectedIndex());
            if(!lm2.isEmpty()) ls2.setSelectedIndex(0);
        });

        gp0.load(0,4, bt1).setWidth(2).removeScaleY().add();
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

        DualPairPickerPanel panel = new DualPairPickerPanel(
                "Contract Endpoints: " + "./src/main/resources/new.yaml", ele0,
                "Prior Contract Endpoints: " + "./src/main/resources/old.yaml", ele1,
                ele2
        );

        getContentPane().setLayout(new BorderLayout());
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
