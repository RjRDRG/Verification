package generator;

import generator.ui.ColoredString;
import generator.ui.JColoredList;
import generator.ui.JGridPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DualPairPickerPanel extends JPanel {

    private static String DIVIDER = "  <->  ";
    private static String SECOND_DIVIDER = "  |  ";

    public DualPairPickerPanel(
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

        final JColoredList ls2 = new JColoredList();   final ViewerPanel<PairComboPanel> viewerPanel = new ViewerPanel<>();
        JButton bt1 = new JButton();

        //--------------------------------------------------------------------------------------------------------------

        la0.setText(title0);
        la0.setAlignmentX(SwingConstants.CENTER);

        gp0.load(0,0, la0).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<String> m0 = new DefaultListModel<>();
        m0.addAll(elements0.keySet());
        ls0.setModel(m0);
        ls0.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if(!elements0.isEmpty()) ls0.setSelectedIndex(0);
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
        if(!elements1.isEmpty()) ls1.setSelectedIndex(0);
        JScrollPane s1 = new JScrollPane(ls1);

        gp0.load(1,1, s1).add();

        //--------------------------------------------------------------------------------------------------------------

        bt0.setText("Make Pair");
        bt0.addActionListener(e -> {
            if(!(ls0.getSelectedIndex()>=0 && ls1.getSelectedIndex()>=0))
                return;

            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
            DefaultListModel<ColoredString> lm2 = (DefaultListModel<ColoredString>) ls2.getModel();

            String se0 = ls0.getSelectedValue();
            String se1 = ls1.getSelectedValue();

            ColoredString coloredString = ColoredString.red(se0 + DIVIDER + se1);
            lm2.addElement(coloredString);

            PairComboPanel pairComboPanel = new PairComboPanel(
                    new ArrayList<>(Optional.ofNullable(elements0.get(se0)).orElse(pairs.get(se0))),
                    new ArrayList<>(Optional.ofNullable(elements1.get(se1)).orElse(pairs.get(se1))),
                    b -> {
                        if(b) coloredString.setGreen();
                        else coloredString.setRed();
                        repaint();
                        revalidate();
                    }
            );

            pairComboPanel.callEvent(null);

            viewerPanel.addPanel(pairComboPanel);

            ls2.setSelectedIndex(lm2.size()-1);
            viewerPanel.setActive(lm2.size()-1);

            lm0.remove(ls0.getSelectedIndex());
            lm1.remove(ls1.getSelectedIndex());

            if(!lm0.isEmpty()) ls0.setSelectedIndex(0);
            if(!lm1.isEmpty()) ls1.setSelectedIndex(0);
        });

        gp0.load(0,2, bt0).setWidth(2).removeScaleY().add();

        //--------------------------------------------------------------------------------------------------------------

        DefaultListModel<ColoredString> m2 = new DefaultListModel<>();

        for (Map.Entry<String,Set<String>> pair : pairs.entrySet()) {
            m2.addElement(ColoredString.green(pair.getKey() + DIVIDER + pair.getKey()));

            viewerPanel.addPanel(
                    new PairComboPanel(
                            new ArrayList<>(pair.getValue()),
                            new ArrayList<>(pair.getValue()),
                            b -> {
                                if(b) ls2.getSelectedValue().setGreen();
                                else ls2.getSelectedValue().setRed();
                                repaint();
                                revalidate();
                            }
                    )
            );
        }

        ls2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ls2.setModel(m2);
        ls2.addListSelectionListener( e -> {
            if (!e.getValueIsAdjusting()){
                viewerPanel.setActive(((JColoredList)e.getSource()).getSelectedIndex());
            }
        });

        if(!m2.isEmpty()) ls2.setSelectedIndex(0);
        JScrollPane s2 = new JScrollPane(ls2);

        JGridPanel gp1 = new JGridPanel();

        gp1.load(0,0, s2).add();
        gp1.load(1,0, viewerPanel).removeScaleX().add();

        gp0.load(0,3, gp1).setTopPad(20).setWidth(2).add();

        //--------------------------------------------------------------------------------------------------------------

        bt1.setText("Unpair");
        bt1.addActionListener(e -> {
            if(ls2.getSelectedIndex()<0)
                return;

            DefaultListModel<String> lm0 = (DefaultListModel<String>) ls0.getModel();
            DefaultListModel<String> lm1 = (DefaultListModel<String>) ls1.getModel();
            DefaultListModel<ColoredString> lm2 = (DefaultListModel<ColoredString>) ls2.getModel();

            String[] parts = ls2.getSelectedValue().string.split(DIVIDER);

            lm0.addElement(parts[0]);
            if(ls0.getSelectedIndex()<0) ls0.setSelectedIndex(0);

            lm1.addElement(parts[1]);
            if(ls1.getSelectedIndex()<0) ls1.setSelectedIndex(0);

            viewerPanel.removePanel(ls2.getSelectedIndex());
            lm2.remove(ls2.getSelectedIndex());
            if(!lm2.isEmpty()) ls2.setSelectedIndex(0);
        });

        gp0.load(0,4, bt1).setWidth(2).removeScaleY().add();
    }
}
