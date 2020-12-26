package lib.datastructure.ints;

public class IntOrderedSet extends IntOrderedMultiSet {
    @Override public void add(int e) {if (!contains(e)) super.add(e);}
    @Override public int count(int key) {return contains(key) ? 1 : 0;}
}