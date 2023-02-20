package edu.ucsd.cse110.cse110_project;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ucsd.cse110.socialcompass.Bearing;

public class BearingUnitTest {

    @Test
    public void testSameCoordinates() {
        float angle = Bearing.bearing(0,0,0,0);
        assertEquals(0, angle, 0);
    }

    @Test
    public void testBearingCalc() {
        float angle = Bearing.bearing(0,0,64.98761,123.098765432345612777777777);
        assertEquals(21.34857559745751, angle, 0.00005);
    }

    @Test
    public void testBearingNegative() {
        float angle = Bearing.bearing(88,-110,89,-111);
        assertEquals(359.00030440876378, angle, 0.00005);
    }

    @Test
    public void testOppositeSidesOfGlobe() {
        float angle = Bearing.bearing(20.5937,78.9629,-20.593700, -101.037100);
        assertEquals(270.00000000000000, angle, 0);
    }

    @Test
    public void testUStoIndia() {
        float angle = Bearing.bearing(36.7783, 119.4179,20.5937,78.9629);
        assertEquals(256.59683913306554, angle, 0.00005);
    }
}
