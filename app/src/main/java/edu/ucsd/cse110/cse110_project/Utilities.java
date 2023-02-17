package edu.ucsd.cse110.cse110_project;

import java.util.Locale;

public class Utilities {

    static String formatOrientation(float azimuth) {
        float degrees = (float) Math.toDegrees(azimuth);
        if (1 / degrees == Double.POSITIVE_INFINITY) degrees = 180;
        if (degrees < 0) {
            degrees += 360;
        }
        //return String.format( "%.0f degrees from North", degrees);
        return String.format(Locale.US, "%.0f degrees from North", degrees);
    }
}
