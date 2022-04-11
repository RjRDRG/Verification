package generator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JMultiTable extends JTable {

    public static final String BLANK = "-";
    public static final String MISSING = "MISSING";

    private final MultiTableModel model;

    private final MissingColumnCellRenderer missingCellRenderer;

    private Set<Integer> nonEditableColumns;

    public JMultiTable() {
        super();
        model = new MultiTableModel();

        missingCellRenderer = new MissingColumnCellRenderer();
        missingCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return !nonEditableColumns.contains(column) && !model.getValueAt(row,column).equals(BLANK);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        String value = (String) model.getValueAt(row,column);
        if (value.equals(MISSING))
            return missingCellRenderer;
        else
            return super.getCellRenderer(row,column);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        String[] options = model.getOptions(row, column);
        if (options != null) {
            JComboBox<String> comboBox = new JComboBox<>(options);
            comboBox.setFocusable(false);
            comboBox.addActionListener((e)-> {
                    model.setValueAt(comboBox.getSelectedItem(), row, column);
            });
            return new ComboBoxCellEditor(comboBox);
        } else {
            return super.getCellEditor(row, column);
        }
    }

    public void buildTable(String[] columnNames, Set<Integer> nonEditableColumns, Action additionalRemoveRowAction) {
        this.nonEditableColumns = nonEditableColumns;
        model.setColumns(columnNames);
        setModel(model);

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setCellSelectionEnabled(false);
        setFocusable(false);
        setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment( SwingConstants.LEFT );
        getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

        Action removeRow = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                int modelRow = Integer.parseInt( e.getActionCommand() );
                model.removeRow(modelRow);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(this, model.getColumnCount()-1, new Color(199, 84, 80), additionalRemoveRowAction, removeRow);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        getColumnModel().getColumn( model.getColumnCount()-1).setMaxWidth(30);
        getColumnModel().getColumn( model.getColumnCount()-1).setMinWidth(30);
    }

    public void addRow(String[] row, Set<String> options, Set<Integer> columns) {
        model.addRow(row,options,columns);
    }

    public String[][] getValues() {
        String[][] values = new String[model.getRowCount()][model.getColumnCount()-1];
        for(int i=0; i<model.getRowCount(); i++) {
            for(int j=0; j<model.getColumnCount()-1; j++) {
                String mVal = (String) model.getValueAt(i,j);
                values[i][j] = mVal.equals(BLANK) ? null : mVal;
            }
        }
        return values;
    }

    public String[] getColumnNames() {
        String[] c = new String[model.getColumnCount()-1];
        for(int i=0; i<model.getColumnCount()-1; i++) {
            c[i] = model.getColumnName(i);
        }
        return c;
    }
}

class MultiTableModel extends DefaultTableModel {

    java.util.List<Set<String>> rowOptions;
    List<Set<Integer>> columnsWithOptions;

    public MultiTableModel() {
        super();
        rowOptions = new ArrayList<>();
        columnsWithOptions = new ArrayList<>();
    }

    public void setColumns(String[] columnNames) {
        setColumnIdentifiers(columnNames);
    }

    public void addRow(String[] row, Set<String> options, Set<Integer> columns) {
        super.addRow(row);
        rowOptions.add(options);
        columnsWithOptions.add(columns);
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
        rowOptions.remove(row);
        columnsWithOptions.remove(row);
    }

    public String[] getOptions(int row, int column) {
        if (columnsWithOptions.get(row).contains(column)) {
            return rowOptions.get(row).toArray(new String[0]);
        }
        else {
            return null;
        }
    }
}

class MissingColumnCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        l.setForeground(new Color(199, 84, 80));
        Font f = l.getFont();
        l.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        return l;
    }
}
