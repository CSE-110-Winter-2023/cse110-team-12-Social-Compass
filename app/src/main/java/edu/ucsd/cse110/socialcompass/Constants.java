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

    // Zone 1 after zooming in once
    public static final HashMap<Double, Integer> HASHMAP_ZONE1_2 = new HashMap<Double, Integer>() {{
        put(0.0, 33);
        put(0.2, 65);
        put(0.4, 98);
        put(0.6, 131);
        put(0.8, 163);
    }};

    public static final HashMap<Double, Integer> HASHMAP_ZONE2_2 = new HashMap<Double, Integer>() {{
        put(1.0, 196);
        put(2.8, 229);
        put(4.6, 261);
        put(6.4, 294);
        put(8.2, 327);
    }};

    public static final HashMap<Double, Integer> HASHMAP_ZONE3_2 = new HashMap<Double, Integer>() {{
        put(10.0, 359);
        put(108.0, 392);
        put(206.0, 425);
        put(304.0, 457);
        put(402.0, 489);
    }};

    // Zone 1 after zooming in third times
    public static final HashMap<Double, Integer> HASHMAP_ZONE1_3 = new HashMap<Double, Integer>() {{
        put(0.0, 49);
        put(0.2, 98);
        put(0.4, 147);
        put(0.6, 196);
        put(0.8, 245);
    }};

    public static final HashMap<Double, Integer> HASHMAP_ZONE2_3 = new HashMap<Double, Integer>() {{
        put(1.0, 294);
        put(2.8, 343);
        put(4.6, 392);
        put(6.4, 441);
        put(8.2, 489);
    }};

    // Zone 1 after zooming in fourth times
    public static final HashMap<Double, Integer> HASHMAP_ZONE1_4 = new HashMap<Double, Integer>() {{
        put(0.0, 98);
        put(0.2, 196);
        put(0.4, 294);
        put(0.6, 392);
        put(0.8, 489);
    }};

    public static final double MILES_CONVERSION = 0.00062137119;
}
