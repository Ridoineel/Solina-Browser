package com.example.demo1;

import java.util.HashMap;
import java.util.Map;

public class SearchEngine {
    private String name;
    private String url;
    public static Map<String, SearchEngine> engines = new HashMap<>();

    public SearchEngine(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    static {
        engines.put("google", new SearchEngine("Google", "https://www.google.com/search?channel=fs&q="));
    }
}
