package generator;

import generator.old.JLinePanel;
import generator.ui.JGridPanel;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PairPickerPanel extends JPanel {

    public PairPickerPanel(String title0, List<String> elementsList0, String title1, List<String> elementsList1, List<Pair<String,String>> pairList) {
        setLayout(new BorderLayout());

        JGridPanel gridPanel = new JGridPanel();

        JLabel la0 = new JLabel(title0);
        JTextField te0 = new JTextField();

        gridPanel.load(0,0, la0, te0).add();

        JLabel la1 = new JLabel(title1);
        JTextField te1 = new JTextField();

        gridPanel.load(1,0, la1, te1).add();

        final JList<String> ls0 = new JList<>(elementsList0.toArray(new String[0]));
        ls0.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane s0 = new JScrollPane(ls0);

        te0.addActionListener(e -> {
            String regex = ((JTextField)e.getSource()).getText();
            ls0.setListData(elementsList0.stream().filter(s -> s.matches(regex)).toArray(String[]::new));
        });

        gridPanel.load(0,1, s0).removePad().removeItemBorder().add();

        final JList<String> ls1 = new JList<>(elementsList1.toArray(new String[0]));
        ls0.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane s1 = new JScrollPane(ls0);

        te1.addActionListener(e -> {
            String regex = ((JTextField)e.getSource()).getText();
            ls1.setListData(elementsList1.stream().filter(s -> s.matches(regex)).toArray(String[]::new));
        });

        gridPanel.load(1,1, s1).removePad().removeItemBorder().add();

        Function<Pair<String,String>,JPanel> pairFunc = p -> {
            JGridPanel gp = new JGridPanel();

            JLabel l0 = new JLabel(p.getLeft());
            JLabel l1 = new JLabel(p.getLeft());
            JButton b0 = new JButton(UIManager.getIcon("Tree.closedIcon")); //InternalFrameTitlePane.closeButton"));

            gp.load(0,0, l0).setAnchorLeft().removeItemBorder().add();
            gp.load(1,0, l1).setAnchorLeft().removeItemBorder().add();
            gp.load(2,0, b0).setAnchorRight().removeItemBorder().add();

            return gp;
        };

        final JList<JPanel> ls2 = new JList<>(pairList.stream().map(pairFunc).toArray(JPanel[]::new));

        JButton bt0 = new JButton("Make Pair");
        bt0.addActionListener(e -> {
            ls2.add(pairItems(elements0.getSelectedValue(), elements1.getSelectedValue()));
            elements0.remove(elements0.getSelectedIndex());
            elements1.remove(elements1.getSelectedIndex());
        });

        gridPanel.load(0,2, s1).add();

        JLinePanel row = JLinePanel.row(10,10,0);
        JLabel la0 = new JLabel("Pairs");
        row.add(la0).close();

    }



    public static void main(String[] args) {
        String p0 = "src/main/resources/new.yaml";
        String p1 = "src/main/resources/old.yaml";

        JPanel jPanel = new JPanel();

        JFrame jFrame = new JFrame();
        PairPickerPanel panel = new PairPickerPanel();

        jFrame.getContentPane().setLayout(new BorderLayout());
        jFrame.getContentPane().add(panel, BorderLayout.CENTER);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //setPreferredSize(new Dimension(600, 600)); pack();
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
