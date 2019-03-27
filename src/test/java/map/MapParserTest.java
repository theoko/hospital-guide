package map;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapParserTest {


    @Test
    public void parse() {
        Map map = MapParser.parse("/data/nodes.csv","/data/edges.csv");
        assertTrue(true);
    }
}