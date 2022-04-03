package resolution.structures;

import contract.structures.PropertyKey;

import java.util.Objects;

public class Resolution {

    public enum Rule {LINK, DEFAULT_VALUE, FUNCTION}
    public final Rule rule;

    public final String resolution;

    private Resolution(Rule rule, String resolution) {
        this.rule = rule;
        this.resolution = resolution;
    }

    public static Resolution linkResolution(PropertyKey key) {
        return new Resolution(Rule.LINK, key.toString());
    }

    public static Resolution defaultValueResolution(String defaultValue) {
        return new Resolution(Rule.DEFAULT_VALUE, defaultValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resolution that = (Resolution) o;
        return rule == that.rule && Objects.equals(resolution, that.resolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, resolution);
    }
}
