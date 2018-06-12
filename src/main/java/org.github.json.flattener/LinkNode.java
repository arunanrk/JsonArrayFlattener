package org.github.json.flattener;

import java.util.ArrayList;

public class LinkNode {
    private LinkNode prev;
    private String nodeName;
    private DataNode dataNode;
    private ArrayList<LinkNode> listOfLinkNodes;
    private int iPtr = 0;

    public LinkNode(String nodeName) {
        this.nodeName = nodeName;
        dataNode = null;
        listOfLinkNodes = new ArrayList<>();
    }

    public LinkNode() {
        dataNode = null;
        listOfLinkNodes = new ArrayList<>();
    }

   /* public String getInstance() {
        return "LinkNode";
    }*/

    public void addLinkNode(LinkNode node) {
        listOfLinkNodes.add(node);
    }

    public void addLinkNode(int index, LinkNode node) {
        if(listOfLinkNodes.size() == index)
            listOfLinkNodes.add(index ,node);
        else {
            String error ="";
            error = "ERROR: List size:" + listOfLinkNodes.size() + "," + nodeName + ":" + node.getNodeName()+"\n";
            LinkNode tNode = this;
            do {
                error = error + tNode.getNodeName()+"<-";
                tNode = tNode.getParent();
            } while((tNode!=null));
            System.out.println(error);
            listOfLinkNodes.add(index ,node);
        }
    }
    public int getSize() {
        return listOfLinkNodes.size();
    }

    public String getNodeName() {
        return nodeName;
    }

    public DataNode getDataNode() {
        return  dataNode;
    }

    public void setDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    public LinkNode getLinkNodeByName(String nodeName) {
        for(LinkNode node: listOfLinkNodes) {
            if(node.nodeName.equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    public LinkNode getLinkNode(KeyIndex keyIndex) {

        if(nodeName.equals(keyIndex.getKey())) {
            if (listOfLinkNodes.size() <= keyIndex.getIndex()) {
                return null;
            }
            return listOfLinkNodes.get(keyIndex.getIndex());
        }
        else {
            if (listOfLinkNodes.size() > 0) {
                LinkNode node = listOfLinkNodes.get(0);
                return node.getLinkNode(keyIndex);
            }
            else
                return null;
        }
    }

    public LinkNode getNextLinkNode() {
        if(listOfLinkNodes.size() > 0 && listOfLinkNodes.size() > iPtr) {
            return listOfLinkNodes.get(iPtr);
        }
        else return null;
    }

    public void addParent(LinkNode node) {
        prev = node;
    }

    public LinkNode getParent() {
        return prev;
    }

    public int getPtr() {
        return iPtr;
    }

    public void incrPtr() {
        iPtr++;
    }

}

