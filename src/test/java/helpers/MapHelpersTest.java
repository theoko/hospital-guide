package helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapHelpersTest {

    @Test
    public void clamp() {
        assertTrue(MapHelpers.clamp(1.0, 2.0, 6.0) == 2.0);
    }

    @Test
    public void imageViewToImage() {
    }
}