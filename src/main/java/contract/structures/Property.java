package contract.structures;

import java.util.List;
import java.util.Objects;

public class Property {
    public final PropertyKey key;
    public final boolean array;
    public final String primitive;
    public final String format;

    public final boolean required;
    public final String defaultValue;

    public Property(PropertyKey.Type type, String location, List<String> precursors, String name, boolean array, String primitive, String format, boolean required, String defaultValue) {
        this.key = new PropertyKey(type, location, precursors, name);
        this.array = array;
        this.primitive = primitive;
        this.format = format;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return array == property.array && key.equals(property.key) && primitive.equals(property.primitive) && format.equals(property.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, array, primitive, format);
    }
}
