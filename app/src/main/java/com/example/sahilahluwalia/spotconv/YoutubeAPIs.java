package com.example.sahilahluwalia.spotconv;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeAPIs {
    private static final String API_KEY = "AIzaSyB-CKLdCOPDsbRh-MEat4brTcumIqFgCIM";

    private final static OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    JSONObject myJSON;

    public void setMyJson(JSONObject json)
    {
        this.myJSON = json;
    }

    public JSONObject getMyJSON()
    {
        return this.myJSON;
    }

    public JSONObject getJSONFromString(String url) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Failed to fetch data!1" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    myJSON =  new JSONObject(response.body().string());
                    if (myJSON == null)
                    {
                        System.out.println("BIG PROBLEMO");
                    }
                    setMyJson(myJSON);
                } catch (JSONException e) {
                    System.out.println("Failed to parse data: " + e.getMessage());
                }
            }
        });

        System.out.println(myJSON);

        return getMyJSON();
    }
    public String getVideos(String phrase, int duration) throws Exception {
        phrase = phrase.trim();
        phrase = phrase.replace(" ", "+");
        JSONObject search = getJSONFromString("https://www.googleapis.com/youtube/v3/search?q=" + phrase + "&maxResults=30&type=video&part=snippet&key=AIzaSyB-CKLdCOPDsbRh-MEat4brTcumIqFgCIM");
        JSONArray items = search.getJSONArray("items");
        String vidList = "";
        for(int i = 0; i < 30; i++) {
            vidList += items.getJSONObject(i).getJSONObject("id").getString("videoId") + ",";
        }
        vidList = vidList.substring(0,vidList.length() - 1);
        int[] scores = getScores(vidList, duration);
        int lowestInd = 0;
        int lowest = scores[0];
        for(int i = 1; i < scores.length; i++) {
            if(scores[i] < lowest) {
                lowest = scores[i];
                lowestInd = i;
            }
        }
        return items.getJSONObject(lowestInd).getJSONObject("id").getString("videoId");

    }

    private int[] getScores(String vidId, int realMill) throws Exception {
        JSONObject vidResponse = getJSONFromString("https://www.googleapis.com/youtube/v3/videos?id=" + vidId + "&part=contentDetails,statistics&key=" + API_KEY);
        JSONArray items = vidResponse.getJSONArray("items");
        int[] scores = new int[vidResponse.getJSONObject("pageInfo").getInt("totalResults")];

        for(int i = 0; i < scores.length; i++) {
            JSONObject myItems = items.getJSONObject(i);
            String myTime = myItems.getJSONObject("contentDetails").getString("duration");
            myTime = myTime.substring(2);
            int hours = 0,min = 0,sec = 0;
            if(myTime.contains("H")) {
                String h = myTime.substring(0, myTime.indexOf('H'));
                myTime = myTime.substring(myTime.indexOf('H') + 1);
                hours = Integer.parseInt(h);
            }
            if(myTime.contains("M")) {
                String m = myTime.substring(0, myTime.indexOf('M'));
                myTime = myTime.substring(myTime.indexOf('M') + 1);
                min = Integer.parseInt(m);
            }
            if(myTime.contains("S")) {
                String s = myTime.substring(0, myTime.indexOf('S'));
                myTime = myTime.substring(myTime.indexOf('S') + 1);
                sec = Integer.parseInt(s);
            }
            int mill = 3600000*hours + 60000*min + 1000*sec;
            JSONObject stats = myItems.getJSONObject("statistics");
            scores[i] = (int)(Math.abs(realMill - mill));
        }


        return scores;
    }
}