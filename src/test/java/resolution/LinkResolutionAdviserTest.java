package resolution;

import contract.structures.Property;
import contract.structures.PropertyKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resolution.structures.Resolution;

import java.util.*;
import java.util.stream.Collectors;

class LinkResolutionAdviserTest {

    LinkResolutionAdviser adviser;

    @BeforeEach
    void setUp() {
        this.adviser = new LinkResolutionAdviser();
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

        assert resolutions.contains(Resolution.linkResolution(ops.get(1).key));
        assert resolutions.contains(Resolution.linkResolution(ops.get(2).key));
        assert !resolutions.contains(Resolution.linkResolution(ops.get(3).key));
        assert !resolutions.contains(Resolution.linkResolution(ops.get(4).key));

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
        Set<LinkResolutionAdviser.Difference> differences;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test0");
        k1 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test1");

        differences = adviser.getDifferences(k0,k1);
        assert differences.contains(LinkResolutionAdviser.Difference.NAME) && differences.size()==1;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.COOKIE, Collections.emptyList(),"test");

        differences = adviser.getDifferences(k0,k1);
        assert differences.contains(LinkResolutionAdviser.Difference.LOCATION) && differences.size()==1;

        k0 = new PropertyKey(PropertyKey.Location.JSON, Collections.emptyList(),"test");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        differences = adviser.getDifferences(k0,k1);
        assert differences.contains(LinkResolutionAdviser.Difference.PREDECESSOR) && differences.size()==1;

        k0 = new PropertyKey(PropertyKey.Location.HEADER, Collections.emptyList(),"test1");
        k1 = new PropertyKey(PropertyKey.Location.JSON, List.of("test0"),"test");

        differences = adviser.getDifferences(k0,k1);
        assert differences.size()==3;
    }
}