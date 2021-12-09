package pl.edu.pw.ee.tree;

import pl.edu.pw.ee.dictionary.Dictionary;

public class HaffmanTree {
    private final int totalOccurrences;
    private final Node head;

    public HaffmanTree(Node head) {
        this.head = head;
        totalOccurrences = head.occurrences;
    }

    public static HaffmanTree merge(HaffmanTree leftTree, HaffmanTree rightTree) {
        Node head = new Node();
        head.occurrences = leftTree.totalOccurrences + rightTree.totalOccurrences;
        head.setLeftChild(leftTree.head);
        head.setRightChild(rightTree.head);
        return new HaffmanTree(head);
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public Dictionary getDictionary() {
        Dictionary dict = new Dictionary();
        if(head.getLeftChild()==null && head.getRightChild() == null){
            dict.put(head.character, "1");
        } else {
            createPrefixes(head, "", dict);
        }
        return dict;
    }

    private void createPrefixes(Node root, String s, Dictionary dictionary) {
        if (root.getLeftChild() == null && root.getRightChild() == null /*&& !root.isBlank*/) {
            dictionary.put(root.character, s);
            return;
        }
        createPrefixes(root.getLeftChild(), s + "1", dictionary);
        createPrefixes(root.getRightChild(), s + "0", dictionary);
    }

    @Override
    public String toString() {
        return "HaffmanTree{" +
                "totalOccurrences=" + totalOccurrences +
                "}\n";
    }
}
