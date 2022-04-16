package generator;

import contract.IContract;
import contract.structures.Endpoint;
import contract.structures.Property;
import contract.structures.PropertyKey;
import generator.ui.JContextButton;
import generator.ui.JGridBagPanel;
import generator.ui.JViewerPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;
import java.awt.*;
import java.util.*;

public class ResolutionPanel extends JPanel {

    final JGridBagPanel gp0;

    final JLabel l0;
    final JMessageTable t0;

    final JViewerPanel<JMessagePanel> v0;

    public final JButton back;
    public final JButton submit;

    public ResolutionPanel(String[][] data, String[] dataHeader) {
        setLayout(new BorderLayout());
        gp0 = new JGridBagPanel();

        //--------------------------------------------------------------------------------------------------------------

        l0 = new JLabel();
        t0 = new JMessageTable(data, dataHeader);

        v0 = new JViewerPanel<>(t0.getColumnCount()-2);

        back = new JButton();
        submit = new JButton();

        //--------------------------------------------------------------------------------------------------------------

        l0.setText("Messages");
        gp0.load(0,0, l0).setWidth(2).removeScaleY().add();

        ListSelectionListener selectionListener = e -> {
            if(e.getValueIsAdjusting())return;

            int c = t0.getSelectedColumn();
            int r = t0.getSelectedRow();

            if(c<2 || t0.getValueAt(r, c) == null) {
                v0.setActiveEmpty();
            }
            else {
                v0.setActive(r, c - 2);
            }
        };

        t0.getSelectionModel().addListSelectionListener(selectionListener);
        t0.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);

        final JGridBagPanel gpAux0 = new JGridBagPanel();
        gpAux0.load(0,0, t0.getTableHeader()).removeScaleY().add();
        gpAux0.load(0,1, t0).removeScaleY().removeScaleY().add();
        gpAux0.setBorder(BorderFactory.createLineBorder(new Color(97, 99, 101)));

        gp0.load(0,1, gpAux0).removeScaleY().setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------

        for(int j=0; j<t0.getRowCount(); j++) {
            for (int i = 2; i < t0.getColumnCount(); i++) {
                Endpoint endpoint = Endpoint.fromString((String) t0.getValueAt(j,0));
                Endpoint priorEndpoint = Endpoint.fromString((String) t0.getValueAt(j,1));
                if(t0.getValueAt(j, i) != null) {
                    String message = t0.getColumnName(i).replace("Response : ","");
                    String priorMessage = (String) t0.getValueAt(j,i);
                    v0.addPanel(
                            j, i - 2,
                            new JMessagePanel(
                                    new JTreePanel(endpoint, message, EditorFrame.CONTRACT),
                                    new JTreePanel(priorEndpoint, priorMessage, EditorFrame.PRIOR_CONTRACT)
                            )
                    );
                }
            }
        }

        gp0.load(0,2, v0).setWidth(2).setTopPad(15).add();

        //--------------------------------------------------------------------------------------------------------------

        back.setText("Back");
        gp0.load(0,3, back).removeScaleX().removeScaleY().setAnchorLeft().setTopPad(5).add();

        submit.setText("Submit");
        gp0.load(1,3, submit).removeScaleX().removeScaleY().setAnchorRight().setTopPad(5).add();

        add(gp0,BorderLayout.CENTER);
    }

}

class JMessagePanel extends JPanel {
    final JGridBagPanel gp0;

    final JGridBagPanel gpa;
    final JLabel l0;
    final JTreePanel t0;
    final JPropertyPanel p0;

    final JGridBagPanel gpb;
    final JLabel l1;
    final JTreePanel t1;
    final JPropertyPanel p1;

    final JGridBagPanel gpc;
    final JLabel l2;
    final JTable t2;

    final JContextButton b0;

    public JMessagePanel(JTreePanel treePanel, JTreePanel treePanel1) {
        setLayout(new BorderLayout());
        gp0 = new JGridBagPanel();

        //--------------------------------------------------------------------------------------------------------------

        gpa = new JGridBagPanel();
        l0 = new JLabel();
        t0 = treePanel;
        p0 = new JPropertyPanel();

        gpb = new JGridBagPanel();
        l1 = new JLabel();
        t1 = treePanel1;
        p1 = new JPropertyPanel();

        gpc = new JGridBagPanel();
        l2 = new JLabel();
        t2 = new JTable();

        b0 = new JContextButton();

        //--------------------------------------------------------------------------------------------------------------

        l0.setText("Properties: ");
        gpa.load(0,0, l0).removeScaleY().add();

        t0.tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) t0.tree.getLastSelectedPathComponent();
            if(node != null && node.getUserObject() instanceof Property) {
                p0.viewProperty((Property) node.getUserObject());
            } else {
                p0.resetView();
            }
        });
        t0.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        gpa.load(0,1, t0).add();

        gpa.load(0,2, p0).removeScaleY().setTopPad(5).add();

        //--------------------------------------------------------------------------------------------------------------

        l1.setText("Prior Properties: ");

        gpb.load(0,0, l1).removeScaleY().add();

        t1.tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) t1.tree.getLastSelectedPathComponent();
            if(node != null && node.getUserObject() instanceof Property) {
                p1.viewProperty((Property) node.getUserObject());
            } else {
                p1.resetView();
            }
        });
        gpb.load(0,1, t1).add();

        gpb.load(0,2, p1).removeScaleY().setTopPad(5).add();

        //--------------------------------------------------------------------------------------------------------------

        l2.setText("Resolutions");

        gpc.load(0,0, l2).removeScaleY().add();
        gpc.load(0,1, t2).add();

        //--------------------------------------------------------------------------------------------------------------

        b0.addContext("Default Value", "Link");

        //--------------------------------------------------------------------------------------------------------------
        Dimension d0 = gpa.getPreferredSize();
        d0.width = 180;
        gpa.setPreferredSize(d0);
        gp0.load(0,0, gpa).setWeight(0.3f,1).add();

        Dimension d1 = gpb.getPreferredSize();
        d1.width = 180;
        gpb.setPreferredSize(d1);
        gp0.load(1,0, gpb).setWeight(0.3f,1).setLeftPad(10).add();

        Dimension d2 = gpc.getPreferredSize();
        d2.width = 240;
        gpc.setPreferredSize(d2);
        gp0.load(2,0, gpc).setWeight(0.4f,1).setLeftPad(20).add();

        gp0.load(0,1, b0).removeScaleY().setWidth(2).add();

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

    public JTree tree;

    public JTreePanel(Endpoint endpoint, String message, IContract contract) {
        Set<Property> properties;
        if(message.equals("Request"))
            properties = contract.getRequestProperties(endpoint);
        else
            properties = contract.getResponseProperties(endpoint, message);

        setLayout(new BorderLayout());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        createNodes(root, properties);
        tree = new JTree(root);
        tree.setDragEnabled(false);
        expandAllNodes(tree, 0, tree.getRowCount());
        tree.setRootVisible(false);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
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
        });
        tree.setSelectionModel(new DefaultTreeSelectionModel() {
            @Override
            public void setSelectionPath(TreePath path) {
                if(tree.isPathSelected(path)) {
                    tree.removeSelectionPath(path);
                }
                else {
                    super.setSelectionPath(path);
                }
            }
        });
        JScrollPane s0 = new JScrollPane(tree);
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

    public void resetView() {
        name.setText("");
        isArray.setSelected(false);
        primitive.setText("");
        format.setText("");
        format.setEnabled(false);
    }
}
