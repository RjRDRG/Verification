package generator;

import contract.IContract;
import contract.structures.Endpoint;
import contract.structures.Property;
import contract.structures.PropertyKey;
import generator.ui.JGridBagPanel;
import generator.ui.JViewerPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.*;

public class PropertyPanel extends JPanel {

    public PropertyPanel(String[][] data, String[] dataHeader) {
        setLayout(new BorderLayout());
        JGridBagPanel gp0 = new JGridBagPanel();

        //--------------------------------------------------------------------------------------------------------------

        final JMessageTable t0 = new JMessageTable(data, dataHeader);
        final JViewerPanel<JTreePanel> v0 = new JViewerPanel<>(t0.getColumnCount()-2);
        final JViewerPanel<JTreePanel> v1 = new JViewerPanel<>(t0.getColumnCount()-2);
        final JPropertyPanel v2 = new JPropertyPanel();

        //--------------------------------------------------------------------------------------------------------------

        ListSelectionListener selectionListener = e -> {
            if(e.getValueIsAdjusting())return;

            int c = t0.getSelectedColumn();
            int r = t0.getSelectedRow();

            if(c<2 || t0.getValueAt(r, c) == null) {
                v0.setActiveEmpty();
                v1.setActiveEmpty();
            }
            else {
                v0.setActive(r, c - 2);
                v1.setActive(r, c - 2);
            }
        };

        t0.getSelectionModel().addListSelectionListener(selectionListener);
        t0.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);

        JGridBagPanel gp1 = new JGridBagPanel();
        gp1.load(0,0, t0.getTableHeader()).removeScaleY().add();
        gp1.load(0,1, t0).removeScaleY().add();
        gp1.setBorder(BorderFactory.createLineBorder(new Color(97, 99, 101)));

        gp0.load(0,0, gp1).removeScaleY().setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------

        for(int j=0; j<t0.getRowCount(); j++) {
            for (int i = 2; i < t0.getColumnCount(); i++) {
                Endpoint endpoint = Endpoint.fromString((String) t0.getValueAt(j,0));
                if(t0.getValueAt(j, i) != null) {
                    String message = t0.getColumnName(i).replace("Response : ","");
                    v0.addPanel(
                            j, i - 2,
                            new JTreePanel(endpoint, message, MainFrame.contract)
                    );
                }
            }
        }

        gp0.load(0,1, v0).add();

        //--------------------------------------------------------------------------------------------------------------

        for(int j=0; j<t0.getRowCount(); j++) {
            for (int i = 2; i < t0.getColumnCount(); i++) {
                Endpoint priorEndpoint = Endpoint.fromString((String) t0.getValueAt(j,1));
                if(t0.getValueAt(j, i) != null) {
                    String priorMessage = (String) t0.getValueAt(j,i);
                    v1.addPanel(
                            j, i - 2,
                            new JTreePanel(priorEndpoint, priorMessage, MainFrame.priorContract)
                    );
                }
            }
        }

        gp0.load(1,1, v1).add();

        //--------------------------------------------------------------------------------------------------------------

        //gp0.load(0,1, v1).setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------

        add(gp0,BorderLayout.CENTER);
    }

}

class JMessageTable extends JTable {

    public JMessageTable(String[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        setModel(model);

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        JLabel comp = (JLabel)super.prepareRenderer(renderer, row, col);
        String value = (String) getModel().getValueAt(row, col);
        if(col>1) {
            if (value != null) {
                comp.setBackground(new Color(60, 63, 65));
                if (isCellSelected(row, col))
                    comp.setBorder(BorderFactory.createLoweredBevelBorder());
                else
                    comp.setBorder(BorderFactory.createRaisedBevelBorder());
            } else {
                comp.setBackground(new Color(43, 43, 43));
            }
        }
        return comp;
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        return false;
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if (columnIndex == 0 || columnIndex == 1 || getValueAt(rowIndex,columnIndex) == null) {
           return;
        }
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }
}

class JTreePanel extends JPanel {

    public JTreePanel(Endpoint endpoint, String message, IContract contract) {
        Set<Property> properties;
        if(message.equals("Request"))
            properties = contract.getRequestProperties(endpoint);
        else
            properties = contract.getResponseProperties(endpoint, message);

        setLayout(new BorderLayout());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        createNodes(root, properties);
        JTree t0 = new JTree(root);
        expandAllNodes(t0, 0, t0.getRowCount());
        t0.setRootVisible(false);
        t0.setCellRenderer(new PropertyTreeRenderer());
        JScrollPane s0 = new JScrollPane(t0);
        add(s0,BorderLayout.CENTER);
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
        for(int i=startingIndex;i<rowCount;++i){
            tree.expandRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    private void createNodes(DefaultMutableTreeNode root, Set<Property> properties) {
        Map<String, Set<Property>> categories = new HashMap<>();

        for(PropertyKey.Location location : PropertyKey.Location.values()) {
            categories.put(location.name().toLowerCase(), new HashSet<>());
        }

        for(Property property : properties) {
            for (Map.Entry<String, Set<Property>> category : categories.entrySet()) {
                if(property.key.location.name().toLowerCase().equals(category.getKey())) {
                    category.getValue().add(property);
                    break;
                }
            }
        }

        for(Map.Entry<String, Set<Property>> category : categories.entrySet()) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category.getKey());
            root.add(categoryNode);
            createNodes(categoryNode, category.getValue(), 0);
        }
    }

    private void createNodes(DefaultMutableTreeNode parent, Set<Property> properties, int depth) {
        Map<String, Set<Property>> children = new HashMap<>();
        for(Property property : properties) {
            if(property.key.predecessors.size() == depth) {
                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(property);
                parent.add(categoryNode);
            }
            else {
                String parentName = property.key.predecessors.get(depth);
                children.putIfAbsent(parentName, new HashSet<>());
                children.get(parentName).add(property);
            }
        }

        for(Map.Entry<String,Set<Property>> entry : children.entrySet()) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(entry.getKey());
            parent.add(categoryNode);
            createNodes(categoryNode, entry.getValue(), depth+1);
        }
    }
}

class PropertyTreeRenderer extends DefaultTreeCellRenderer {

    public PropertyTreeRenderer() {}

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        if (userObject instanceof Property) {
            this.setText(((Property) userObject).key.name);
        } else {
            this.setText((String) userObject);
        }
        return this;
    }
}

class JPropertyPanel extends JPanel {

    public JPropertyPanel() {
        setLayout(new BorderLayout());
        JGridBagPanel gp0 = new JGridBagPanel();
        add(gp0,BorderLayout.CENTER);
    }
}
