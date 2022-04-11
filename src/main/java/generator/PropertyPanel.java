package generator;

import contract.IContract;
import contract.structures.Endpoint;
import contract.structures.Property;
import contract.structures.PropertyKey;
import generator.ui.JGridBagPanel;
import generator.ui.JViewerPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.*;
import java.util.List;

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

        t0.getSelectionModel().addListSelectionListener(e -> {
            int c = t0.getSelectedColumn();
            int r = t0.getSelectedRow();

            if(c<2 || t0.getValueAt(r, c) == null)
                return;

            v0.setActive(r, c-2);
            v1.setActive(r, c-2);
        });

        gp0.load(0,0, t0.getTableHeader()).removeScaleY().setWidth(2).add();
        gp0.load(0,1, t0).removeScaleY().setWidth(2).add();

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

        gp0.load(0,2, v0).add();

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

        gp0.load(1,2, v1).add();

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
