package edu.ucsd.cse110.socialcompass;

public class Bearing {

    // Calculates the bearing angle (0-360 degrees) from two points using latitude and longitude.
    // If the result is negative, return the corresponding positive angle
    protected static float bearing(double lat1, double lon1, double lat2, double lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2)
                - Math.sin(latitude1) * Math.cos(latitude2)
                * Math.cos(longDiff);

        return (float) ((Math.toDegrees(Math.atan2(y, x))+360)%360);
    }

}



