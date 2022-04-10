package generator.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class JViewerPanel<T extends JPanel> extends JPanel {

    private final Map<Integer, T> panels;
    private T active;
    private int numberOfRows;

    public JViewerPanel(int numberOfRows) {
        setLayout(new BorderLayout());
        this.panels = new HashMap<>();
        this.active = null;
        this.numberOfRows = numberOfRows;
    }

    public int flattenCoordinates(int row, int column) {
        return column + row*numberOfRows;
    }

    public void addPanel(int row, int column, T panel) {
        panels.put(flattenCoordinates(row,column),panel);
    }

    public void removePanel(int row, int column) {
        int index = flattenCoordinates(row,column);
        if(active.equals(panels.get(index))) {
            remove(active);
            active = null;
        }
        panels.remove(index);
        repaint();
        revalidate();
    }

    public void setActive(int row, int column) {
        int index = flattenCoordinates(row,column);
        if(!panels.containsKey(index))
            return;

        if(active != null)
            remove(active);

        active = panels.get(index);
        add(active, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
}
