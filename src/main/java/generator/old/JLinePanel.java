package generator.old;

import javax.swing.*;
import java.awt.*;

public class JLinePanel extends JPanel {

    private final int vBorder;
    private final int hBorder;
    private final int spacing;
    private final boolean isColumn;

    private boolean empty;

    private final JPanel inner;

    private JLinePanel(int vBorder, int hBorder, int spacing, boolean isColumn) {
        super();
        this.vBorder = vBorder;
        this.hBorder = hBorder;
        this.spacing = spacing;
        this.isColumn = isColumn;
        this.empty = true;

        setLayout(new BoxLayout(this, isColumn ? BoxLayout.LINE_AXIS : BoxLayout.PAGE_AXIS));
        add(Box.createRigidArea(getOuterDimension()));

        this.inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, isColumn ? BoxLayout.PAGE_AXIS : BoxLayout.LINE_AXIS));
        inner.add(Box.createRigidArea(getInnerDimension()));
        super.add(inner);

        add(Box.createRigidArea(getOuterDimension()));
    }

    public static JLinePanel row(int vBorder, int hBorder, int spacing) {
        return new JLinePanel(vBorder, hBorder, spacing, false);
    }

    public static JLinePanel column(int vBorder, int hBorder, int spacing) {
        return new JLinePanel(vBorder, hBorder, spacing, true);
    }

    @Override
    public JLinePanel add(Component component) {
        if(!empty)
            inner.add(Box.createRigidArea(getSpacingDimension()));
        else
            empty = false;

        inner.add(component);
        return this;
    }

    public JLinePanel close() {
        inner.add(Box.createRigidArea(getInnerDimension()));
        return this;
    }

    public Dimension getSpacingDimension() {
        if(isColumn)
            return new Dimension(0,spacing);
        else
            return new Dimension(spacing, 0);
    }

    public Dimension getInnerDimension() {
        if(isColumn)
            return new Dimension(0,vBorder);
        else
            return new Dimension(hBorder, 0);
    }

    public Dimension getOuterDimension() {
        if(isColumn)
            return new Dimension(hBorder,0);
        else
            return new Dimension(0, vBorder);
    }
}
