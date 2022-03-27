package validation.resolution;

import java.util.Map;

public class Resolution {
    public final ResolutionRule resolutionRule;
    public final Map<ARG_TYPE, String> resolutionArgs;

    public enum ARG_TYPE {NAME, LOCATION, DEFAULT_VALUE}

    private Resolution(ResolutionRule resolutionRule, Map<ARG_TYPE, String> resolutionArgs) {
        this.resolutionRule = resolutionRule;
        this.resolutionArgs = resolutionArgs;
    }

    public static Resolution keyResolution(String name, String location) {
        Map<ARG_TYPE, String> args = Map.of(
                ARG_TYPE.NAME, name,
                ARG_TYPE.LOCATION, location
        );
        return new Resolution(ResolutionRule.KEY, args);
    }

    public static Resolution defaultValueResolution(String defaultValue) {
        Map<ARG_TYPE, String> args = Map.of(
                ARG_TYPE.DEFAULT_VALUE, defaultValue
        );
        return new Resolution(ResolutionRule.DEFAULT_VALUE, args);
    }
}
