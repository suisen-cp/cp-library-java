package lib.util.collections.longs;

public class LongFixedCapacityQueue {
    private final long[] queue;
    private int head = 0, tail = 0;
    public LongFixedCapacityQueue(int maxPushCount) {this.queue = new long[maxPushCount];}
    public void push(int v) {queue[tail++] = v;}
    public long first() {return queue[head];}
    public long last() {return queue[tail - 1];}
    public long pop() {return queue[--tail];}
    public int size() {return tail - head;}
    public boolean hasElem() {return tail > head;}
    public boolean isEmpty() {return tail == head;}
}
