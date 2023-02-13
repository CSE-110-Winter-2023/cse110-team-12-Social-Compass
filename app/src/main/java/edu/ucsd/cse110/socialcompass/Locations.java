package edu.ucsd.cse110.socialcompass;

import android.util.Pair;

import java.util.HashMap;

public class Locations {
    // HashMap containing (label, coordinates) pairs inputted by the User
    private HashMap<String, Pair<Double,Double>> userInputHashSet;

    public Locations() {
        this.userInputHashSet = new HashMap<>();
    }

    public boolean addLocation(String name, Pair<Double, Double> coordinates) {
        return this.userInputHashSet.put(name, coordinates) != null;
    }

    public HashMap<String, Pair<Double, Double>> getLocations() {
        return this.userInputHashSet;
    }

}
