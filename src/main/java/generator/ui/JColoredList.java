package generator.ui;

import javax.swing.*;
import java.awt.*;

public class JColoredList extends JList<ColoredString> {

    public JColoredList() {
        super();
        setCellRenderer(new ColoredListCellRenderer());
    }
}

class ColoredListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus
        ) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ColoredString) {
                ColoredString next = (ColoredString) value;
                setText(next.string);
                setForeground(next.color);
            }
            return c;
        }
}
