package generator.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JGridPanel extends JPanel {

    private static class Item {
        JComponent component;
        int column;
        int row;
        int width;
        Insets externalPad;
        int spacing;
        Border border;
        int anchor;
        float weightX;
        float weightY;
        boolean scaleX;
        boolean scaleY;

        public Item(JComponent component, int column, int row) {
            this.component = component;
            this.column = column;
            this.row = row;
            width = 1;
            externalPad = new Insets(0,0,0,0);
            spacing = 5;
            border = BorderFactory.createLineBorder(Color.black);
            anchor = GridBagConstraints.CENTER;
            weightX = 1;
            weightY = 1;
            scaleX = true;
            scaleY = true;
        }
    }

    private Item item;

    public JGridPanel() {
        setLayout(new GridBagLayout());
    }

    public JGridPanel load(int column, int row, JComponent component) {
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

    public JGridPanel setBottomPad(int pad) {
        item.externalPad = new Insets(item.externalPad.top, item.externalPad.left, pad,item.externalPad.right);
        return this;
    }

    public JGridPanel setTopPad(int pad) {
        item.externalPad = new Insets(pad, item.externalPad.left,item.externalPad.bottom,item.externalPad.right);;
        return this;
    }

    public JGridPanel setLeftPad(int pad) {
        item.externalPad = new Insets(item.externalPad.top, pad,item.externalPad.bottom,item.externalPad.right);
        return this;
    }

    public JGridPanel setRightPad(int pad) {
        item.externalPad = new Insets(item.externalPad.top, item.externalPad.left,item.externalPad.bottom,pad);
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

    public JGridPanel setWeight(float x, float y) {
        item.weightX = x;
        item.weightY = y;
        return this;
    }

    public JGridPanel removeScaleX() {
        item.scaleX = false;
        return this;
    }

    public JGridPanel removeScaleY() {
        item.scaleY = false;
        return this;
    }

   public JGridPanel add() {
       GridBagConstraints c = new GridBagConstraints();
       c.gridx = item.column;
       c.gridy = item.row;
       c.gridwidth = item.width;
       c.insets = item.externalPad;
       c.anchor = item.anchor;

       if(item.scaleX && item.scaleY) {
           c.fill = GridBagConstraints.BOTH;
           c.weightx = item.weightX;
           c.weighty = item.weightY;
       }
       else if(item.scaleX) {
           c.fill = GridBagConstraints.HORIZONTAL;
           c.weightx = item.weightX;
           c.weighty = 0;
       }
       else if(item.scaleY) {
           c.fill = GridBagConstraints.VERTICAL;
           c.weightx = 0;
           c.weighty = item.weightY;
       }

       if(item.border != null) {
           item.component.setBorder(item.border);
       }

       add(item.component, c);

       item = null;
       return this;
   }
}
