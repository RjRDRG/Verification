package generator;

import generator.ui.JGridPanel;
import generator.ui.RoundBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class PairComboPanel extends JPanel {

    static final String NONE = "None";

    Map<String,JComboBox<String>> pairs;
    Consumer<Boolean> okConsumer;

    public PairComboPanel(
            List<String> elementsList0,
            List<String> elementsList1,
            Consumer<Boolean> okConsumer
    ) {
        setLayout(new BorderLayout());

        this.pairs = new HashMap<>();
        this.okConsumer = okConsumer;

        JGridPanel gp0 = new JGridPanel();

        final List<String> elementsList1WithNone = new ArrayList<>(elementsList1.size()+1);
        elementsList1WithNone.add(NONE);
        elementsList1WithNone.addAll(elementsList1);

        for (int i = 0; i < elementsList0.size(); i++) {
            JLabel la2 = new JLabel(elementsList0.get(i), SwingConstants.CENTER);
            la2.setFont(new Font("Serif", Font.PLAIN, 15));

            JComboBox<String> c0 = new JComboBox<>(elementsList1WithNone.toArray(new String[0]));
            ((JLabel)c0.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            c0.setFont(new Font("Serif", Font.PLAIN, 15));
            c0.setSelectedItem(elementsList0.get(i));
            c0.addActionListener(this::callEvent);

            gp0.load(0, i, la2).setItemBorder(new RoundBorder(Color.black)).add();
            gp0.load(1, i, c0).add();

            pairs.put(elementsList0.get(i),c0);
        }

        add(gp0);
    }

    public void callEvent(ActionEvent e) {
        for(JComboBox<String> jComboBox : pairs.values()) {
            if(Objects.equals(jComboBox.getSelectedItem(), NONE)) {
                okConsumer.accept(false);
                return;
            }
        }
        okConsumer.accept(true);
    }
}
