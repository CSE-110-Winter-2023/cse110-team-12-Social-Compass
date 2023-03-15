package edu.ucsd.cse110.socialcompass;

import java.util.HashMap;

public class Constants {
    // increment by 39 (except for ZONE1_2 since it is too close to the location_icon)
    public static final int ZONE1_1 = 0; // 0
    public static final int ZONE1_2 = 45; // (0,0.2] 39
    public static final int ZONE1_3 = 100;  // (0.2,0.4] 78
    public static final int ZONE1_4 = 155; // (0.4,0.6] 117
    public static final int ZONE1_5 = 210; // (0.6,0.8] 156

    public static final int ZONE2_1 = 260; // (0.8, 1] 195
    public static final int ZONE2_2 = 320; // (1.0,2.8] 234
    public static final int ZONE2_3 = 370; // (2.8,4.6] 273
    public static final int ZONE2_4 = 425; // (4.6,6.4] 312
    public static final int ZONE2_5 = 480; // (6.4, 8.2] 351

    public static final int ZONE3_1 = 530; // (8.2, 10] 390

    //hash maps to map to the inner zones
    public static final HashMap<Double, Integer> HASHMAPZONE1 = new HashMap<>() {{
        put(0.0, 45);
        put(0.2, 100);
        put(0.4, 155);
        put(0.6, 210);
        put(0.8, 260);
    }};

    public static final HashMap<Double, Integer> HASHMAPZONE2 = new HashMap<Double, Integer>() {{
        put(1.0, 320);
        put(2.8, 370);
        put(4.6, 425);
        put(6.4, 480);
        put(8.2, 530);
    }};

    public static final HashMap<Double, Integer> HASHMAPZONE3 = new HashMap<Double, Integer>() {{

    }};

    public static final HashMap<Double, Integer> HASHMAPZONE4 = new HashMap<Double, Integer>() {{

    }};

    public static final double MILES_CONVERSION = 0.00062137119;
}
