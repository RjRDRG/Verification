package generator.old;

import javax.swing.*;
import java.awt.*;

public class BColumn extends JPanel implements BFrame {

    private final int border;
    private final int spacing;

    private boolean empty;

    public BColumn(int border, int spacing) {
        super();
        this.border = border;
        this.spacing = spacing;
        this.empty = true;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(Box.createRigidArea(new Dimension(0,border)));
    }

    public BColumn() {
        this(10,5);
    }

    public BColumn add(JComponent component) {
        if(!empty)
            add(Box.createRigidArea(new Dimension(0,spacing)));
        else
            empty = false;

        add(component);

        return this;
    }

    public JPanel close() {
        add(Box.createRigidArea(new Dimension(0,border)));
        return this;
    }
}
