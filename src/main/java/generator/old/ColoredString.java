package generator.old;

import java.awt.*;

public class ColoredString extends Component{
    public final String string;
    public Color color;

    public ColoredString(String string, Color color) {
        this.string = string;
        this.color = color;
    }

    public static ColoredString red(String string) {
        return new ColoredString(string,new Color(199, 84, 80));
    }

    public static ColoredString green(String string) {
        return new ColoredString(string,new Color(90, 194, 104));
    }

    public void setRed() {
        this.color = new Color(199, 84, 80);
    }

    public void setGreen() {
        this.color = new Color(90, 194, 104);
    }
}
