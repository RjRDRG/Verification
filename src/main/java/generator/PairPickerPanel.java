package generator;

import generator.ui.ButtonColumn;
import generator.ui.ColoredString;
import generator.ui.JColoredList;
import generator.ui.JGridPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

            String se0 = ls0.getSelectedValue();
            String se1 = ls1.getSelectedValue();



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

        Object[][] data = new Object[pairs.size()][columnNames.length];
        int i=0;
        for(Map.Entry<String,Set<String>> endpoint : pairs.entrySet()) {
            data[i][0] = endpoint.getKey();
            data[i][1] = endpoint.getKey();
            int j=2;
            for(String response : responses) {
                if(endpoint.getValue().contains(response)) {
                    data[i][j]=response;
                }
                else {
                    data[i][j]="-";
                }
                j++;
            }
            data[i][columnNames.length-1] = "X";
            i++;
        }

        t0.setModel(new DefaultTableModel(data,columnNames));
        JScrollPane s2 = new JScrollPane(t0);

        gp0.load(0,3, s2).setTopPad(10).setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------


    }
}

class PairTable extends JTable {
    PairTable() {
        super();
    }

    void setModel(DefaultTableModel m) {
        super.setModel(m);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment( SwingConstants.LEFT );
        getColumnModel().getColumn(0).setCellRenderer( leftRenderer );
        getColumnModel().getColumn(1).setCellRenderer( leftRenderer );

        Action delete = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
                ((DefaultTableModel)table.getModel()).removeRow(modelRow);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(this, delete, m.getColumnCount()-1, new Color(199, 84, 80));
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setCellSelectionEnabled(false);
        setFocusable(false);
        setShowGrid(true);

        getColumnModel().getColumn( m.getColumnCount()-1).setMaxWidth(30);
        getColumnModel().getColumn( m.getColumnCount()-1).setMinWidth(30);
    }
}
