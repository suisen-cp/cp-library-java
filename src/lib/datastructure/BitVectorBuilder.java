package lib.datastructure;

public class BitVectorBuilder {
    static final int LARGE_BLOCK_SIZE = 1 << 8;
    static final int SMALL_BLOCK_SIZE = 1 << 3;
    final byte[] Data;
    final int[] LargeBlocks;
    final byte[] SmallBlocks;
    public BitVectorBuilder(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be positive.");
        }
        int n = (length + Byte.SIZE - 1) / Byte.SIZE;
        this.Data = new byte[n];
        this.SmallBlocks = new byte[n];
        int m = (length + LARGE_BLOCK_SIZE - 1) / LARGE_BLOCK_SIZE;
        this.LargeBlocks = new int[m];
    }
    public void set(int idx, boolean b) {
        int q = idx >> 3;
        int r = idx & 0b0111;
        if (b) {
            Data[q] |= 1 << r;
        } else {
            Data[q] &= ~(1 << r);
        }
    }
    public BitVector build() {
        int b = SmallBlocks.length;
        for (int i = 0; i < b; i++) {
            if ((i & 0x1f) != 0) {
                SmallBlocks[i] = (byte) (SmallBlocks[i - 1] + popcount(Data[i - 1]));
            }
        }
        int c = LargeBlocks.length;
        for (int i = 1; i < c; i++) {
            LargeBlocks[i] = LargeBlocks[i - 1] + (0xff & SmallBlocks[(i << 5) - 1]) + popcount(Data[(i << 5) - 1]);
        }
        return new BitVector(Data, LargeBlocks, SmallBlocks);
    }
    static int popcount(byte b) {
        int x = b & 0xff;
        x = (x & 0x55) + ((x & 0xaa) >>> 1);
        x = (x & 0x33) + ((x & 0xcc) >>> 2);
        x = (x & 0x0f) + ((x & 0xf0) >>> 4);
        return x;
    }

    /** utility methods */

    public static BitVector fromBinaryString(String binaryString) {
        int length = binaryString.length();
        BitVectorBuilder builder = new BitVectorBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.set(i, binaryString.charAt(i) == '1');
        }
        return builder.build();
    }
    public static BitVector fromArray(long[] bits) {
        int length = bits.length * Long.SIZE;
        BitVectorBuilder builder = new BitVectorBuilder(length);
        for (int i = 0; i < bits.length; i++) {
            for (int j = 0; j < Long.SIZE; j++) {
                builder.set(i * Long.SIZE + j, ((bits[i] >> j) & 1) == 1);
            }
        }
        return builder.build();
    }
    public static BitVector fromArray(byte[] bits) {
        int length = bits.length * Long.SIZE;
        BitVectorBuilder builder = new BitVectorBuilder(length);
        System.arraycopy(bits, 0, builder.Data, 0, bits.length);
        return builder.build();
    }
    public static BitVector fromBitSet(java.util.BitSet bitset) {
        int length = bitset.length();
        BitVectorBuilder builder = new BitVectorBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.set(i, bitset.get(i));
        }
        return builder.build();
    }
    public static BitVector fromBigInteger(java.math.BigInteger bigint) {
        int length = bigint.bitLength();
        BitVectorBuilder builder = new BitVectorBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.set(i, bigint.testBit(i));
        }
        return builder.build();
    }
}