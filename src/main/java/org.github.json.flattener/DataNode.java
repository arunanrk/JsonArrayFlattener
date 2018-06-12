package org.github.json.flattener;

import java.util.ArrayList;

public class DataNode {
    private ArrayList<KeyPair> dataKeyValue;
    public DataNode() {
        dataKeyValue = new ArrayList<>();
    }
    public String getInstance() {
        return "DataNode";
    }
    public void addKeyPair(KeyPair key) {
        dataKeyValue.add(key);
    }

    public ArrayList<KeyPair> getDataKey() {
        return dataKeyValue;
    }
}
