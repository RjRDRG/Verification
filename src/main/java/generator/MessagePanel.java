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
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.*;

public class MessagePanel extends JPanel {

    public MessagePanel(String[][] data, String[] dataHeader) {
        setLayout(new BorderLayout());
        final JGridBagPanel gp0 = new JGridBagPanel();

        //--------------------------------------------------------------------------------------------------------------

        final JGridBagPanel gp1 = new JGridBagPanel();
        final JLabel l0 = new JLabel();
        final JMessageTable t0 = new JMessageTable(data, dataHeader);

        final JGridBagPanel gp2a = new JGridBagPanel();
        final JLabel l1 = new JLabel();
        final JViewerPanel<JTreePanel> v0 = new JViewerPanel<>(t0.getColumnCount()-2);
        final JPropertyPanel p0 = new JPropertyPanel();

        final JGridBagPanel gp2b = new JGridBagPanel();
        final JLabel l2 = new JLabel();
        final JViewerPanel<JTreePanel> v1 = new JViewerPanel<>(t0.getColumnCount()-2);
        final JPropertyPanel p1 = new JPropertyPanel();

        final JGridBagPanel gp2c = new JGridBagPanel();
        final JLabel l3 = new JLabel();
        final JTable t2 = new JTable();

        //--------------------------------------------------------------------------------------------------------------

        l0.setText("Messages");
        gp1.load(0,0, l0).add();

        ListSelectionListener selectionListener = e -> {
            if(e.getValueIsAdjusting())return;

            int c = t0.getSelectedColumn();
            int r = t0.getSelectedRow();

            if(c<2 || t0.getValueAt(r, c) == null) {
                v0.setActiveEmpty();
                v1.setActiveEmpty();
            }
            else {
                l1.setText("Properties: " + t0.getValueAt(r,0) + " " + t0.getColumnName(c).replace("Response : ",""));
                v0.setActive(r, c - 2);

                l2.setText("Properties: " + t0.getValueAt(r,1) + " " + t0.getValueAt(r,c));
                v1.setActive(r, c - 2);
            }
        };

        t0.getSelectionModel().addListSelectionListener(selectionListener);
        t0.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);

        final JGridBagPanel gpAux0 = new JGridBagPanel();
        gpAux0.load(0,0, t0.getTableHeader()).removeScaleY().add();
        gpAux0.load(0,1, t0).removeScaleY().add();
        gpAux0.setBorder(BorderFactory.createLineBorder(new Color(97, 99, 101)));

        gp1.load(0,1, gpAux0).add();

        //--------------------------------------------------------------------------------------------------------------

        l1.setText("Properties: ");

        gp2a.load(0,0, l1).removeScaleY().add();

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

        gp2a.load(0,1, v0).add();

        gp2a.load(0,2, p0).removeScaleY().setTopPad(5).add();

        //--------------------------------------------------------------------------------------------------------------

        l2.setText("Properties: ");

        gp2b.load(0,0, l2).removeScaleY().add();

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

        gp2b.load(0,1, v1).add();

        gp2b.load(0,2, p1).removeScaleY().setTopPad(5).add();

        //--------------------------------------------------------------------------------------------------------------

        l3.setText("Resolutions");

        gp2c.load(0,0, l3).removeScaleY().add();
        gp2c.load(0,1, t2).add();

        //--------------------------------------------------------------------------------------------------------------

        gp0.load(0,0, gp1).removeScaleY().setWidth(3).add();

        Dimension d0 = gp2a.getPreferredSize();
        d0.width = 180;
        gp2a.setPreferredSize(d0);
        gp0.load(0,1, gp2a).setTopPad(20).setWeight(0.3f,1).add();

        Dimension d1 = gp2b.getPreferredSize();
        d1.width = 180;
        gp2b.setPreferredSize(d1);
        gp0.load(1,1, gp2b).setTopPad(20).setWeight(0.3f,1).setLeftPad(10).add();

        Dimension d2 = gp2c.getPreferredSize();
        d2.width = 240;
        gp2c.setPreferredSize(d2);
        gp0.load(2,1, gp2c).setTopPad(20).setWeight(0.4f,1).setLeftPad(20).add();

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
                comp.setText("Edit");
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
        t0.setDragEnabled(false);
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

    JTextField name;
    JCheckBox isArray;
    JTextField primitive;
    JTextField format;

    public JPropertyPanel() {
        setLayout(new BorderLayout());
        JGridBagPanel gp0 = new JGridBagPanel();

        JLabel nameLabel = new JLabel("Name: ");
        name = new JTextField("");
        name.setEnabled(false);

        gp0.load(0,0,nameLabel).setTopPad(5).setLeftPad(10).add();
        gp0.load(1,0,name).setTopPad(5).setRightPad(10).add();

        JLabel arrayLabel = new JLabel("Is Array: ");
        isArray = new JCheckBox("", false);
        isArray.setEnabled(false);

        gp0.load(0,1,arrayLabel).setLeftPad(10).add();
        gp0.load(1,1,isArray).setRightPad(10).add();

        JLabel primitiveLabel = new JLabel("Primitive Type: ");
        primitive = new JTextField("");
        primitive.setEnabled(false);

        gp0.load(0,2,primitiveLabel).setLeftPad(10).add();
        gp0.load(1,2,primitive).setRightPad(10).add();

        JLabel formatLabel = new JLabel("Format: ");
        format = new JTextField("");
        format.setEnabled(false);

        gp0.load(0,3,formatLabel).setLeftPad(10).setBottomPad(15).add();
        gp0.load(1,3,format).setRightPad(10).setBottomPad(10).add();

        add(gp0,BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Property view"));
    }

    public void viewProperty(Property property) {
        name.setText(property.key.name);
        isArray.setSelected(property.array);
        primitive.setText(property.primitive);
        format.setText(property.format);
        format.setEnabled(false);
    }
}
