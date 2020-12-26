package lib.util;

public class IDManager<T> {
    private final java.util.HashMap<T, Integer> ids = new java.util.HashMap<>();
    public void put(T e, int id) {
        ids.put(e, id);
    }
    public java.util.OptionalInt get(T e) {
        Integer id = ids.get(e);
        return id == null ? java.util.OptionalInt.empty() : java.util.OptionalInt.of(id);
    }
}
