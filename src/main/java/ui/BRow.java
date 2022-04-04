package ui;

import javax.swing.*;
import java.awt.*;

public class BRow {

    private final int border;
    private final int spacing;

    private boolean empty;

    private JPanel p;

    public BRow(int border, int spacing) {
        this.border = border;
        this.spacing = spacing;
        this.empty = true;
        this.p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.add(Box.createRigidArea(new Dimension(border,0)));
    }

    public BRow() {
        this(10,5);
    }

    public BRow add(JComponent component) {
        if(!empty)
            p.add(Box.createRigidArea(new Dimension(spacing,0)));
        else
            empty = false;

        p.add(component);

        return this;
    }

    public JPanel close() {
        p.add(Box.createRigidArea(new Dimension(border,0)));
        return p;
    }
}
