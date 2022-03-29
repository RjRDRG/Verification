package contract.structures;

import java.util.*;

public class PropertyKey {
    public enum Type {PARAMETER, BODY}

    public final Type type;
    public final String location;
    public final List<String> precursors;
    public final String name;

    public PropertyKey(Type type, String location, List<String> precursors, String name) {
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
                + location + ":"
                + String.join(".", p);
    }

    public PropertyKey fromString(String s0) {
        String[] s1 = s0.split("-");
        Type type = s1[0].equals(Type.PARAMETER.name().toLowerCase()) ? Type.PARAMETER : Type.BODY;

        String[] s2 = s1[1].split(":");
        String location = s2[0];

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
