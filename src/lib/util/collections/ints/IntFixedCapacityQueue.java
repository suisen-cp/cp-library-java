package lib.util.collections.ints;

public class IntFixedCapacityQueue {
    private final int[] queue;
    private int head = 0, tail = 0;
    public IntFixedCapacityQueue(int maxPushCount) {this.queue = new int[maxPushCount];}
    public void push(int v) {queue[tail++] = v;}
    public int first() {return queue[head];}
    public int last() {return queue[tail - 1];}
    public int pop() {return queue[--tail];}
    public int size() {return tail - head;}
    public boolean hasElem() {return tail > head;}
    public boolean isEmpty() {return tail == head;}
}
