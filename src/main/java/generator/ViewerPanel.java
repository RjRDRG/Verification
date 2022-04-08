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
            active = null;
        }
        panels.remove(index);
        repaint();
        revalidate();
    }

    public void setActive(int activeIndex) {
        if(active != null) {
            remove(active);
        }
        if(activeIndex < panels.size() && activeIndex >= 0) {
            active = panels.get(activeIndex);
            add(active, BorderLayout.CENTER);
        }
        repaint();
        revalidate();
    }

}
