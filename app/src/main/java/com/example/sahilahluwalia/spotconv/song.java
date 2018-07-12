package com.example.sahilahluwalia.spotconv;

public class song {
    String URI;
    String coverLink;
    String name;

    public String getURI(String link) {
        int lastBackslash = link.lastIndexOf('/');
        return link.substring(lastBackslash);
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
}