package generator.old;

import javax.swing.*;
import java.awt.*;

public class BFill extends JPanel{

    public static JPanel of(JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
