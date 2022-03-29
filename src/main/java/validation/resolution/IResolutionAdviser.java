package validation.resolution;

import contract.structures.Property;

import java.util.List;
import java.util.Set;

public interface IResolutionAdviser {
    List<Resolution> solve(Property parameter, Set<Property> data);
}
