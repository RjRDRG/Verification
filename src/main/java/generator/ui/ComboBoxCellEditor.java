package generator.ui;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ComboBoxCellEditor extends DefaultCellEditor {

    public ComboBoxCellEditor(JComboBox comboBox) {
        super(comboBox);
        comboBox.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
                cancelCellEditing();
            }
        });
    }
}
