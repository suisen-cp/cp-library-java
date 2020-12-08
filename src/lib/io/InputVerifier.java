package lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Pattern;

public class InputVerifier implements AutoCloseable {

    public static final Pattern ALPHABET_REGEX   = Pattern.compile("^[a-zA-Z]+$");
    public static final Pattern LOWER_CASE_REGEX = Pattern.compile("^[a-z]+$");
    public static final Pattern UPPER_CASE_REGEX = Pattern.compile("^[A-Z]+$");
    public static final Pattern NUMERIC_REGEX    = Pattern.compile("^[0-9]+$");

    private static final int EOF_CODE_POINT = -1;

    private static final int BUFFER_SIZE = 1 << 13;

    private final InputStream in;

    private final byte[] buf = new byte[BUFFER_SIZE];
    private int bufptr = 0;
    private int buflen = 0;

    private final byte[] undo = new byte[1024];
    private int undoptr = 0;
    private int undolen = 0;

    private int line = 1;

    public InputVerifier(InputStream in) {
        Objects.requireNonNull(in);
        this.in = in;
    }

    public InputVerifier() {
        this(System.in);
    }

    private boolean hasNextByte() {
        if (undoptr < undolen) return true;
        undoptr = undolen = 0;
        if (bufptr < buflen) return true;
        bufptr = 0;
        try {
            buflen = in.read(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buflen > 0;
    }

    private int nextByte() {
        int b;
        if (undoptr < undolen) {
            b = undo[undoptr++];
        } else if (hasNextByte()) {
            b = buf[bufptr++];
        } else {
            b = EOF_CODE_POINT;
        }
        if (isEoln(b)) line++;
        return b;
    }

    private void undo(int b) {
        if (isEoln(b)) line--;
        if (!isEof(b)) undo[undolen++] = (byte) b;
    }

    public char nextChar() {
        return (char) nextByte();
    }
    
    public void nextEof() {
        int b = nextByte();
        if (!isEof(b)) {
            undo(b);
            throw new InputVerificationException(line, "Eof", toReadableString(b));
        }
    }

    public void nextEoln() {
        int b = nextByte();
        if (isCarriageReturn(b)) {
            b = nextByte();
            undo('\r'); undo(b);
            if (isEoln(b)) {
                throw new InputVerificationException(line, "Eoln (LF)", "Eoln (CRLF)");
            } else {
                throw new InputVerificationException(line, "Eoln (LF)", "Eoln (CR)");
            }
        }
        if (!isEoln(b)) {
            undo(b);
            throw new InputVerificationException(line, "Eoln", toReadableString(b));
        }
    }

    public void nextSpace() {
        int b = nextByte();
        if (!isSpace(b)) {
            undo(b);
            throw new InputVerificationException(line, "Space", toReadableString(b));
        }
    }

    public char nextChar(Pattern regex) {
        int b = nextByte();
        if (isEof(b) || !regex.matcher(String.valueOf((char) b)).matches()) {
            undo(b);
            throw new InputVerificationException(line, String.format("char (matches with %s)", regex.toString()), toReadableString(b));
        }
        return (char) b;
    }

    public char nextChar(char expected) {
        int b = nextByte();
        if (b != expected) {
            undo(b);
            throw new InputVerificationException(
                line, 
                "code point " + (int) expected + " = " + toReadableString(expected), 
                "code point " + b + " = " + toReadableString(b)
            );
        }
        return (char) b;
    }

    public String nextLine() {
        if (!hasNextByte()) {
            throw new InputVerificationException(line, "Line", "Eof");
        }
        StringBuilder sb = new StringBuilder();
        for (int b = nextByte(); !isEoln(b); b = nextByte()) {
            if (isCarriageReturn(b)) {
                b = nextByte();
                undo('\r'); undo(b);
                if (isEoln(b)) {
                    throw new InputVerificationException(line, "Eoln (LF)", "Eoln (CRLF)");
                } else {
                    throw new InputVerificationException(line, "Eoln (LF)", "Eoln (CR)");
                }
            }
            sb.appendCodePoint(b);
        }
        return sb.toString();
    }

    public String nextToken() {
        StringBuilder sb = new StringBuilder();
        int b = nextByte();
        for (;isPrintableAsciiCharacter(b); b = nextByte()) {
            sb.appendCodePoint(b);
        }
        undo(b);
        if (sb.length() == 0) {
            throw new InputVerificationException(line, "Token", toReadableString(b));
        }
        return sb.toString();
    }

    public String nextToken(int expectedLength) {
        String token = nextToken();
        int actualLength = token.length();
        if (actualLength != expectedLength) {
            throw new InputVerificationException(line, String.format("Token with the length %d", expectedLength), String.format("Token with the length %d", actualLength));
        }
        return token;
    }

    public String nextToken(int minLengthInclusive, int maxLengthInclusive) {
        if (minLengthInclusive > maxLengthInclusive) {
            throw new IllegalArgumentException("Empty range.");
        }
        String token = nextToken();
        int length = token.length();
        if (length < minLengthInclusive || length > maxLengthInclusive) {
            throw new InputVerificationException(line, String.format("Token with the length from %d to %d (both inclusive)", minLengthInclusive, maxLengthInclusive), String.format("Token with the length %d", length));
        }
        return token;
    }

    public String nextToken(Pattern regexPattern) {
        String token = nextToken();
        if (!regexPattern.matcher(token).matches()) {
            throw new InputVerificationException(line, String.format("token that matches with %s.", regexPattern.toString()), token);
        }
        return token;
    }

    public String nextToken(Pattern regexPattern, int minLengthInclusive, int maxLengthInclusive) {
        String token = nextToken(minLengthInclusive, maxLengthInclusive);
        if (!regexPattern.matcher(token).matches()) {
            throw new InputVerificationException(line, String.format("token that matches with %s.", regexPattern.toString()), token);
        }
        return token;
    }

    public String[] nextTokens(int size, char delimiter) {
        String[] tokens = new String[size];
        for (int i = 0; i < size; i++) {
            tokens[i] = nextToken();
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return tokens;
    }

    public String[] nextTokens(int size, int minLengthInclusive, int maxLengthInclusive, char delimiter) {
        String[] tokens = new String[size];
        for (int i = 0; i < size; i++) {
            tokens[i] = nextToken(minLengthInclusive, maxLengthInclusive);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return tokens;
    }

    public String[] nextTokens(int size, Pattern regex, char delimiter) {
        String[] tokens = new String[size];
        for (int i = 0; i < size; i++) {
            tokens[i] = nextToken(regex);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return tokens;
    }

    public String[] nextTokens(int size, Pattern regex, int minLengthInclusive, int maxLengthInclusive, char delimiter) {
        String[] tokens = new String[size];
        for (int i = 0; i < size; i++) {
            tokens[i] = nextToken(regex, minLengthInclusive, maxLengthInclusive);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return tokens;
    }

    public long nextLong() {
        boolean isNegetive = false;
        long n = 0;
        int b = nextByte();
        if (b == '-') {
            isNegetive = true;
            b = nextByte();
        }
        if (!isDigit(b)) {
            undo(b);
            throw new InputVerificationException(line, "long", toReadableString(b));
        }
        if (b == '0') {
            b = nextByte();
            undo(b);
            if (isDigit(b)) {
                throw new InputVerificationException(line, "long", "0" + toReadableString(b));
            } else if (!isPrintableAsciiCharacter(b)) {
                return 0;
            } else {
                throw new InputVerificationException(line, "long", "0" + toReadableString(b));
            }
        }
        while (true) {
            if (isDigit(b)) {
                // long : -9223372036854775808 ~ 9223372036854775807
                if (n < 922337203685477580L) {
                    n = n * 10 + b - '0';
                } else if (n == 922337203685477580L) {
                    if (isNegetive) {
                        if (b > '8') {
                            undo(b);
                            throw new InputVerificationException(line, "long", "big integer");
                        } else {
                            n = -n * 10 - (b - '0');
                        }
                    } else {
                        if (b > '7') {
                            undo(b);
                            throw new InputVerificationException(line, "long", "big integer");
                        } else {
                            n = n * 10 + b - '0';
                        }
                    }
                    b = nextByte();
                    undo(b);
                    if (isDigit(b)) {
                        throw new InputVerificationException(line, "long", "big integer");
                    } else if (!isPrintableAsciiCharacter(b)) {
                        return n;
                    } else {
                        throw new InputVerificationException(line, "long", toReadableString(b));
                    }
                } else {
                    throw new InputVerificationException(line, "long", "big integer");
                }
            } else if (!isPrintableAsciiCharacter(b)) {
                undo(b);
                return isNegetive ? -n : n;
            } else {
                undo(b);
                throw new InputVerificationException(line, "long", toReadableString(b));
            }
            b = nextByte();
        }
    }

    public long nextLong(long minInclusive, long maxInclusive) {
        if (minInclusive > maxInclusive) {
            throw new IllegalArgumentException("Empty range.");
        }
        long v = nextLong();
        if (v < minInclusive || v > maxInclusive) {
            throw new InputVerificationException(line, String.format("long in [%d, %d]", minInclusive, maxInclusive), String.valueOf(v));
        }
        return v;
    }

    public long[] nextLongs(int size, long minInclusive, long maxInclusive, char delimiter) {
        long[] longs = new long[size];
        for (int i = 0; i < size; i++) {
            longs[i] = nextLong(minInclusive, maxInclusive);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return longs;
    }

    public int nextInt() {
        long n = nextLong();
        try {
            return Math.toIntExact(n);
        } catch (ArithmeticException e) {
            throw new InputVerificationException(line, "int", String.format("long %d", n));
        }
    }

    public int nextInt(int minInclusive, int maxInclusive) {
        if (minInclusive > maxInclusive) {
            throw new IllegalArgumentException("Empty range.");
        }
        int v = nextInt();
        if (minInclusive > v || v > maxInclusive) {
            throw new InputVerificationException(line, String.format("int in [%d, %d]", minInclusive, maxInclusive), String.valueOf(v));
        }
        return v;
    }

    public int[] nextInts(int size, int minInclusive, int maxInclusive, char delimiter) {
        int[] ints = new int[size];
        for (int i = 0; i < size; i++) {
            ints[i] = nextInt(minInclusive, maxInclusive);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return ints;
    }

    private double nextDoubleStrict(int minAfterPointDigitCountInclusive, int maxAfterPointDigitCountInclusive) {
        if (minAfterPointDigitCountInclusive > maxAfterPointDigitCountInclusive) {
            throw new IllegalArgumentException("Empty Range");
        }
        if (minAfterPointDigitCountInclusive < 0) {
            throw new IllegalArgumentException("The minimum value of the digits after the decimal point must be non-negative.");
        }
        StringBuilder sb = new StringBuilder();
        boolean isNegetive = false;
        int b = nextByte();
        if (b == '-') {
            isNegetive = true;
            b = nextByte();
        }
        if (b == '0') {
            sb.appendCodePoint(b);
            b = nextByte();
            if (isDigit(b)) {
                undo(b);
                throw new InputVerificationException(line, "double", sb.toString() + toReadableString(b));
            }
        } else if (isDigit(b)) {
            do {
                sb.appendCodePoint(b);
                b = nextByte();
            } while (isDigit(b));
        } else {
            undo(b);
            throw new InputVerificationException(line, "double", sb.toString() + toReadableString(b));
        }
        boolean point = false;
        int afterPointDigitCount = 0;
        if (b == '.') {
            point = true;
            do {
                sb.appendCodePoint(b);
                b = nextByte();
                afterPointDigitCount++;
            } while (isDigit(b));
            afterPointDigitCount--;
        }
        undo(b);
        if (isPrintableAsciiCharacter(b)) {
            throw new InputVerificationException(line, "double", sb.toString() + toReadableString(b));
        }
        if (afterPointDigitCount < minAfterPointDigitCountInclusive || afterPointDigitCount > maxAfterPointDigitCountInclusive) {
            throw new InputVerificationException(line, String.format("a double value that has [%d, %d] digits after the decimal point", minAfterPointDigitCountInclusive, maxAfterPointDigitCountInclusive), sb.toString());
        }
        int i = sb.length() - 1;
        double d = 0;
        if (point) {
            while (true) {
                char c = sb.charAt(i--);
                if (c == '.') break;
                d = (d + c - '0') / 10.;
            }
        }
        double pow = 1.;
        for (int j = i; j >= 0; j--) {
            d += pow * (sb.charAt(j) - '0');
            pow *= 10;
        }
        return isNegetive ? -d : d;
    }

    public double nextDouble() {
        return nextDoubleStrict(0, Integer.MAX_VALUE);
    }

    public double nextDouble(double minInclusive, double maxInclusive) {
        if (minInclusive > maxInclusive) {
            throw new IllegalArgumentException("Empty range.");
        }
        double v = nextDouble();
        if (v < minInclusive || v > maxInclusive) {
            throw new InputVerificationException(line, String.format("double in [%f, %f]", minInclusive, maxInclusive), String.valueOf(v));
        }
        return v;
    }

    public double nextDoubleStrict(double minInclusive, double maxInclusive, int minAfterPointDigitCountInclusive, int maxAfterPointDigitCountInclusive) {
        if (minInclusive > maxInclusive || minAfterPointDigitCountInclusive > maxAfterPointDigitCountInclusive) {
            throw new IllegalArgumentException("Empty range.");
        }
        double v = nextDoubleStrict(minAfterPointDigitCountInclusive, maxAfterPointDigitCountInclusive);
        if (v < minInclusive || v > maxInclusive) {
            throw new InputVerificationException(line, String.format("double in [%f, %f]", minInclusive, maxInclusive), String.valueOf(v));
        }
        return v;
    }

    public double[] nextDoubles(int size, double minInclusive, double maxInclusive, char delimiter) {
        double[] doubles = new double[size];
        for (int i = 0; i < size; i++) {
            doubles[i] = nextDouble(minInclusive, maxInclusive);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return doubles;
    }

    public double[] nextDoublesStrict(int size, double minInclusive, double maxInclusive, int minAfterPointDigitCountInclusive, int maxAfterPointDigitCountInclusive, char delimiter) {
        double[] doubles = new double[size];
        for (int i = 0; i < size; i++) {
            doubles[i] = nextDoubleStrict(minInclusive, maxInclusive, minAfterPointDigitCountInclusive, maxAfterPointDigitCountInclusive);
            if (i < size - 1) {
                int b = nextByte();
                if (b != delimiter) {
                    undo(b);
                    throw new InputVerificationException(line, toReadableString(delimiter), toReadableString(b));
                }
            }
        }
        return doubles;
    }

    private static String toReadableString(int codePoint) {
        if (codePoint == EOF_CODE_POINT) {
            return "Eof";
        }
        if (isEoln(codePoint)) {
            return "Eoln";
        }
        if (Character.isWhitespace(codePoint)) {
            return "white space";
        }
        if (Character.isISOControl(codePoint)) {
            return "control character";
        }
        if (!Character.isDefined(codePoint)) {
            return "undefined";
        }
        return Character.toString(codePoint);
    }

    private static boolean isEof(int codePoint) {
        return codePoint == EOF_CODE_POINT;
    }

    private static boolean isEoln(int codePoint) {
        return codePoint == '\n';
    }

    private static boolean isCarriageReturn(int codePoint) {
        return codePoint == '\r';
    }

    private static boolean isSpace(int codePoint) {
        return codePoint == ' ';
    }

    private static boolean isDigit(int codePoint) {
        return '0' <= codePoint && codePoint <= '9';
    }

    private static boolean isPrintableAsciiCharacter(int codePoint) {
        return 32 < codePoint && codePoint < 127;
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}