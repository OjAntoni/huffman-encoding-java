package pl.edu.pw.ee.tree;

public class Node {
    public char character;
    public int occurrences;
    private Node leftChild, rightChild;

    public Node(char character, int occurrences) {
        this.character = character;
        this.occurrences = occurrences;
    }

    public Node() {}

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public String toString() {
        return "Node{" +
                "character=" + character +
                ", occurrences=" + occurrences +
                '}';
    }
}
