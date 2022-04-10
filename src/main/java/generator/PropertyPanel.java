package generator;

import contract.structures.Endpoint;
import generator.ui.JGridPanel;
import generator.ui.JViewerPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PropertyPanel extends JPanel {

    public PropertyPanel(String[][] data, String[] dataHeader) {
        setLayout(new BorderLayout());
        JGridPanel gp0 = new JGridPanel();
        add(gp0,BorderLayout.CENTER);

        //--------------------------------------------------------------------------------------------------------------

        final JMessageTable t0 = new JMessageTable(data, dataHeader);
        final JViewerPanel<JTreePanel> v0 = new JViewerPanel<>(t0.getColumnCount()-2);
        final JPropertyPanel v1 = new JPropertyPanel();

        //--------------------------------------------------------------------------------------------------------------

        t0.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            int c = t0.getSelectedColumn();
            int r = t0.getSelectedRow();

            if(c<2 || t0.getValueAt(r, c) == null)
                return;

            v0.setActive(r, c-2);
        });

        gp0.load(0,0, t0).add();

        //--------------------------------------------------------------------------------------------------------------

        for(int j=0; j<t0.getRowCount(); j++) {
            for (int i = 2; i < t0.getColumnCount(); i++) {
                Endpoint endpoint = Endpoint.fromString((String) t0.getValueAt(j,0));
                Endpoint priorEndpoint = Endpoint.fromString((String) t0.getValueAt(j,1));
                if(t0.getValueAt(j, i) != null) {
                    String message = t0.getColumnName(i);
                    String priorMessage = (String) t0.getValueAt(j,i);
                    v0.addPanel(
                            j, i - 2,
                            new JTreePanel(endpoint, message, priorEndpoint, priorMessage)
                    );
                }
            }
        }

        gp0.load(1,0, v0).add();

        //--------------------------------------------------------------------------------------------------------------

        gp0.load(0,1, v1).setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------
    }

}

class JMessageTable extends JTable {

    public JMessageTable(String[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        setModel(model);

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setCellSelectionEnabled(true);
        setFocusable(true);
        setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment( SwingConstants.LEFT );
        getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
    }
}

class JTreePanel extends JPanel {

    public JTreePanel(
            Endpoint endpoint, String message,
            Endpoint priorEndpoint, String priorMessage
    ) {
        setLayout(new BorderLayout());
        //JTreePanel t0 = new JTreePanel();
        //add(t0,BorderLayout.CENTER);
    }
}

class JPropertyPanel extends JPanel {

    public JPropertyPanel() {
        setLayout(new BorderLayout());
        JGridPanel gp0 = new JGridPanel();
        add(gp0,BorderLayout.CENTER);
    }
}
