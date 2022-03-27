package validation.http;

import java.util.LinkedList;
import java.util.List;

public class Form {
    public final String mediaType;
    public List<Property> properties;

    public Form(String mediaType) {
        this.mediaType = mediaType;
        this.properties = new LinkedList<>();
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }
}
