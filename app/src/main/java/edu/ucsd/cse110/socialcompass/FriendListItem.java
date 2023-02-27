package edu.ucsd.cse110.socialcompass;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class FriendListItem {

    public long id; // id for database
    public String name, uid;

    public FriendListItem(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

//    public static List<FriendsListItem> loadJSON(Context context, String path) {
//        try {
//            InputStream input = context.getAssets().open(path);
//            Reader reader = new InputStreamReader(input);
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<FriendsListItem>>(){}.getType();
//            return gson.fromJson(reader, type);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "name='" + name + '\'' +
                ", uid=" + uid +
                '}';
    }
}
