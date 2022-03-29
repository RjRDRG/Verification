package validation.resolution;

import contract.structures.Property;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SimpleResolutionAdviser implements IResolutionAdviser {

    @Override
    public List<Resolution> solve(Property np, Set<Property> ops) {

        List<Resolution> suggestions = new LinkedList<>();

        //USE DEFAULT VALUE
        if(!np.required) {
            suggestions.add(Resolution.defaultValueResolution(np.defaultValue));
        }

        //PARAMETER RENAME
        for(Property op : ops) {
            if(op.primitive.equals(np.primitive) && op.location.equals(np.location)) {
                suggestions.add(Resolution.parameterKeyResolution(op.key, op.location));
            }
        }

        //PARAMETER LOCATION CHANGE
        for(Property op : ops) {
            if(op.primitive.equals(np.primitive) && op.key.equals(np.key)) {
                suggestions.add(Resolution.parameterKeyResolution(op.key, op.location));
            }
        }

        //PARAMETER RENAME AND LOCATION CHANGE
        for (Property op : ops) {
            if (op.primitive.equals(np.primitive)) {
                suggestions.add(Resolution.parameterKeyResolution(op.key, op.location));
            }
        }

        return suggestions;
    }
}
