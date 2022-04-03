package resolution;

import contract.structures.Property;
import resolution.structures.Resolution;

import java.util.*;

public class ValueResolutionAdviser {

    public List<Resolution> solve(Property np) {

        List<Resolution> suggestions = new LinkedList<>();

        if(!np.required) {
            suggestions.add(Resolution.valueResolution(np.defaultValue, "default"));
        }

        return suggestions;
    }
}
