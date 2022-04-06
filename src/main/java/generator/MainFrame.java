package generator;

import generator.old.ContractViewPanel;
import generator.old.BBucket;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        setNimbusStyle();

        JComponent main = new BBucket(20,0).add(getMainComponentSimple()).close();

        setTitle("Contract Evolution Spec");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(main, BorderLayout.CENTER);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //setPreferredSize(new Dimension(600, 600)); pack();
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JComponent getMainComponentSimple() {
        JSplitPane s0 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new ContractViewPanel(), new ContractViewPanel());
        s0.setOneTouchExpandable(true);
        s0.setResizeWeight(0.5);

        JSplitPane s1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new EditorPanel(), new ContractViewPanel());
        s1.setOneTouchExpandable(true);
        s1.setResizeWeight(0.2);

        s1.setBorder(BorderFactory.createLineBorder(new Color(63, 65, 70)));

        return s1;
    }

    private JComponent getMainComponent() {
        EditorPanel editorPanel = new EditorPanel();
        JPanel diffPanel = new JPanel();

        JSplitPane s0 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPanel, diffPanel);
        s0.setOneTouchExpandable(true);
        s0.setResizeWeight(0.2);

        s0.setBorder(BorderFactory.createLineBorder(new Color(63, 65, 70)));

        return s0;
    }

    private void setNimbusStyle() {
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

    public static void main(String[] args) {
        new MainFrame();
    }
}

