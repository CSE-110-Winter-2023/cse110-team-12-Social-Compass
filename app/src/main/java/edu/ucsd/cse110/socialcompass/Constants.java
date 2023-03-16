package edu.ucsd.cse110.socialcompass;

import java.util.HashMap;

public class Constants {

    // Zones and their radius
    public static final int ZONE0 = 0;
    public static final int ZONE1 = 1;
    public static final int ZONE2 = 10;
    public static final int ZONE3 = 500;
    public static final int ZONE4 = 1000;

    // HashMap to map to the first zone, the inner most one
    // Divide each zone into five smaller circles with increasing radius
    // How the increments are calculated: e.g. (ZONE1 - ZONE0) / 5, (1 - 0) / 5 = 0.2
    public static final HashMap<Double, Integer> HASHMAP_ZONE1 = new HashMap<>() {{
        put(0.0, 25);
        put(0.2, 49);
        put(0.4, 74);
        put(0.6, 98);
        put(0.8, 123);
    }};

    public static final HashMap<Double, Integer> HASHMAP_ZONE2 = new HashMap<Double, Integer>() {{
        put(1.0, 147);
        put(2.8, 172);
        put(4.6, 196);
        put(6.4, 221);
        put(8.2, 245);
    }};

    public static final HashMap<Double, Integer> HASHMAP_ZONE3 = new HashMap<Double, Integer>() {{
        put(10.0, 270);
        put(108.0, 294);
        put(206.0, 319);
        put(304.0, 343);
        put(402.0, 368);
    }};

    public static final HashMap<Double, Integer> HASHMAP_ZONE4 = new HashMap<Double, Integer>() {{
        put(500.0, 392);
        put(600.0, 417);
        put(700.0, 441);
        put(800.0, 466);
        put(900.0, 489);
    }};

    public static final double MILES_CONVERSION = 0.00062137119;
}
