package generator.old;

import javax.swing.*;
import java.awt.*;

public class BRow extends JPanel implements BFrame {

    private final int border;
    private final int spacing;

    private boolean empty;

    public BRow(int border, int spacing) {
        super();
        this.border = border;
        this.spacing = spacing;
        this.empty = true;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(border,0)));
    }

    public BRow() {
        this(10,5);
    }

    public BRow add(JComponent component) {
        if(!empty)
            add(Box.createRigidArea(new Dimension(spacing,0)));
        else
            empty = false;

        add(component);

        return this;
    }

    public JPanel close() {
        add(Box.createRigidArea(new Dimension(border,0)));
        return this;
    }
}
