package resolution;

import contract.structures.Property;
import contract.structures.PropertyKey;
import resolution.structures.Difference;
import resolution.structures.Resolution;

import java.util.*;

public class SimpleResolutionAdviser implements IResolutionAdviser {

    @Override
    public Map<Set<Difference>, List<Resolution>> solve(Property np, Set<Property> ops) {

        Map<Set<Difference>, List<Resolution>> suggestions = new HashMap<>();

        //USE DEFAULT VALUE
        if(!np.required) {
            suggestions.put(Set.of(Difference.NEW), List.of(Resolution.defaultValueResolution(np.defaultValue)));
        }

        for(Property op : ops) {
            if(op.primitive != null && op.primitive.equals(np.primitive) && Objects.equals(op.format, np.format)) {
                Set<Difference> differences = getDifferences(np.key, op.key);
                suggestions.putIfAbsent(differences, new LinkedList<>());
                suggestions.get(differences).add(Resolution.linkResolution(op.key));
            }
        }

        return suggestions;
    }

    public Set<Difference> getDifferences(PropertyKey k0, PropertyKey k1) {
        Set<Difference> differences = new HashSet<>();

        if(!k1.location.equals(k0.location))
            differences.add(Difference.LOCATION);

        if(!k1.predecessors.equals(k0.predecessors))
            differences.add(Difference.PREDECESSOR);

        if(!Objects.equals(k1.name, k0.name))
            differences.add(Difference.NAME);

        return differences;
    }
}
