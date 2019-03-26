package map;

import models.map.Location;
import models.map.Map;
import models.map.Neighbor;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class PathFinderTest {

    @Test
    public void findPath() {
        Map map = MapParser.parse(PathFinder.class.getResource("/data/nodes.csv").getFile(), PathFinder.class.getResource("/data/edges.csv").getFile());

        Location start = map.getLocation("ADEPT00301");
        Location end = map.getLocation("DDEPT00402");
        Stack<Neighbor> path = PathFinder.findPath(map, start, end);
        assertTrue(validatePath(path));
    }

    private boolean validatePath(Stack<Neighbor> path) {
        return true;
    }
}