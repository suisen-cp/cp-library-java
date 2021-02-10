package lib.util.collections.longs;

public class LongFixedCapacityStack {
    private final long[] stack;
    private int ptr = 0;
    public LongFixedCapacityStack(int maxCapacity) {this.stack = new long[maxCapacity];}
    public void push(long v) {stack[ptr++] = v;}
    public long top() {return stack[ptr - 1];}
    public long topk(int k) {return stack[ptr + ~k];}
    public long pop() {return stack[--ptr];}
    public int size() {return ptr;}
    public boolean hasElem() {return ptr > 0;}
    public boolean isEmpty() {return ptr == 0;}
}
