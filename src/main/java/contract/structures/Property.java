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

    public Property(PropertyKey.Type type, PropertyKey.Location location, List<String> precursors, String name, boolean array, String primitive, String format, boolean required, String defaultValue) {
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
        return array == property.array && key.equals(property.key) && primitive.equals(property.primitive) && Objects.equals(format, property.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, array, primitive, format);
    }

    @Override
    public String toString() {
        return String.format(
                "%-40s %-20s %-20s %-20s %-20s %-20s",
                "key: " + key, "isArray: " + array, "primitive: " + primitive, "format: " + format, "required: " + required, "default: " + defaultValue
        );
    }
}
