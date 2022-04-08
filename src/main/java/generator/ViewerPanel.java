package generator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewerPanel<T extends JPanel> extends JPanel {

    private final List<T> panels;
    private T active;

    public ViewerPanel() {
        setLayout(new BorderLayout());
        this.panels = new ArrayList<>();
        this.active = null;
    }

    public void addPanel(T panel) {
        panels.add(panel);
    }

    public void removePanel(int index) {
        if(active == panels.get(index)) {
            remove(active);
        }
        panels.remove(index);
    }

    public void setActive(int activeIndex) {
        if(active != null) {
            remove(active);
        }
        active = panels.get(activeIndex);
        add(active, BorderLayout.CENTER);
    }

}
