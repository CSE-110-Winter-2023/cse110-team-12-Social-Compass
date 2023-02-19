package edu.ucsd.cse110.cse110_project;

import java.util.Locale;

public class Utilities {

    static String formatOrientation(float azimuth) {
        //return String.format( "%.0f degrees from North", degrees);
        return String.format(Locale.US, "%.0f degrees from North", 360-azimuth);
    }
}
