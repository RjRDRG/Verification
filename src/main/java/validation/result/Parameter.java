package validation.result;

public class Parameter {
    final String key;
    boolean satisfied;
    boolean ambiguous;
    ResolutionRule resolutionRule;
    String[] resolutionArgs;

    public Parameter(String key) {
        this.key = key;
        this.satisfied = false;
    }
}
