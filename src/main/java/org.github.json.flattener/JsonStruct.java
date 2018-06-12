package org.github.json.flattener;

import org.json.JSONObject;

public class JsonStruct {
    private String key;
    private JSONObject value;

    JsonStruct(String key, JSONObject value){
        this.key = key;
        this.value = value;
    }
    public String getKey(){
        return this.key;
    }

    public JSONObject getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "";
    }
}
