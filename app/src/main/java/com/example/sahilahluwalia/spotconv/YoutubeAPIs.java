package com.example.sahilahluwalia.spotconv;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
public class YoutubeAPIs {
    public static final String API_KEY = "AIzaSyB-CKLdCOPDsbRh-MEat4brTcumIqFgCIM";

    private static JSONObject getJSONFromString(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }
    public static String getVideos(String phrase, int duration) throws Exception {
        System.out.print("hello nibba");
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
        System.out.print("items:" + items);
        return items.getJSONObject(lowestInd).getJSONObject("id").getString("videoId");

    }

    private static int[] getScores(String vidId, int realMill) throws Exception {
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