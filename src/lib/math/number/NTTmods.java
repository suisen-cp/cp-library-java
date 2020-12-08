package lib.math.number;

public enum NTTmods {
    MOD_167772161 (167772161,  3),
    MOD_469762049 (469762049,  3),
    MOD_754974721 (754974721, 11),
    MOD_924844033 (924844033,  5),
    MOD_998244353 (998244353,  3),
    MOD_1012924417(1012924417, 5),
    MOD_1224736769(1224736769, 3);
    private final long MOD, PRIMITIVE_ROOT;
    NTTmods(long mod, long primitiveRoot) {
        this.MOD = mod; this.PRIMITIVE_ROOT = primitiveRoot;
    }
    public long getMod() {return MOD;}
    public long getPrimitiveRoot() {return PRIMITIVE_ROOT;}
}