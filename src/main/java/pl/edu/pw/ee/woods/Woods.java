package pl.edu.pw.ee.woods;

import pl.edu.pw.ee.tree.HaffmanTree;
import pl.edu.pw.ee.tree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Woods {
    private final List<HaffmanTree> trees = new ArrayList<>();

    public Woods(Collection<Node> nodes) {
        for (Node node : nodes) {
            trees.add(new HaffmanTree(node));
        }
        sort();
    }

    public HaffmanTree pullMinTree() {
        return trees.remove(0);
    }

    public void putTree(HaffmanTree tree) {
        if (tree == null) {
            throw new IllegalArgumentException("Provided tree is null");
        }
        trees.add(tree);
        sort();
    }

    public List<HaffmanTree> getTrees() {
        return List.copyOf(trees);
    }

    public boolean hasOneTree() {
        return trees.size() == 1;
    }

    private void sort() {
        trees.sort(Comparator.comparingInt(HaffmanTree::getTotalOccurrences));
    }
}
