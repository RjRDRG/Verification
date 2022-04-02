package resolution.structures;

import contract.structures.PropertyKey;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Resolution {

    public enum Rule {LINK, DEFAULT_VALUE, FUNCTION}
    public final Rule rule;

    public enum ARG_TYPE {KEY, DEFAULT_VALUE}
    public final Map<ARG_TYPE, String> resolutionArgs;

    private Resolution(Rule rule, Map<ARG_TYPE, String> resolutionArgs) {
        this.rule = rule;
        this.resolutionArgs = resolutionArgs;
    }

    public static Resolution keyResolution(PropertyKey key) {
        Map<ARG_TYPE, String> args = Map.of(
                ARG_TYPE.KEY, key.toString()
        );
        return new Resolution(Rule.LINK, args);
    }

    public static Resolution defaultValueResolution(String defaultValue) {
        Map<ARG_TYPE, String> args = Map.of(
                ARG_TYPE.DEFAULT_VALUE, Optional.ofNullable(defaultValue).orElse("null")
        );
        return new Resolution(Rule.DEFAULT_VALUE, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resolution that = (Resolution) o;
        return rule == that.rule && resolutionArgs.equals(that.resolutionArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, resolutionArgs);
    }
}
