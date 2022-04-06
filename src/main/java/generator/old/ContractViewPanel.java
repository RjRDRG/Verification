package generator.old;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ContractViewPanel extends JPanel {

    JComponent main;

    String filePath = "src/main/resources/new.yaml";

    public ContractViewPanel() {
        setLayout(new BorderLayout());
        contractViewer();
    }

    public void filePicker() {

    }

    public void contractViewer() {
        if(main != null) remove(main);

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
            textArea.setCodeFoldingEnabled(false);
            textArea.read(br,null);
            textArea.setEditable(false);

            try {
                Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
                theme.apply(textArea);
            } catch (IOException ioe) { // Never happens
                ioe.printStackTrace();
            }

            main = new RTextScrollPane(textArea);
            add(main, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        ContractViewPanel panel = new ContractViewPanel();

        jFrame.getContentPane().setLayout(new BorderLayout());
        jFrame.getContentPane().add(panel, BorderLayout.CENTER);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //setPreferredSize(new Dimension(600, 600)); pack();
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
