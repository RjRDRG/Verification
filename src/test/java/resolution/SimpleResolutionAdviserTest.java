package resolution;

import contract.structures.Property;
import contract.structures.PropertyKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resolution.structures.Resolution;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SimpleResolutionAdviserTest {

    SimpleResolutionAdviser adviser;

    @BeforeEach
    void setUp() {
        this.adviser = new SimpleResolutionAdviser();
    }

    @Test
    void testSolve() {
        Property np;
        Map<Integer, Property> ops;
        List<Resolution> resolutions;

        np = new Property(
                PropertyKey.Location.JSON,
                List.of("user"),
                "email",
                false,
                "string",
                null,
                false,
                null
        );
        ops = Map.of(
                1, new Property(
                        PropertyKey.Location.JSON,
                        Collections.emptyList(),
                        "email",
                        false,
                        "string",
                        null,
                        false,
                        null
                ),
                2, new Property(
                        PropertyKey.Location.PATH,
                        Collections.emptyList(),
                        "email",
                        false,
                        "string",
                        null,
                        false,
                        null
                ),
                3, new Property(
                        PropertyKey.Location.JSON,
                        List.of("user"),
                        "email",
                        false,
                        "integer",
                        null,
                        true,
                        null
                ),
                4, new Property(
                        PropertyKey.Location.COOKIE,
                        Collections.emptyList(),
                        "email",
                        false,
                        "string",
                        "some_format",
                        true,
                        null
                )
        );

        resolutions = adviser.solve(np, new HashSet<>(ops.values()));

        assert resolutions.get(0).equals(Resolution.defaultValueResolution(null));
        assert resolutions.get(1).equals(Resolution.keyResolution(ops.get(1).key));
        assert resolutions.get(2).equals(Resolution.keyResolution(ops.get(2).key));
        assert !resolutions.contains(Resolution.keyResolution(ops.get(3).key));
        assert !resolutions.contains(Resolution.keyResolution(ops.get(4).key));

        np = new Property(
                PropertyKey.Location.QUERY,
                Collections.emptyList(),
                "email",
                true,
                null,
                null,
                true,
                null
        );
        ops = Map.of(
                1, new Property(
                        PropertyKey.Location.QUERY,
                        Collections.emptyList(),
                        "email",
                        true,
                        null,
                        null,
                        false,
                        null
                ),
                2, new Property(
                        PropertyKey.Location.QUERY,
                        Collections.emptyList(),
                        "email",
                        true,
                        "string",
                        null,
                        false,
                        null
                )
        );

        resolutions = adviser.solve(np, new HashSet<>(ops.values()));

        assert resolutions.isEmpty();
    }

    @Test
    void testGetDifferences() {
        PropertyKey k0, k1;
        Set<SimpleResolutionAdviser.Differences> differences;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test0");
        k1 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test1");

        differences = adviser.getDifferences(k0,k1);
        assert differences.contains(SimpleResolutionAdviser.Differences.NAME) && differences.size()==1;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.COOKIE, Collections.emptyList(),"test");

        differences = adviser.getDifferences(k0,k1);
        assert differences.contains(SimpleResolutionAdviser.Differences.LOCATION) && differences.size()==1;

        k0 = new PropertyKey(PropertyKey.Location.JSON, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        differences = adviser.getDifferences(k0,k1);
        assert differences.contains(SimpleResolutionAdviser.Differences.PRECURSORS) && differences.size()==1;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test1");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        differences = adviser.getDifferences(k0,k1);
        assert differences.size()==3;
    }

    @Test
    void testGetDifferencesWeight() {
        PropertyKey k0, k1;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test0");
        k1 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test1");

        assert adviser.getDifferencesWeight(adviser.getDifferences(k0,k1))==1;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.COOKIE, Collections.emptyList(),"test");

        assert adviser.getDifferencesWeight(adviser.getDifferences(k0,k1))==2;

        k0 = new PropertyKey(PropertyKey.Location.JSON, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        assert adviser.getDifferencesWeight(adviser.getDifferences(k0,k1))==2;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        assert adviser.getDifferencesWeight(adviser.getDifferences(k0,k1))==3;

        k0 = new PropertyKey(PropertyKey.Location.JSON, Collections.emptyList(),"test1");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        assert adviser.getDifferencesWeight(adviser.getDifferences(k0,k1))==4;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test1");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        assert adviser.getDifferencesWeight(adviser.getDifferences(k0,k1))==5;
    }
}