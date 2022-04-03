package resolution.structures;

import contract.structures.PropertyKey;

import java.util.Objects;
import java.util.Set;

public class Resolution {

    public static String LINK = "link=";
    public static String VALUE = "value=";
    public static String TYPE = "type=";

    public final String resolution;
    public final Set<String> tags;

    private Resolution(String resolution, Set<String> tags) {
        this.resolution = resolution;
        this.tags = tags;
    }

    public static Resolution linkResolution(PropertyKey key, String... tags) {
        String resolution = LINK + key.toString();
        return new Resolution(resolution, Set.of(tags));
    }

    public static Resolution valueResolution(String value, String... tags) {
        String resolution = VALUE + value;
        return new Resolution(resolution, Set.of(tags));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resolution that = (Resolution) o;
        return resolution.equals(that.resolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolution);
    }
}
