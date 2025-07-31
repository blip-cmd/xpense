package app.util;


// A simple TreeMap implementation using a Binary Search Tree (BST)
public class SimpleTreeMap<K extends Comparable<K>, V> {

    // Inner class representing each node in the tree
    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root; // Root of the BST

    // Insert or update a key-value pair
    public void put(K key, V value) {
        root = insert(root, key, value);
    }

    private Node insert(Node node, K key, V value) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value; // update existing key
        }
        return node;
    }

    // Get the value for a specific key
    public V get(K key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        return null; // key not found
    }

    // Print all key-value pairs in sorted order
    public void printInOrder() {
        inOrder(root);
    }

    private void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.key + " => " + node.value);
            inOrder(node.right);
        }
    }

    // Delete a key from the map
    public void remove(K key) {
        root = delete(root, key);
    }

    private Node delete(Node node, K key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            // Node to be deleted found

            // Case 1: No child
            if (node.left == null && node.right == null) return null;

            // Case 2: One child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Case 3: Two children - get the smallest in the right subtree
            Node minNode = getMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = delete(node.right, minNode.key);
        }

        return node;
    }

    // Helper to find the smallest node in a subtree
    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }
}
