package edu.ucsd.cse110.socialcompass.model;

import android.util.Log;

import androidx.annotation.WorkerThread;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FriendAPI {

    private volatile static FriendAPI instance = null;
    private OkHttpClient client;
    // URL for Social Compass server API
    private static final String URL = "https://socialcompass.goto.ucsd.edu/location/";
    // MediaType specification for putFriend
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public FriendAPI() {this.client = new OkHttpClient();}

    public static FriendAPI provide() {
        if (instance == null) {
            instance = new FriendAPI();
        }
        return instance;
    }

    /**
     * Background thread for getting friend location from server
     * @require FriendDao.exists(uid) == true
     */
    @WorkerThread
    public String getFriend(String uid) {

        var request = new Request.Builder()
                .url(URL + uid)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("GET FRIEND", body);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Background thread for getting the response code for a GET request to the server.
     * Called to check that the UID being added by the user is valid.
     */
    @WorkerThread
    public int getFriendCode(String uid) {
        int code = 0;
        var request = new Request.Builder()
                .url(URL + uid)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            code = response.code();
            Log.i("GET FRIEND CODE", String.valueOf(code));
            System.out.println(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Code: " + code);
        return code;
    }

    /**
     * Background thread for putting friend location to server
     * @require FriendDao.exists(friend.uid) == true
     */
    @WorkerThread
    public void putFriend(Friend friend) {

        String uid = friend.uid;
        Log.d("UID",uid);

        var requestBody = RequestBody.create(friend.toJSON(), JSON);
        var request = new Request.Builder()
                .url(URL + uid)
                .put(requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("PUT FRIEND", body);
            System.out.println("Request Body: " + requestBody.toString());
            System.out.println("Response body: " + body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
