package lib.datastructure.ints;

import java.util.TreeSet;

import lib.util.Random;

public class IntOrderedSet extends IntOrderedMultiSet {
    @Override
    Node insertKey(Node t, int e) {
        if (count(t, e) > 0) return t;
        return insert(t, leqCount(t, e), e, null);
    }
    @Override
    public void insertKey(int e) {
        root = insertKey(root, e);
    }

    public static void main(String[] args) {
        int n = 100000;
        int m = 10000;
        IntOrderedSet s1 = new IntOrderedSet();
        TreeSet<Integer> s2 = new TreeSet<>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int x = i;//r.nextInt(m);
            checkXor((s1.count(x) > 0) ^ s2.contains(x), i);
            s1.insertKey(x);
            s2.add(x);
            checkXor((s1.count(x) > 0) ^ s2.contains(x), i);
        }
        for (int i = 0; i < n; i++) {
            int x = r.nextInt(m);
            checkXor(s1.count(x) > 0 ^ s2.contains(x), i);
            s1.eraseKey(x);
            s2.remove(x);
            checkXor(s1.count(x) > 0 ^ s2.contains(x), i);
        }
    }

    public static void checkXor(boolean xor, int i) {
        if (xor) {
            System.out.println("Error in " + i);
            throw new RuntimeException();
        }
    }
}