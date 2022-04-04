package com.example.demo1;


import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class AnchorPaneCust  extends javafx.scene.layout.AnchorPane {
    private Map<String, Node> children = new HashMap<>();

    public void addChild(Node child, String name) {
        getChildren().add(child);
        children.put(name, child);
    }

    public Node getChild(String name) {
        return children.get(name);
    }
}
