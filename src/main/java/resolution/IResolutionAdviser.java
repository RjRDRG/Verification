package resolution;

import contract.structures.Property;
import resolution.structures.Difference;
import resolution.structures.Resolution;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IResolutionAdviser {
    Map<Set<Difference>, List<Resolution>> solve(Property parameter, Set<Property> data);
}
