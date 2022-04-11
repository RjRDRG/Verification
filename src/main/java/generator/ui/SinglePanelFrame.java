package generator.ui;

import javax.swing.*;
import java.awt.*;

public class SinglePanelFrame extends JFrame {

    public SinglePanelFrame(String title, JPanel panel, Dimension dimension, int borderPad) {
        super();
        setTitle(title);
        getContentPane().setLayout(new BorderLayout());

        JGridBagPanel top = new JGridBagPanel();
        top.load(0,0, new JSeparator(SwingConstants.HORIZONTAL)).add();
        top.load(0,1, Box.createRigidArea(new Dimension(0,20))).add();
        getContentPane().add(top, BorderLayout.PAGE_START);

        getContentPane().add(Box.createRigidArea(new Dimension(0,borderPad)), BorderLayout.PAGE_END);
        getContentPane().add(Box.createRigidArea(new Dimension(borderPad,0)), BorderLayout.LINE_START);
        getContentPane().add(Box.createRigidArea(new Dimension(borderPad,0)), BorderLayout.LINE_END);
        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(dimension);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}