package resolution;

import contract.structures.Property;
import resolution.structures.Resolution;

import java.util.List;
import java.util.Set;

public interface IResolutionAdviser {
    List<Resolution> solve(Property parameter, Set<Property> data);
}
