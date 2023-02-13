package edu.ucsd.cse110.socialcompass;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

public class TestInput {
    private boolean compareArrays(double[] a, double[] b) {
        return a[0] == b[0] && a[1] == b[1];
    }

    /**
     * Tests for parseCoordinates
     */
    @Test
    public void test1() {
        Assert.assertTrue(compareArrays(new double[]{75, 179.99},
                Objects.requireNonNull(Utilities.parseCoordinates("75, 179.99"))));
    }

    @Test
    public void test2() {
        Assert.assertTrue(compareArrays(new double[]{90.0, -147.45},
                Objects.requireNonNull(Utilities.parseCoordinates("90.0, -147.45"))));
    }

    @Test
    public void test3() {
        Assert.assertTrue(compareArrays(new double[]{77.11112223331, 149.99999999},
                Objects.requireNonNull(Utilities.parseCoordinates("77.11112223331, 149.99999999"))));
    }

    @Test
    public void test4() {
        Assert.assertTrue(compareArrays(new double[]{90, 179.99},
                Objects.requireNonNull(Utilities.parseCoordinates("90, 179.99"))));
    }

    @Test
    public void test5() {
        Assert.assertTrue(compareArrays(new double[]{-90.00000, -180.0000},
                Objects.requireNonNull(Utilities.parseCoordinates("-90.00000, -180.0000"))));
    }

    @Test
    public void test6() {
        Assert.assertNull(Utilities.parseCoordinates("75, 280"));
    }

    @Test
    public void test7() {
        Assert.assertNull(Utilities.parseCoordinates("190.0, -147.45"));
    }

    @Test
    public void test8() {
        Assert.assertNull(Utilities.parseCoordinates("77.11112223331, 249.9999999"));
    }

    @Test
    public void test9() {
        Assert.assertNull(Utilities.parseCoordinates("90, 180.2"));
    }

    @Test
    public void test10() {
        Assert.assertNull(Utilities.parseCoordinates(("90.1, 180.01")));
    }

    @Test
    public void test11() {
        Assert.assertNull(Utilities.parseCoordinates("-90.00001, -180.0001"));
    }

    @Test
    public void test12() {
        Assert.assertNull(Utilities.parseCoordinates("-100.00001, -180.0001"));
    }
}