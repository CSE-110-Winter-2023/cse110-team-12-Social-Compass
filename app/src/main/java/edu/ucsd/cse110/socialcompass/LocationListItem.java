package edu.ucsd.cse110.socialcompass;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class LocationListItem {

    public long id = 0;
    public String label, coords;

    public LocationListItem(String label, String coords) {
        this.label = label;
        this.coords = coords;
    }

    public static List<LocationListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<LocationListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "LocationListItem{" +
                "label='" + label + '\'' +
                ", coords=" + coords +
                '}';
    }
}
