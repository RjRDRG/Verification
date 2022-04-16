package generator;

import generator.ui.JGridBagPanel;
import generator.ui.JMultiTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class EndpointPanel extends JPanel {

    final JLabel la0;               final JLabel la1;
    final JList<String> ls0;       final JList<String> ls1;
    final JButton bt0;

    final JMultiTable t0;

    public JButton next;

    public EndpointPanel(
            String title0, final Map<String,Set<String>> elements0,
            String title1, final Map<String,Set<String>> elements1,
            final Map<String,Set<String>> pairs
    ) {
        setLayout(new BorderLayout());
        JGridBagPanel gp0 = new JGridBagPanel();
        add(gp0,BorderLayout.CENTER);

        //--------------------------------------------------------------------------------------------------------------

        la0 = new JLabel();
        la1 = new JLabel();

        ls0 = new JList<>();
        ls1 = new JList<>();

        bt0 = new JButton();

        t0 = new JMultiTable();

        next = new JButton();

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

        bt0.setText("Pair Endpoints");
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
                .map(r -> "Response : " + r)
                .collect(Collectors.toList());

        String[] columnNames = List.of(
                List.of("Endpoint", "Prior Endpoint", "Request"),
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

        t0.buildTable(columnNames, Set.of(0,1,2), removeRow);

        for(Map.Entry<String,Set<String>> entry : pairs.entrySet()) {
            buildRow(t0, entry.getKey(), entry.getKey(), entry.getValue(), entry.getValue());
        }

        JScrollPane s2 = new JScrollPane(t0);

        gp0.load(0,3, s2).setTopPad(10).setWidth(2).add();

        next.setText("Next");
        next.setFocusable(false);

        gp0.load(1,4, next).removeScaleY().removeScaleX().setAnchorRight().setTopPad(5).add();

        //--------------------------------------------------------------------------------------------------------------


    }

    private void buildRow(JMultiTable table, String endpoint, String previousEndpoint, Set<String> responses, Set<String> responseOptions) {
        String[] row = new String[table.getColumnCount()];
        row[0] = endpoint;
        row[1] = previousEndpoint;
        row[2] = "Request";

        for(int i=3; i<table.getColumnCount()-1; i++) {
            String columnName = table.getColumnName(i).replace("Response : ","");
            if(responses.contains(columnName)) {
                if(responseOptions.contains(columnName))
                    row[i] = columnName;
                else
                    row[i] = "MISSING";
            }
            else
                row[i] = JMultiTable.BLANK;
        }

        row[table.getColumnCount()-1]="X";

        Set<Integer> columnsWithOptions = new HashSet<>();
        for(int i=3; i<table.getColumnCount()-1; i++) {
            if(responses.contains(table.getColumnName(i).replace("Response : ","")))
                columnsWithOptions.add(i);
        }
        table.addRow(row, responseOptions, columnsWithOptions);
    }

    public ResolutionPanel getNextPanel() {
        return new ResolutionPanel(t0.getValues(), t0.getColumnNames());
    }
}
