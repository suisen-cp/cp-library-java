package lib.string;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class PatriciaTrie {
    static class Node {
        int size;
        int length;
        char initial;
        HashMap<Character, Node> branch;

        private Node(char initial) {
            this(initial, 1);
        }

        private Node(char initial, int length) {
            this.size = 0;
            this.length = length;
            this.initial = initial;
            this.branch = new HashMap<>();
        }
        
        void compress() {
            while (this.branch.size() == 1) {
                for (var b : this.branch.values()) {
                    this.branch = b.branch;
                }
                this.length++;
            }
        }
    }

    Node root;

    public PatriciaTrie(String[] strings) {
        this.root = new PatriciaTrie.Node('^', 0);
        for (var s : strings) this.add(s);
        this.compress();
    }

    private void add(String s) {
        int l = s.length();
        Node t = this.root;
        t.size++;
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            if (!t.branch.containsKey(c)) {
                t.branch.put(c, new PatriciaTrie.Node(c));
            }
            t = t.branch.get(c);
            t.size++;
        }
    }
    
    private void compress() {
        ArrayDeque<Node> stack = new ArrayDeque<>();
        stack.addLast(this.root);
        while (stack.size() > 0) {
            Node node = stack.removeLast();
            node.compress();
            stack.addAll(node.branch.values());
        }
    }

    public ArrayList<Node> search(String s) {
        Node t = this.root;
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(t);
        int i = t.length;
        int n = s.length();
        while (i < n) {
            char c = s.charAt(i);
            t = t.branch.get(c);
            i += t.length;
            nodes.add(t);
        }
        return nodes;
    }
}
