package generator.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class RoundBorder extends AbstractBorder {

    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    RenderingHints hints;

    public RoundBorder(Color color) {
        this(color, 1, 8);
    }

    public RoundBorder(Color color, int thickness, int radii) {
        this.thickness = thickness;
        this.radii = radii;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + 1 + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - 4;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                3 + strokePad,
                2 + strokePad,
                width - thickness - 5,
                bottomLineY,
                radii,
                radii);

        Area area = new Area(bubble);

        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
        Component parent  = c.getParent();
        if (parent!=null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0,0,width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}