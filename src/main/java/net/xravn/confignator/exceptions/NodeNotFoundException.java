package net.xravn.confignator.exceptions;

public class NodeNotFoundException extends Exception {
    String key;

    public NodeNotFoundException(String key, String message) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
