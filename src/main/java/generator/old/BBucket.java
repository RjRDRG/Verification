package generator.old;

import javax.swing.*;
import java.awt.*;

public class BBucket extends JPanel implements BFrame {

    private final int border;
    private final int spacing;

    private boolean empty;

    private final JPanel c;

    public BBucket(int border, int spacing) {
        super();
        this.border = border;
        this.spacing = spacing;
        this.empty = true;

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(border,0)));

        this.c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
        c.add(Box.createRigidArea(new Dimension(0,border)));
        add(c);

        add(Box.createRigidArea(new Dimension(border,0)));
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
        return this;
    }
}
