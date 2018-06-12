package org.github.json.flattener;

public class KeyIndex {
    private String key;
    private int index;

    @Override
    public String toString() {
        return "KeyIndex{" +
                "key='" + key + '\'' +
                ", index=" + index +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
