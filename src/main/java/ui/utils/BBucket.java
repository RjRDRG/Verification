package ui.utils;

import javax.swing.*;
import java.awt.*;

public class BBucket {

    private final int border;
    private final int spacing;

    private boolean empty;

    private final JPanel r;
    private final JPanel c;

    public BBucket(int border, int spacing) {
        this.border = border;
        this.spacing = spacing;
        this.empty = true;

        this.r = new JPanel();
        r.setLayout(new BoxLayout(r, BoxLayout.LINE_AXIS));
        r.add(Box.createRigidArea(new Dimension(border,0)));

        this.c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
        c.add(Box.createRigidArea(new Dimension(0,border)));
        r.add(c);

        r.add(Box.createRigidArea(new Dimension(border,0)));
    }

    public BBucket() {
        this(10,5);
    }

    public BBucket add(JComponent component) {
        if(!empty)
            c.add(Box.createRigidArea(new Dimension(0,spacing)));
        else
            empty = false;

        c.add(component);

        return this;
    }

    public JPanel close() {
        c.add(Box.createRigidArea(new Dimension(0,border)));
        return r;
    }
}
