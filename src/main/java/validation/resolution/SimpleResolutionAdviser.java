package validation.resolution;

import validation.http.Property;

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
            if(op.type.equals(np.type) && op.location.equals(np.location)) {
                suggestions.add(Resolution.keyResolution(op.key, op.location));
            }
        }

        //PARAMETER LOCATION CHANGE
        for(Property op : ops) {
            if(op.type.equals(np.type) && op.key.equals(np.key)) {
                suggestions.add(Resolution.keyResolution(op.key, op.location));
            }
        }

        //PARAMETER RENAME AND LOCATION CHANGE
        for (Property op : ops) {
            if (op.type.equals(np.type)) {
                suggestions.add(Resolution.keyResolution(op.key, op.location));
            }
        }

        return suggestions;
    }
}
