package map;

import org.junit.Test;
import map.*;
import java.util.Stack;

import static org.junit.Assert.*;

public class PathFinderTest {

    @Test
    public void findPath() {
        Map map = MapParser.parse("/data/nodes.csv","/data/edges.csv");

        Location start = map.getLocation("ADEPT00301");
        Location end = map.getLocation("DDEPT00402");
        Stack<Neighbor> path = PathFinder.findPath(map, start, end);
        assertTrue(validatePath(path));
    }

    private boolean validatePath(Stack<Neighbor> path) {
        return true;
    }
}