package lib.util;

import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class Tuple {
    public static final Tuple EMPTY = new Tuple0();
    public abstract <T> T get(int index);
    public abstract int size();
    @SuppressWarnings("SameReturnValue")
    public static Tuple of() {
        return EMPTY;
    }
    public static Tuple of(Object obj0) {
        return new Tuple1(obj0);
    }
    public static Tuple of(Object obj0, Object obj1) {
        return new Tuple2(obj0, obj1);
    }
    public static Tuple of(Object obj0, Object obj1, Object obj2) {
        return new Tuple3(obj0, obj1, obj2);
    }
    public static Tuple of(Object obj0, Object obj1, Object obj2, Object obj3) {
        return new Tuple4(obj0, obj1, obj2, obj3);
    }
    public static Tuple of(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4) {
        return new Tuple5(obj0, obj1, obj2, obj3, obj4);
    }
    public static Tuple of(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        return new Tuple6(obj0, obj1, obj2, obj3, obj4, obj5);
    }
    public static Tuple of(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        return new Tuple7(obj0, obj1, obj2, obj3, obj4, obj5, obj6);
    }
    public static Tuple of(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        return new Tuple8(obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7);
    }
    public static Tuple of(Object... objs) {
        return new TupleN(objs);
    }
    public static Tuple copyOfRange(Tuple t, int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > t.size()) throw new IndexOutOfBoundsException();
        final int length = endIndex - beginIndex;
        if (length > 8) {
            TupleN tn = (TupleN) t;
            Object[] objs = new Object[length];
            System.arraycopy(tn.objs, beginIndex, objs, 0, length);
            return new TupleN(objs);
        }
        switch (length) {
            case 0: return of();
            case 1: return of(t.get(beginIndex + 0));
            case 2: return of(t.get(beginIndex + 0), t.get(beginIndex + 1));
            case 3: return of(t.get(beginIndex + 0), t.get(beginIndex + 1), t.get(beginIndex + 2));
            case 4: return of(t.get(beginIndex + 0), t.get(beginIndex + 1), t.get(beginIndex + 2), t.get(beginIndex + 3));
            case 5: return of(t.get(beginIndex + 0), t.get(beginIndex + 1), t.get(beginIndex + 2), t.get(beginIndex + 3),
                              t.get(beginIndex + 4));
            case 6: return of(t.get(beginIndex + 0), t.get(beginIndex + 1), t.get(beginIndex + 2), t.get(beginIndex + 3),
                              t.get(beginIndex + 4), t.get(beginIndex + 5));
            case 7: return of(t.get(beginIndex + 0), t.get(beginIndex + 1), t.get(beginIndex + 2), t.get(beginIndex + 3),
                              t.get(beginIndex + 4), t.get(beginIndex + 5), t.get(beginIndex + 6));
            case 8: return of(t.get(beginIndex + 0), t.get(beginIndex + 1), t.get(beginIndex + 2), t.get(beginIndex + 3),
                              t.get(beginIndex + 4), t.get(beginIndex + 5), t.get(beginIndex + 6), t.get(beginIndex + 7));
            default: throw new IllegalArgumentException();
        }
    }
    public static Tuple copyOf(Tuple t) {
        return copyOfRange(t, 0, t.size());
    }
    private static class Tuple0 extends Tuple {
        @Override public <T> T get(int index) {
            switch (index) {
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 0;
        }
        @Override public String toString() {
            return "()";
        }
        @Override public boolean equals(Object o) {
            return o == EMPTY;
        }
        @Override public int hashCode() {
            return 0;
        }
    }
    private static class Tuple1 extends Tuple {
        final Object obj0;
        private Tuple1(Object obj0) {
            this.obj0 = obj0;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 1;
        }
        @Override public String toString() {
            return "(" + obj0 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple1) {
                Tuple1 t = (Tuple1) o;
                if (obj0 == null) return t.obj0 == null;
                return obj0.equals(t.obj0);
            }
            return false;
        }
        @Override public int hashCode() {
            return obj0.hashCode();
        }
    }
    private static class Tuple2 extends Tuple {
        final Object obj0, obj1;
        private Tuple2(Object obj0, Object obj1) {
            this.obj0 = obj0; this.obj1 = obj1;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 2;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple2) {
                Tuple2 t = (Tuple2) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            return hash;
        }
    }
    private static class Tuple3 extends Tuple {
        final Object obj0, obj1, obj2;
        private Tuple3(Object obj0, Object obj1, Object obj2) {
            this.obj0 = obj0; this.obj1 = obj1; this.obj2 = obj2;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                case 2: return (T) obj2;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 3;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + "," + obj2 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple3) {
                Tuple3 t = (Tuple3) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                eq &= Objects.equals(obj2, t.obj2);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            hash = hash * 31 + (obj2 == null ? 0 : obj2.hashCode());
            return hash;
        }
    }
    private static class Tuple4 extends Tuple {
        final Object obj0, obj1, obj2, obj3;
        private Tuple4(Object obj0, Object obj1, Object obj2, Object obj3) {
            this.obj0 = obj0; this.obj1 = obj1; this.obj2 = obj2; this.obj3 = obj3;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                case 2: return (T) obj2;
                case 3: return (T) obj3;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 4;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + "," + obj2 + "," + obj3 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple4) {
                Tuple4 t = (Tuple4) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                eq &= Objects.equals(obj2, t.obj2);
                eq &= Objects.equals(obj3, t.obj3);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            hash = hash * 31 + (obj2 == null ? 0 : obj2.hashCode());
            hash = hash * 31 + (obj3 == null ? 0 : obj3.hashCode());
            return hash;
        }
    }
    private static class Tuple5 extends Tuple {
        final Object obj0, obj1, obj2, obj3, obj4;
        private Tuple5(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4) {
            this.obj0 = obj0; this.obj1 = obj1; this.obj2 = obj2; this.obj3 = obj3;
            this.obj4 = obj4;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                case 2: return (T) obj2;
                case 3: return (T) obj3;
                case 4: return (T) obj4;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 5;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + "," + obj2 + "," + obj3 + "," + obj4 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple5) {
                Tuple5 t = (Tuple5) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                eq &= Objects.equals(obj2, t.obj2);
                eq &= Objects.equals(obj3, t.obj3);
                eq &= Objects.equals(obj4, t.obj4);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            hash = hash * 31 + (obj2 == null ? 0 : obj2.hashCode());
            hash = hash * 31 + (obj3 == null ? 0 : obj3.hashCode());
            hash = hash * 31 + (obj4 == null ? 0 : obj4.hashCode());
            return hash;
        }
    }
    private static class Tuple6 extends Tuple {
        final Object obj0, obj1, obj2, obj3, obj4, obj5;
        private Tuple6(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
            this.obj0 = obj0; this.obj1 = obj1; this.obj2 = obj2; this.obj3 = obj3;
            this.obj4 = obj4; this.obj5 = obj5;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                case 2: return (T) obj2;
                case 3: return (T) obj3;
                case 4: return (T) obj4;
                case 5: return (T) obj5;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 6;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + "," + obj2 + "," + obj3 + "," + obj4 + "," + obj5 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple6) {
                Tuple6 t = (Tuple6) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                eq &= Objects.equals(obj2, t.obj2);
                eq &= Objects.equals(obj3, t.obj3);
                eq &= Objects.equals(obj4, t.obj4);
                eq &= Objects.equals(obj5, t.obj5);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            hash = hash * 31 + (obj2 == null ? 0 : obj2.hashCode());
            hash = hash * 31 + (obj3 == null ? 0 : obj3.hashCode());
            hash = hash * 31 + (obj4 == null ? 0 : obj4.hashCode());
            hash = hash * 31 + (obj5 == null ? 0 : obj5.hashCode());
            return hash;
        }
    }
    private static class Tuple7 extends Tuple {
        final Object obj0, obj1, obj2, obj3, obj4, obj5, obj6;
        private Tuple7(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
            this.obj0 = obj0; this.obj1 = obj1; this.obj2 = obj2; this.obj3 = obj3;
            this.obj4 = obj4; this.obj5 = obj5; this.obj6 = obj6;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                case 2: return (T) obj2;
                case 3: return (T) obj3;
                case 4: return (T) obj4;
                case 5: return (T) obj5;
                case 6: return (T) obj6;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 7;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + "," + obj2 + "," + obj3 + "," + obj4 + "," + obj5 + "," + obj6 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple7) {
                Tuple7 t = (Tuple7) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                eq &= Objects.equals(obj2, t.obj2);
                eq &= Objects.equals(obj3, t.obj3);
                eq &= Objects.equals(obj4, t.obj4);
                eq &= Objects.equals(obj5, t.obj5);
                eq &= Objects.equals(obj6, t.obj6);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            hash = hash * 31 + (obj2 == null ? 0 : obj2.hashCode());
            hash = hash * 31 + (obj3 == null ? 0 : obj3.hashCode());
            hash = hash * 31 + (obj4 == null ? 0 : obj4.hashCode());
            hash = hash * 31 + (obj5 == null ? 0 : obj5.hashCode());
            hash = hash * 31 + (obj6 == null ? 0 : obj6.hashCode());
            return hash;
        }
    }
    private static class Tuple8 extends Tuple {
        final Object obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7;
        private Tuple8(Object obj0, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
            this.obj0 = obj0; this.obj1 = obj1; this.obj2 = obj2; this.obj3 = obj3;
            this.obj4 = obj4; this.obj5 = obj5; this.obj6 = obj6; this.obj7 = obj7;
        }
        @Override public <T> T get(int index) {
            switch (index) {
                case 0: return (T) obj0;
                case 1: return (T) obj1;
                case 2: return (T) obj2;
                case 3: return (T) obj3;
                case 4: return (T) obj4;
                case 5: return (T) obj5;
                case 6: return (T) obj6;
                case 7: return (T) obj7;
                default: throw new IndexOutOfBoundsException();
            }
        }
        @Override public int size() {
            return 8;
        }
        @Override public String toString() {
            return "(" + obj0 + "," + obj1 + "," + obj2 + "," + obj3 + "," + obj4 + "," + obj5 + "," + obj6 + "," + obj7 + ")";
        }
        @Override public boolean equals(Object o) {
            if (o instanceof Tuple8) {
                Tuple8 t = (Tuple8) o;
                boolean eq = true;
                eq &= Objects.equals(obj0, t.obj0);
                eq &= Objects.equals(obj1, t.obj1);
                eq &= Objects.equals(obj2, t.obj2);
                eq &= Objects.equals(obj3, t.obj3);
                eq &= Objects.equals(obj4, t.obj4);
                eq &= Objects.equals(obj5, t.obj5);
                eq &= Objects.equals(obj6, t.obj6);
                eq &= Objects.equals(obj7, t.obj7);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            hash = hash * 31 + (obj0 == null ? 0 : obj0.hashCode());
            hash = hash * 31 + (obj1 == null ? 0 : obj1.hashCode());
            hash = hash * 31 + (obj2 == null ? 0 : obj2.hashCode());
            hash = hash * 31 + (obj3 == null ? 0 : obj3.hashCode());
            hash = hash * 31 + (obj4 == null ? 0 : obj4.hashCode());
            hash = hash * 31 + (obj5 == null ? 0 : obj5.hashCode());
            hash = hash * 31 + (obj6 == null ? 0 : obj6.hashCode());
            hash = hash * 31 + (obj7 == null ? 0 : obj7.hashCode());
            return hash;
        }
    }
    private static class TupleN extends Tuple {
        final Object[] objs;
        final int length;
        private TupleN(Object... objs) {
            this.objs = objs;
            this.length = objs.length;
        }
        @Override public <T> T get(int index) {
            return (T) objs[index];
        }
        @Override public int size() {
            return length;
        }
        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for (int i = 0; i < length - 1; i++) sb.append(objs[i]).append(',');
            sb.append(objs[length - 1]).append(')');
            return sb.toString();
        }
        @Override public boolean equals(Object o) {
            if (o instanceof TupleN) {
                TupleN t = (TupleN) o;
                if (t.length != length) return false;
                boolean eq = true;
                for (int i = 0; i < length; i++) eq &= Objects.equals(objs[i], t.objs[i]);
                return eq;
            }
            return false;
        }
        @Override public int hashCode() {
            int hash = 1;
            for (int i = 0; i < length; i++) hash = hash * 31 + (objs[i] == null ? 0 : objs[i].hashCode());
            return hash;
        }
    }
}