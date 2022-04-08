package generator.ui;

import java.awt.*;

public class ColoredString extends Component{
    public final String string;
    public Color color;

    public ColoredString(String string, Color color) {
        this.string = string;
        this.color = color;
    }

    public static ColoredString red(String string) {
        return new ColoredString(string,Color.red);
    }

    public static ColoredString green(String string) {
        return new ColoredString(string,Color.green);
    }

    public void setRed() {
        this.color = Color.red;
    }

    public void setGreen() {
        this.color = Color.green;
    }
}
