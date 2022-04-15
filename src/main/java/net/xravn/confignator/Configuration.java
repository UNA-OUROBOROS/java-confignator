package net.xravn.confignator;

import java.util.HashMap;

import net.xravn.confignator.exceptions.NodeNotFoundException;

/**
 * class that provides a way to get and modify configuration values.
 */
public class Configuration {
    private String variant = "default";
    private Node root = new Node();

    public Configuration(String variant) {
        this.variant = variant;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) getNode(key).getValue();
    }

    public void set(String key, Object value) throws NodeNotFoundException {
        Node node = getNode(key);
        if (node == null) {
            throw new NodeNotFoundException(key, "Node not found");
        } else {
            node.setValue(value);
        }
    }

    public void add(String key) {
        addValue(key, null);
    }

    public boolean renameKey(String oldKey, String newKey) {
        Node node = getNode(oldKey);
        if (node == null) {
            return false;
        }
        node.setKey(newKey);
        return true;
    }

    public boolean changeValue(String key, Object value) {
        Node node = getNode(key);
        if (node == null) {
            return false;
        }
        node.setValue(value);
        return true;
    }

    /**
     * tries to add a new value to the configuration, if it exists replace it.
     * 
     * @param key   the key of the value
     * @param value the value
     */
    public void addValue(String key, Object value) {
        Node node = getNode(key);
        if (node == null) {
            root.addNode(key, new Node("", value));
        } else {
            node.setValue(value);
        }
    }

    public boolean removeValue(String key) {
        try {
            getNode(key).removeNode(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets a node from the configuration with the following syntax:
     * a.b.c.d
     * 
     * @param keyPath the path to the node
     * 
     * @return the node
     */
    private Node getNode(String keyPath) {
        return root.getNode(keyPath);
    }

    private class Node {
        private String key;
        private Object value;
        private HashMap<String, Node> children = new HashMap<String, Node>();

        public Node(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Node(String key) {
            this.key = key;
        }

        public Node() {
        }

        public String getKey() {
            return key;
        }

        public String setKey(String key) {
            this.key = key;
            return key;
        }

        @SuppressWarnings("unchecked")
        public <T> T getValue() {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                return null;
            }
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Node getNode(String key) {
            String nodeKey = key;
            // get the first substring before the first dot
            if (key.contains(".")) {
                nodeKey = key.substring(0, key.indexOf("."));
            }
            // search for the node
            Node node = children.get(nodeKey);
            // if the node is not found, return null
            if (node == null) {
                return null;
            }
            // if the node is found check if it is the value we are looking for
            if (key.equals(node.getKey())) {
                return node;
            }
            // if the node is not the value we are looking for, check if it is a
            // subnode
            return node.getNode(key);
        }

        public void addNode(String path, Node node) {
            // check if the path contains a dot
            if (path.indexOf(".") == -1) {
                // if not, add the node to the children if it does not exist
                if (!children.containsKey(path)) {
                    children.put(path, node);
                    // rename the node key
                    node.setKey(path);
                } else {
                    throw new IllegalArgumentException("Node already exists");
                }
            } else {
                // we need to find the node
                // get the first substring before the first dot
                String nodeKey = path.substring(0, path.indexOf("."));
                Node parentNode = children.get(nodeKey);
                // if the node is not found, create it
                if (parentNode == null) {
                    // strip the path of the first word
                    String newPath = path.substring(path.indexOf(".") + 1);
                    // create the node
                    parentNode = new Node(nodeKey);
                    // add the node to the children recursively
                    parentNode.addNode(newPath, node);
                } else {
                    // if the node is found add recursively
                    parentNode.addNode(path.substring(path.indexOf(".") + 1), node);
                }
            }
        }

        public void removeNode(String path) {
            // check if the path contains a dot
            if (path.indexOf(".") == -1) {
                // if not, remove the node from the children if it exists
                if (children.containsKey(path)) {
                    children.remove(path);
                } else {
                    throw new IllegalArgumentException("Node does not exist");
                }
            } else {
                // we need to find the node
                // get the first substring before the first dot
                String nodeKey = path.substring(0, path.indexOf("."));
                Node parentNode = children.get(nodeKey);
                // if the node is not found, throw an exception
                if (parentNode == null) {
                    throw new IllegalArgumentException("Node does not exist");
                } else {
                    // if the node is found remove recursively
                    parentNode.removeNode(path.substring(path.indexOf(".") + 1));
                }
            }
        }

    }

}
