package lib.util.collections.ints;

public class IntFixedCapacityStack {
    private final int[] stack;
    private int ptr = 0;
    public IntFixedCapacityStack(int maxCapacity) {this.stack = new int[maxCapacity];}
    public void push(int v) {stack[ptr++] = v;}
    public int top() {return stack[ptr - 1];}
    public int topk(int k) {return stack[ptr + ~k];}
    public int pop() {return stack[--ptr];}
    public int size() {return ptr;}
    public boolean hasElem() {return ptr > 0;}
    public boolean isEmpty() {return ptr == 0;}
}
