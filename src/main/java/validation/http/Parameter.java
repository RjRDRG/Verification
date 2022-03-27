package validation.http;

import validation.resolution.Resolution;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Parameter {
    public final String key;
    public final String location;

    Resolution resolution;
    List<Resolution> suggestions;

    public Parameter(String key, String location) {
        this.key = key;
        this.location = location;

        this.resolution = null;
        suggestions = new LinkedList<>();
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
        Parameter parameter = (Parameter) o;
        return key.equals(parameter.key) && location.equals(parameter.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, location);
    }
}
