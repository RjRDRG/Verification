package validation.http;

import validation.resolution.Resolution;

import java.util.Arrays;
import java.util.List;

public class Field {
    public final String[] keys;

    Resolution resolution;
    List<Resolution> suggestions;

    public Field(String[] keys) {
        this.keys = keys;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public List<Resolution> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Resolution> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Arrays.equals(keys, field.keys);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }
}
