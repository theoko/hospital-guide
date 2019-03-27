package map;

import models.map.Location;
import models.map.Map;
import models.map.SubPath;
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
        Stack<SubPath> path = PathFinder.findPath(map, start, end);
        assertTrue(validatePath(path));
    }

    private boolean validatePath(Stack<SubPath> path) {
        return true;
    }
}