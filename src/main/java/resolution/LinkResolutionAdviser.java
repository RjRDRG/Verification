package resolution;

import contract.structures.Property;
import contract.structures.PropertyKey;
import resolution.structures.Resolution;

import java.util.*;

public class LinkResolutionAdviser {

    public enum Difference {NAME, LOCATION, PREDECESSOR}

    public List<Resolution> solve(Property np, Set<Property> ops) {

        List<Resolution> suggestions = new LinkedList<>();

        for(Property op : ops) {
            if(op.primitive != null && op.primitive.equals(np.primitive) && Objects.equals(op.format, np.format)) {
                Set<Difference> differences = getDifferences(np.key, op.key);

                ArrayList<String> tags = new ArrayList<>(2);
                if(differences.contains(Difference.NAME))
                    tags.add("rename");
                if(differences.contains(Difference.LOCATION) || differences.contains(Difference.PREDECESSOR))
                    tags.add("relocation");

                suggestions.add(Resolution.linkResolution(op.key, tags.toArray(new String[0])));
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
