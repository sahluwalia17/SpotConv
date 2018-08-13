package com.example.sahilahluwalia.spotconv;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class song {
    String URI;
    String name;
    String thequery;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;


    public void setQuery(String query)
    {
        this.thequery = query;
    }

    public String getURI(String link) {
        int lastBackslash = link.lastIndexOf('/') + 1;
        int questionMark = link.indexOf('?');

        String URI;
        if (questionMark != -1)
        {
            URI = link.substring(lastBackslash,questionMark);
        }
        else
        {
            URI = link.substring(lastBackslash);
        }
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getQuery(String URI, String authcode)
    {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/tracks/"+URI)
                .addHeader("Authorization","Bearer " + authcode)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Failed to fetch data!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    //uncomment if need to see response JSON
                    //System.out.println(jsonObject.toString(3));
                    String songName = jsonObject.getString("name");
                    String songArtist = jsonObject.getJSONObject("album")
                            .getJSONArray("artists")
                            .getJSONObject(0)
                            .getString("name");
                    String query = songName + " " + songArtist + " mp3";
                    setQuery(query);
                } catch (JSONException e) {
                    System.out.println("Failed to parse data: " + e);
                }
            }
        });
        return this.thequery;
    }
}