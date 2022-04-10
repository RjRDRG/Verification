package generator;

import generator.ui.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PairPickerPanel extends JPanel {

    public PairPickerPanel(
            String title0, final Map<String,Set<String>> elements0,
            String title1, final Map<String,Set<String>> elements1,
            final Map<String,Set<String>> pairs
    ) {
        setLayout(new BorderLayout());
        JGridPanel gp0 = new JGridPanel();
        add(gp0,BorderLayout.CENTER);

        //--------------------------------------------------------------------------------------------------------------

        final JLabel la0 = new JLabel();               final JLabel la1 = new JLabel();
        final JList<String> ls0 = new JList<>();       final JList<String> ls1 = new JList<>();
        final JButton bt0 = new JButton();

        final PairTable t0 = new PairTable();

        //--------------------------------------------------------------------------------------------------------------

        la0.setText(title0);
        la0.setAlignmentX(SwingConstants.CENTER);

        gp0.load(0,0, la0).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<String> m0 = new DefaultListModel<>();
        m0.addAll(elements0.keySet());
        ls0.setModel(m0);
        ls0.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ls0.setFocusable(false);
        JScrollPane s0 = new JScrollPane(ls0);

        gp0.load(0,1, s0).add();

        //--------------------------------------------------------------------------------------------------------------

        la1.setText(title1);
        la1.setAlignmentX(SwingConstants.CENTER);

        gp0.load(1,0, la1).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<String> m1 = new DefaultListModel<>();
        m1.addAll(elements1.keySet());
        ls1.setModel(m1);
        ls1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ls1.setFocusable(false);
        JScrollPane s1 = new JScrollPane(ls1);

        gp0.load(1,1, s1).add();

        //--------------------------------------------------------------------------------------------------------------

        bt0.setText("Make Pair");
        bt0.setFocusable(false);
        bt0.addActionListener(e -> {
            if(!(ls0.getSelectedIndex()>=0 && ls1.getSelectedIndex()>=0))
                return;

            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();

            String endpoint = ls0.getSelectedValue();
            String priorEndpoint = ls1.getSelectedValue();
            Set<String> responses = Optional.ofNullable(elements0.get(endpoint)).orElse(pairs.get(endpoint));
            Set<String> responseOptions = Optional.ofNullable(elements1.get(priorEndpoint)).orElse(pairs.get(priorEndpoint));

            buildRow(t0,endpoint,priorEndpoint,responses,responseOptions);

            lm0.remove(ls0.getSelectedIndex());
            lm1.remove(ls1.getSelectedIndex());
        });

        gp0.load(0,2, bt0).setWidth(2).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        List<String> responses = List.of(elements0.values(), elements1.values(), pairs.values()).stream()
                .flatMap(Collection::stream)
                .flatMap(Set::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        String[] columnNames = List.of(
                List.of("Endpoint", "Prior Endpoint"),
                responses,
                List.of("")
        ).stream().flatMap(Collection::stream).toArray(String[]::new);

        Action removeRow = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                int row = Integer.parseInt( e.getActionCommand() );
                ((DefaultListModel<String>) ls0.getModel()).addElement((String) t0.getModel().getValueAt(row,0));
                ((DefaultListModel<String>) ls1.getModel()).addElement((String) t0.getModel().getValueAt(row,1));
            }
        };

        t0.buildTable(columnNames, new HashSet<>(responses), removeRow);

        for(Map.Entry<String,Set<String>> entry : pairs.entrySet()) {
            buildRow(t0, entry.getKey(), entry.getKey(), entry.getValue(), entry.getValue());
        }

        JScrollPane s2 = new JScrollPane(t0);

        gp0.load(0,3, s2).setTopPad(10).setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------


    }

    private void buildRow(PairTable table, String endpoint, String previousEndpoint, Set<String> responses, Set<String> responseOptions) {
        String[] row = new String[table.getColumnCount()];
        row[0] = endpoint;
        row[1] = previousEndpoint;

        for(int i=2; i<table.getColumnCount()-1; i++) {
            String columnName = table.getColumnName(i);
            if(responses.contains(columnName)) {
                if(responseOptions.contains(columnName))
                    row[i] = columnName;
            }
        }

        row[table.getColumnCount()-1]="X";

        table.getModel().addRow(row,responseOptions);
    }
}

class PairTable extends JTable {

    private final MultiTableModel model;

    PairTable() {
        super();
        model = new MultiTableModel();
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        String[] options = model.getOptions(row, column);
        if (options != null) {
            JComboBox<String> comboBox = new JComboBox<>(options);
            comboBox.addActionListener((e)-> model.setValueAt(comboBox.getSelectedItem(), row, column));
            return new DefaultCellEditor(comboBox);
        } else {
            return super.getCellEditor(row, column);
        }
    }

    void buildTable(String[] columnNames, Set<String> columnWithOptions, Action additionalRemoveRowAction) {
        model.setColumns(columnNames,columnWithOptions);
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

    public MultiTableModel getModel() {
        return model;
    }
}

class MultiTableModel extends DefaultTableModel {

    Set<String> columnWithOptions;
    List<Set<String>> rowOptions;

    MultiTableModel() {
        super();
        rowOptions = new ArrayList<>();
    }

    public void setColumns(String[] columnNames, Set<String> columnWithOptions) {
        setColumnIdentifiers(columnNames);
        this.columnWithOptions = columnWithOptions;
    }

    public void addRow(String[] row, Set<String> options) {
        super.addRow(row);
        rowOptions.add(options);
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
        rowOptions.remove(row);
    }

    public String[] getOptions(int row, int column) {
        if (columnWithOptions.contains(getColumnName(column))) {
            return rowOptions.get(row).toArray(new String[0]);
        }
        else {
            return null;
        }
    }
}
