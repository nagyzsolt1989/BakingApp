package com.nagy.zsolt.bakingapp.api;

import org.json.JSONArray;

public interface FetchDataListener {
    void onFetchComplete(JSONArray data);

    void onFetchFailure(String msg);

    void onFetchStart();
}

