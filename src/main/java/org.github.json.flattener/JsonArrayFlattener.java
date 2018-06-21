package org.github.json.flattener;

import com.github.wnameless.json.flattener.JsonFlattener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonArrayFlattener {
    private LinkNode rootNode;

    private JsonArrayFlattener() { rootNode = null;}

    public static List<String> getFlattenedJson(String json) throws Exception {
        JsonArrayFlattener tree = new JsonArrayFlattener();
        Map<String, Object> jsonMap = JsonFlattener.flattenAsMap(json);
        jsonMap.forEach((k, v) -> {
            if (v==null) tree.createDataStructure(k, "null");
            else tree.createDataStructure(k, v.toString());
        });
        return tree.getFlattened(null);
    }

    private List<String> getFlattened(JsonStruct hStruct) throws Exception {
        List<String> jsonList = new ArrayList<>();
        while (rootNode.getSize() != rootNode.getPtr()) {
            LinkNode tempNode = rootNode;
            LinkNode prvNode = null;
            String nodeName = "";
            List<JsonStruct> list = new ArrayList<>();
            if( hStruct != null) list.add(hStruct);
            while (tempNode != null) {
                DataNode dataNode = tempNode.getDataNode();
                JSONObject dataJson = new JSONObject();
                if (dataNode != null) {
                    dataNode.getDataKey().forEach(keyPair -> {
                        try {
                            dataJson.put(keyPair.keyName, keyPair.keyValue);
                        } catch (JSONException exp) { exp.printStackTrace(); }
                    });
                }
                String tempNodeName = getNodeName(tempNode);
                nodeName = tempNodeName.equals("") ? nodeName : tempNodeName;
                if (dataNode == null) {
                    if (getNodeName(tempNode).equals(nodeName))
                        list.add(new JsonStruct(nodeName, null));
                } else {
                    if (list.size() > 0
                            && list.get(list.size() - 1).getKey().equals(nodeName)
                            && list.get(list.size() - 1).getValue() == null)
                        list.remove(list.size() - 1);
                    list.add(new JsonStruct(nodeName, dataJson));
                }
                prvNode = tempNode;
                tempNode = tempNode.getNextLinkNode();
            }
            reversePtrSet(prvNode);
            jsonList.add(formJson(list));
        }
        return jsonList;
    }

    private void reversePtrSet(LinkNode prvNode) {
        LinkNode tmpNode = prvNode;
        while (tmpNode != null) {
            tmpNode = tmpNode.getParent();
            if(tmpNode == null) break;
            if(tmpNode.getSize() > tmpNode.getPtr()) {
                tmpNode.incrPtr();
                if(tmpNode.getSize() != tmpNode.getPtr()) break;
            }
        }
    }

    private String formJson(List<JsonStruct> list) throws JSONException {
        JSONObject json = new JSONObject();
        int listSize = list.size() - 1;
        String key = "";
        for (int i = listSize ; i >= 0; i--) {
            JsonStruct struct = list.get(i);
            if (key.equals(struct.getKey()))
                json = mergeJSONObjects( json, struct.getValue());
            else if(struct.getKey().equals("_h"))
                json = mergeJSONObjects(json, new JSONObject().putOpt(struct.getKey(), struct.getValue()));
            else if (i == listSize )
                json = new JSONObject().putOpt(struct.getKey(), struct.getValue());
            else
                json = new JSONObject().put(struct.getKey(), mergeJSONObjects(json, struct.getValue()));
            key = struct.getKey();
        }
        return json.toString();
    }

    private static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = null;
        try {
            if(json1 == null || json1.length() == 0)
                return json2;
            else if(json2 == null ||json2.length() == 0)
                return json1;
            mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
            for (String key : JSONObject.getNames(json2)) {
                mergedJSON.put(key, json2.get(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException("JSON Exception" + e);
        }
        return mergedJSON;
    }

    public String getNodeName(LinkNode node) {
        String nodeName = "";
        try  { Integer.parseInt(node.getNodeName()); }
        catch (NumberFormatException nfe) { nodeName = node.getNodeName(); }
        return nodeName;
    }

    private void createDataStructure(String fullKey, String value) {
        LinkNode tempNode = rootNode;
        String[] splitKeys = getSplitKeys(fullKey);
        for(int i = 0; i < splitKeys.length; i++) {
            if(splitKeys.length != (i + 1)){
                KeyIndex keyIndex = getKeyIndexForArray(splitKeys[i]);
                if(keyIndex != null) {
                    if(tempNode == null) {
                        tempNode = new LinkNode(keyIndex.getKey());
                        rootNode = tempNode;
                        tempNode = createLinkNode(tempNode, keyIndex.getIndex());
                    }
                    else if(tempNode.getLinkNode(keyIndex) != null) {
                        tempNode = tempNode.getLinkNode(keyIndex);
                    }
                    else {
                        if(!tempNode.getNodeName().equals(keyIndex.getKey())) {
                            tempNode = linkTheNode(tempNode, keyIndex.getKey());
                        }
                        tempNode = createLinkNode(tempNode, keyIndex.getIndex());
                    }
                }
                else {
                    if(tempNode == null) {
                        tempNode = new LinkNode(splitKeys[i]);
                        rootNode = tempNode;
                    }
                    else tempNode = linkTheNode(tempNode, splitKeys[i]);
                }
            }
            else {
                KeyPair keyPair = createKeyPair(splitKeys[i], value);
                DataNode dataNode = tempNode.getDataNode();
                if(dataNode == null) dataNode = new DataNode();
                dataNode.addKeyPair(keyPair);
                tempNode.setDataNode(dataNode);
            }
        }
    }

    private String[] getSplitKeys(String fullKey) {
        String[] splitKeys = null;
        if(fullKey.contains("[\\\"")) {
            String[] a = fullKey.split("\\[\\\\\"");
            splitKeys = new String[a[0].split("\\.").length + 1];
            String[] a1 = a[0].split("\\.");
            for(int i = 0; i < a1.length; i++) {
                splitKeys[i] = a1[i];
            }
            splitKeys[splitKeys.length - 1] = a[1].split("\\\\\"]")[0];
        }
        else
            splitKeys = fullKey.split("\\.");
        return splitKeys;
    }

    private LinkNode createLinkNode(LinkNode tempNode, int index) {
        LinkNode nextNode = new LinkNode(new Integer(index).toString());
        tempNode.addLinkNode(index, nextNode);
        nextNode.addParent(tempNode);
        return nextNode;
    }

    private LinkNode linkTheNode(LinkNode tempNode, String key) {
        LinkNode innerNode = tempNode.getLinkNodeByName(key);
        if (innerNode == null) {
            innerNode = new LinkNode(key);
            tempNode.addLinkNode(innerNode);
            innerNode.addParent(tempNode);
        }
        return innerNode;
    }

    private KeyPair createKeyPair(String key, String value ){
        KeyPair keyPair = new KeyPair();
        keyPair.keyName = key;
        keyPair.keyValue = value;
        return keyPair;
    }

    private KeyIndex getKeyIndexForArray(String key) {
        if(key.contains("[") && key.contains("]")) {
            KeyIndex keyIndex = new KeyIndex();
            int index = Integer.parseInt(
                    key.substring(
                            key.indexOf("[", 0) + 1, key.lastIndexOf("]")
                    )
            );
            keyIndex.setIndex(index);
            keyIndex.setKey(key.substring(0 , key.lastIndexOf("[")));
            return keyIndex;
        }
        return null;
    }
}
