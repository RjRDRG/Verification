package validation.resolution;

import validation.http.Property;

import java.util.List;
import java.util.Set;

public interface IResolutionAdviser {
    List<Resolution> solve(Property parameter, Set<Property> data);
}
