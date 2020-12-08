package lib.util.collections.ints;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class IntLinkedList {
    private Node head, tail;
    private int size;

    public int getFirst() {
        return head.value;
    }

    public int getLast() {
        return tail.value;
    }

    public void addFirst(int v) {
        Node nd = new Node(v, null, head);
        if (head != null) {
            head.prev = nd;
        }
        head = nd;
        size++;
    }

    public void addLast(int v) {
        Node nd = new Node(v, tail, null);
        if (tail != null) {
            tail.next = nd;
        }
        tail = nd;
        size++;
    }

    public void add(int v) {
        addLast(v);
    }

    public int removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty.");
        }
        int ret = head.value;
        if (head.next == null) {
            head = null;
        } else {
            head.next.prev = null;
            head = head.next;
        }
        size--;
        return ret;
    }

    public int removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty.");
        }
        int ret = tail.value;
        if (tail.prev == null) {
            tail = null;
        } else {
            tail.prev.next = null;
            tail = tail.prev;
        }
        size--;
        return ret;
    }

    public int remove() {
        return removeLast();
    }

    public void setFirst(int v) {
        if (head == null) {
            throw new NoSuchElementException("List is empty.");
        } else {
            head.value = v;
        }
    }

    public void setLast(int v) {
        if (tail == null) {
            throw new NoSuchElementException("List is empty.");
        } else {
            tail.value = v;
        }
    }

    public int size() {
        return size;
    }

    public AscendingIterator iterator() {
        return new AscendingIterator();
    }

    public DescendingIterator descendingIterator() {
        return new DescendingIterator();
    }

    public IntListIterator listIterator() {
        return new BidirectedIterator(head);
    }

    public IntListIterator descendingListIterator() {
        return new BidirectedIterator(tail);
    }

    public int[] toArray() {
        int[] ret = new int[size];
        PrimitiveIterator.OfInt iter = iterator();
        for (int idx = 0; iter.hasNext(); idx++) {
            ret[idx] = iter.nextInt();
        }
        return ret;
    }

    private abstract static class DirectedIterator implements PrimitiveIterator.OfInt {
        Node it;
        private DirectedIterator(Node nd) {
            this.it = nd;
        }
        public boolean hasNext() {
            return it != null;
        }
        public abstract int nextInt();
        public void remove() {
            if (it.prev != null) it.prev.next = it.next;
            if (it.next != null) it.next.prev = it.prev;
        }
        public void forEachRemaining(IntConsumer action) {
            while (hasNext()) action.accept(nextInt());
        }
        public void forEachRemaining(Consumer<? super Integer> action) {
            while (hasNext()) action.accept(next());
        }
    }

    private class AscendingIterator extends DirectedIterator {
        private AscendingIterator() {super(head);}
        public int nextInt() {
            int ret = it.value;
            it = it.next;
            return ret;
        }
    }

    private class DescendingIterator extends DirectedIterator {
        private DescendingIterator() {super(tail);}
        public int nextInt() {
            int ret = it.value;
            it = it.prev;
            return ret;
        }
    }

    private class BidirectedIterator implements IntListIterator {
        private Node it;
        private int i;
        private BidirectedIterator(Node nd) {
            this.it = nd;
            this.i = nd == head ? 0 : size - 1;
        }
        public boolean hasNext() {
            return it != null;
        }
        public int nextInt() {
            i++;
            int ret = it.value;
            it = it.next;
            return ret;
        }
        public boolean hasPrevious() {
            return it != null && it.prev != null;
        }
        public int previousInt() {
            i--;
            it = it.prev;
            return it.value;
        }
        public void remove() {
            if (it.prev != null) it.prev.next = it.next;
            if (it.next != null) it.next.prev = it.prev;
        }
        public void forEachRemaining(IntConsumer action) {
            while (hasNext()) action.accept(nextInt());
        }
        public void forEachRemaining(Consumer<? super Integer> action) {
            while (hasNext()) action.accept(next());
        }
        public void add(int v) {
            Node nd = null;
            if (it == null) {
                IntLinkedList.this.add(v);
            } else {
                nd = new Node(v, it.prev, it);
                if (it.prev != null) {
                    it.prev.next = nd;
                }
                it.prev = nd;
                size++;
            }
            it = nd;
        }
        public void set(int v) {
            throw new UnsupportedOperationException("Sorry, set() has not been implemented now.");
        }
        public int nextIndex() {
            return i;
        }
        public int previousIndex() {
            return i - 1;
        }
        public Integer next() {
            return nextInt();
        }
        public Integer previous() {
            return previousInt();
        }
        public void add(Integer e) {
            add(e.intValue());
        }
        public void set(Integer e) {
            set(e.intValue());
        }
    }

    private static class Node {
        private int value;
        private Node next, prev;
        public Node(int value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }
}