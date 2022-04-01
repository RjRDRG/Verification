package resolution;

import contract.structures.Property;
import contract.structures.PropertyKey;
import resolution.structures.Resolution;

import java.util.*;

public class SimpleResolutionAdviser implements IResolutionAdviser {

    @Override
    public List<Resolution> solve(Property np, Set<Property> ops) {

        List<List<Resolution>> suggestions = new ArrayList<>(getDifferencesMaxWeight());
        for(int i=0; i<getDifferencesMaxWeight()+1; i++) {
            suggestions.add(new LinkedList<>());
        }

        //USE DEFAULT VALUE
        if(!np.required) {
            suggestions.set(0, List.of(Resolution.defaultValueResolution(np.defaultValue)));
        }

        for(Property op : ops) {
            if(op.primitive.equals(np.primitive) && op.format.equals(np.format)) {
                Set<Differences> differences = getDifferences(np.key, op.key);
                int resolutionWeight = getDifferencesWeight(differences);

                suggestions.get(resolutionWeight).add(Resolution.keyResolution(op.key));
            }
        }

        List<Resolution> result = new LinkedList<>();
        suggestions.forEach(result::addAll);
        return result;
    }

    private enum Differences {NAME, LOCATION, PRECURSORS}
    private Set<Differences> getDifferences(PropertyKey k0, PropertyKey k1) {
        Set<Differences> differences = new HashSet<>();

        if(!k1.location.equals(k0.location))
            differences.add(Differences.LOCATION);

        if(!k1.precursors.equals(k0.precursors))
            differences.add(Differences.PRECURSORS);

        if(!Objects.equals(k1.name, k0.name))
            differences.add(Differences.NAME);

        return differences;
    }


    private int getDifferencesWeight(Set<Differences> differences) {
        if(differences.isEmpty()) {
            return 0;
        }
        else if(differences.size() == 1 && differences.contains(Differences.NAME)) { //PARAMETER RENAME
            return 1;
        }
        else if(!differences.contains(Differences.NAME)) {  //PARAMETER LOCATION CHANGE
            return differences.size()+1; //2-3
        }
        else  //PARAMETER RENAME AND LOCATION CHANGE
            return differences.size() + Differences.values().length - 1; //4-5
    }

    private int getDifferencesMaxWeight() {
        return (2*Differences.values().length) - 1;
    }
}
