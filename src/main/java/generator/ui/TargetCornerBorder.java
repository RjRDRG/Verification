package generator.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Line2D;

public class TargetCornerBorder extends AbstractBorder {
    private Color borderColour;
    private int gap;

    public TargetCornerBorder(Color colour, int g)
    {
        borderColour = colour;
        gap = g;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y
            , int width
            , int height)
    {
        super.paintBorder(c, g, x, y, width, height);
        Graphics2D g2d = null;
        if (g instanceof Graphics2D)
        {
            g2d = (Graphics2D) g;
            g2d.setColor(borderColour);
            //Left Border
            g2d.draw(new Line2D.Double((double)x + 10, (double)y + 10
                    , (double)x + 10, (double)y + 20));
            g2d.draw(new Line2D.Double( (double)x + 10, (double)y + 10
                    , (double)x + 20, (double)y + 10));
            // Right Border
            g2d.draw(new Line2D.Double( (double)width - 10, (double)y + 10
                    , (double)width - 10, (double)y + 20));
            g2d.draw(new Line2D.Double( (double)width - 10, (double)y + 10
                    , (double)width - 20, (double)y + 10));
            // Lower Left Border
            g2d.draw(new Line2D.Double( (double)x + 10, (double)height - 10
                    , (double)x + 20, (double)height - 10));
            g2d.draw(new Line2D.Double( (double)x + 10, (double)height - 10
                    , (double)x + 10, (double)height - 20));
            // Lower Right Border
            g2d.draw(new Line2D.Double( (double)width - 10, (double)height - 10
                    , (double)width - 20, (double)height - 10));
            g2d.draw(new Line2D.Double( (double)width - 10, (double)height - 10
                    , (double)width - 10, (double)height - 20));
        }
    }

    @Override
    public Insets getBorderInsets(Component c)
    {
        return (getBorderInsets(c, new Insets(gap, gap, gap, gap)));
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets)
    {
        insets.left = insets.top = insets.right = insets.bottom = gap;
        return insets;
    }

    @Override
    public boolean isBorderOpaque()
    {
        return true;
    }
}
