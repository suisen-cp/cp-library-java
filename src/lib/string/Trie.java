package lib.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trie {
    public static int nodeCount = 0;
    static final ArrayList<Node> nodes = new ArrayList<>();

    final int w;
    final int[] map;
    public final Node root;

    public Trie(int w, int[] charToIntMap) {
        this.w = w;
        this.map = charToIntMap.clone();
        this.root = new Node(null, -1, 0);
    }

    public void addCharArray(char[] s) {
        Node cur = root;
        for (char c : s) {
            cur.midCnt++;
            int t = map[c];
            if (cur.children[t] == null) {
                cur.children[t] = new Node(cur, t, cur.depth + 1);
            }
            cur = cur.children[t];
        }
        cur.midCnt++;
        cur.endCnt++;
    }

    public void addString(String s) { addCharArray(s.toCharArray()); }

    public void addCharArrays(char[][] s) { for (char[] c : s) addCharArray(c); }

    public void addStrings(String[] s) { for (String c : s) addString(c); }

    public Node search(char[] s, int fromIndex, int toIndex) {
        Node cur = root;
        for (int i = fromIndex; i < toIndex; i++) {
            cur = cur.children[map[s[i]]];
            if (cur == null) return null;
        }
        return cur;
    }

    public Node search(char[] s) { return search(s, 0, s.length); }

    public Node search(String s, int fromIndex, int toIndex) {
        Node cur = root;
        for (int i = fromIndex; i < toIndex; i++) {
            cur = cur.children[map[s.charAt(i)]];
            if (cur == null) return null;
        }
        return cur;
    }

    public Node search(String s) { return search(s, 0, s.length()); }

    public Node getNode(int i) { return nodes.get(i); }

    public List<Node> gteNodes() { return nodes; }

    public static int[] lowercaseAlphabetMap() {
        int[] map = new int[256];
        for (char c = 'a'; c <= 'z'; c++) map[c] = c - 'a';
        return map;
    }

    public static int[] uppercaseAlphabetMap() {
        int[] map = new int[256];
        for (char c = 'A'; c <= 'Z'; c++) map[c] = c - 'A';
        return map;
    }

    public static int[] alphabetMap() {
        int[] map = new int[256];
        for (char c = 'A'; c <= 'Z'; c++) map[c] = c - 'A';
        for (char c = 'a'; c <= 'z'; c++) map[c] = c - 'a' + 26;
        return map;
    }

    public static char[] lowercaseAlphabetInv() {
        char[] inv = new char[26];
        for (int i = 0; i < 26; i++) inv[i] = (char) ('a' + i);
        return inv;
    }

    public static char[] uppercaseAlphabetInv() {
        char[] inv = new char[26];
        for (int i = 0; i < 26; i++) inv[i] = (char) ('A' + i);
        return inv;
    }

    public static char[] alphabetInv() {
        char[] inv = new char[52];
        for (int i = 0; i < 26; i++) inv[i] = (char) ('A' + i);
        for (int i = 0; i < 26; i++) inv[i + 26] = (char) ('a' + i);
        return inv;
    }

    public String toString(char[] inv) {
        return root.toString(new char[]{}, inv);
    }

    public class Node {
        public final int nodeId;

        public final int depth;
        public final int character;
        public final Node parent;
        public Node[] children = new Node[w];

        public int midCnt, endCnt;

        Node(Node parent, int character, int depth) {
            this.nodeId = nodeCount++;
            this.parent = parent;
            this.character = character;
            this.depth = depth;
            nodes.add(this);
        }

        public int hashCode() { return nodeId; }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof Node)) return false;
            return ((Node) o).nodeId == nodeId;
        }

        public String toString(char[] prefix, char[] inv) {
            StringBuilder sb = new StringBuilder().append(character < 0 ? '*' : inv[character]);

            int l = sb.length();

            char[] newprefix = Arrays.copyOf(prefix, prefix.length + l + 4);
            Arrays.fill(newprefix, prefix.length, newprefix.length, ' ');
            newprefix[prefix.length + 2] = '|';

            Node firstNode = null, lastNode = null;
            for (Node nd : children) {
                if (nd != null) {
                    if (firstNode == null) {
                        firstNode = nd;
                    }
                    lastNode = nd;
                }
            }

            for (Node nd : children) {
                if (nd == null) continue;
                if (nd == lastNode) newprefix[prefix.length + 2] = ' ';
                if (nd != firstNode) {
                    sb.append('\n').append(newprefix, 0, prefix.length + l).append(" |");
                    sb.append('\n').append(newprefix, 0, prefix.length + l);
                }
                sb.append(" +- ").append(nd.toString(newprefix, inv));
            }
            return sb.toString();
        }
    }
}
