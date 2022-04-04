package generator.ui;

import javax.swing.*;
import java.awt.*;

public class BColumn {

    private final int border;
    private final int spacing;

    private boolean empty;

    private JPanel p;

    public BColumn(int border, int spacing) {
        this.border = border;
        this.spacing = spacing;
        this.empty = true;
        this.p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.add(Box.createRigidArea(new Dimension(0,border)));
    }

    public BColumn() {
        this(10,5);
    }

    public BColumn add(JComponent component) {
        if(!empty)
            p.add(Box.createRigidArea(new Dimension(0,spacing)));
        else
            empty = false;

        p.add(component);

        return this;
    }

    public JPanel close() {
        p.add(Box.createRigidArea(new Dimension(0,border)));
        return p;
    }
}
