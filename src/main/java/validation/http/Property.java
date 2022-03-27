package validation.http;

import java.util.Objects;

public class Property {
    public final String key;
    public final String location;
    public final String type;
    public final boolean required;
    public final String defaultValue;

    public Property(String key, String location, String type, boolean required, String defaultValue) {
        this.key = key;
        this.location = location;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property that = (Property) o;
        return key.equals(that.key) && location.equals(that.location) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, location, type);
    }
}
