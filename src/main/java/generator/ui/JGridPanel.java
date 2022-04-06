package generator.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JGridPanel extends JPanel {

    private static class Item {
        Component[] components;
        int column;
        int row;
        int width;
        Insets externalPad;
        int spacing;
        Border border;
        int anchor;
        boolean isVertical;
        float weightX;
        float weightY;

        public Item(Component[] components, int column, int row) {
            this.components = components;
            this.column = column;
            this.row = row;
            width = 1;
            externalPad = new Insets(5,5,5,5);
            spacing = 5;
            border = BorderFactory.createLineBorder(Color.black);
            anchor = GridBagConstraints.CENTER;
            isVertical = true;
            weightX = 0;
            weightY = 0;
        }
    }

    private Item item;

    public JGridPanel() {
        setLayout(new GridBagLayout());
    }

    public JGridPanel load(int column, int row, Component... component) {
        item = new Item(component, column, row);
        return this;
    }

    public JGridPanel setWidth(int width) {
        item.width = width;
        return this;
    }

    public JGridPanel setPad(Insets externalPad) {
        item.externalPad = externalPad;
        return this;
    }

    public JGridPanel removePad() {
        item.externalPad = new Insets(0,0,0,0);
        return this;
    }

    public JGridPanel setSpacing(int spacing) {
        item.spacing = spacing;
        return this;
    }

    public JGridPanel setItemBorder(Border border) {
        item.border = border;
        return this;
    }

    public JGridPanel removeItemBorder() {
        item.border = null;
        return this;
    }

    public JGridPanel setAnchorLeft() {
        item.anchor = GridBagConstraints.LINE_START;
        return this;
    }

    public JGridPanel setAnchorRight() {
        item.anchor = GridBagConstraints.LINE_END;
        return this;
    }

    public JGridPanel setHorizontal() {
        item.isVertical = false;
        return this;
    }

    public JGridPanel setWeight(float x, float y) {
        item.weightX = x;
        item.weightY = y;
        return this;
    }

   public JGridPanel add() {
       GridBagConstraints c = new GridBagConstraints();
       c.gridx = item.column;
       c.gridy = item.row;
       c.gridwidth = item.width;
       c.insets = item.externalPad;
       c.anchor = item.anchor;
       c.weightx = item.weightX;
       c.weighty = item.weightY;

       JPanel panel = new JPanel();
       panel.setLayout(new BoxLayout(panel, item.isVertical ? BoxLayout.PAGE_AXIS : BoxLayout.LINE_AXIS));

       for(int i=0; i<item.components.length; i++) {
           panel.add(item.components[i]);
           if(i<item.components.length-1)
               panel.add(Box.createRigidArea(item.isVertical ? new Dimension(0,item.spacing) : new Dimension(item.spacing,0)));
       }

       if(item.border != null) {
           panel.setBorder(item.border);
           add(panel, c);
       }

       add(panel, c);

       item = null;
       return this;
   }
}
