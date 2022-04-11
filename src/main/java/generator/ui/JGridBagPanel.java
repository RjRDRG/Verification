package generator.ui;

import javax.swing.*;
import java.awt.*;

public class JGridBagPanel extends JPanel {

    private static class Item {
        Component component;
        int column;
        int row;
        int width;
        Insets externalPad;
        int spacing;
        int anchor;
        float weightX;
        float weightY;
        boolean scaleX;
        boolean scaleY;

        public Item(Component component, int column, int row) {
            this.component = component;
            this.column = column;
            this.row = row;
            width = 1;
            externalPad = new Insets(0,0,0,0);
            spacing = 5;
            anchor = GridBagConstraints.CENTER;
            weightX = 1;
            weightY = 1;
            scaleX = true;
            scaleY = true;
        }
    }

    private Item item;

    public JGridBagPanel() {
        setLayout(new GridBagLayout());
    }

    public JGridBagPanel load(int column, int row, Component component) {
        item = new Item(component, column, row);
        return this;
    }

    public JGridBagPanel setWidth(int width) {
        item.width = width;
        return this;
    }

    public JGridBagPanel setPad(Insets externalPad) {
        item.externalPad = externalPad;
        return this;
    }

    public JGridBagPanel setBottomPad(int pad) {
        item.externalPad = new Insets(item.externalPad.top, item.externalPad.left, pad,item.externalPad.right);
        return this;
    }

    public JGridBagPanel setTopPad(int pad) {
        item.externalPad = new Insets(pad, item.externalPad.left,item.externalPad.bottom,item.externalPad.right);;
        return this;
    }

    public JGridBagPanel setLeftPad(int pad) {
        item.externalPad = new Insets(item.externalPad.top, pad,item.externalPad.bottom,item.externalPad.right);
        return this;
    }

    public JGridBagPanel setRightPad(int pad) {
        item.externalPad = new Insets(item.externalPad.top, item.externalPad.left,item.externalPad.bottom,pad);
        return this;
    }

    public JGridBagPanel removePad() {
        item.externalPad = new Insets(0,0,0,0);
        return this;
    }

    public JGridBagPanel setSpacing(int spacing) {
        item.spacing = spacing;
        return this;
    }

    public JGridBagPanel setAnchorLeft() {
        item.anchor = GridBagConstraints.LINE_START;
        return this;
    }

    public JGridBagPanel setAnchorRight() {
        item.anchor = GridBagConstraints.LINE_END;
        return this;
    }

    public JGridBagPanel setWeight(float x, float y) {
        item.weightX = x;
        item.weightY = y;
        return this;
    }

    public JGridBagPanel removeScaleX() {
        item.scaleX = false;
        return this;
    }

    public JGridBagPanel removeScaleY() {
        item.scaleY = false;
        return this;
    }

   public JGridBagPanel add() {
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

       add(item.component, c);

       item = null;
       return this;
   }
}
