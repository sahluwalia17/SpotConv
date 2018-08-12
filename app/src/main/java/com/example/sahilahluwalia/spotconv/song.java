package com.example.sahilahluwalia.spotconv;

public class song {
    String URI;
    String coverLink;
    String name;

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
}