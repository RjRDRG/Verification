package generator.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ComboBoxRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor
{
    static final String MISSING = "Missing";

    private final String[] e;
    private String selected;

    public ComboBoxRenderer(Set<String> elements)
    {
        List<String> list = new ArrayList<>(elements.size()+1);
        list.add(MISSING);
        list.addAll(elements);
        e = list.toArray(new String[0]);
        selected = MISSING;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
    {
        JComboBox<String> c = new JComboBox<>(e);
        c.setSelectedItem(Objects.requireNonNullElse(value, MISSING));
        selected = (String) value;
        return c;
    }

    @Override
    public Object getCellEditorValue()
    {
        return selected;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JComboBox<String> c = new JComboBox<>(e);
        c.setSelectedItem(Objects.requireNonNullElse(value, MISSING));
        return c;
    }
}

