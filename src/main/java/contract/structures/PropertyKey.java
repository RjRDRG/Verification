package contract.structures;

import java.util.*;

public class PropertyKey {
    public enum Type {PARAMETER, BODY}
    public enum Location {HEADER, PATH, QUERY, COOKIE, JSON}

    public final Type type;
    public final Location location;
    public final List<String> precursors;
    public final String name;

    public PropertyKey(Type type, Location location, List<String> precursors, String name) {
        this.type = type;
        this.location = location;
        this.precursors = precursors;
        this.name = name;
    }

    @Override
    public String toString() {
        List<String> p = new LinkedList<>(precursors);
        p.add(name);
        return type.name().toLowerCase() + "-"
                + location.name().toLowerCase() + ":"
                + String.join(".", p);
    }

    public PropertyKey fromString(String s0) {
        String[] s1 = s0.split("-");
        Type type = Type.valueOf(s1[0].toUpperCase());

        String[] s2 = s1[1].split(":");
        Location location = Location.valueOf(s2[0].toUpperCase());

        String[] s3 = s2[1].split(".");
        String name = s3[s3.length-1];

        List<String> precursors;
        if(s3.length == 1) {
            precursors = Collections.emptyList();
        }
        else {
            precursors = Arrays.asList(s3).subList(0, s3.length-2);
        }

        return new PropertyKey(type,location,precursors,name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyKey that = (PropertyKey) o;
        return type == that.type && location.equals(that.location) && precursors.equals(that.precursors) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, location, precursors, name);
    }
}
