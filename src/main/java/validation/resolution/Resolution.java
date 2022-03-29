package validation.resolution;

import contract.structures.PropertyKey;

import java.util.Map;

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
                ARG_TYPE.DEFAULT_VALUE, defaultValue
        );
        return new Resolution(Rule.DEFAULT_VALUE, args);
    }


}
