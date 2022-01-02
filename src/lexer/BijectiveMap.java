package lexer;

import java.util.HashMap;
import java.util.Map;

public class BijectiveMap<K, V> {

    private Map<K, V> map = new HashMap<K, V>();
    private Map<V, K> inverseMap = new HashMap<V, K>();

    public void put(K key, V value) {
        map.put(key, value);
        inverseMap.put(value, key);
    }

    public V getValue(K key) {
        return map.get(key);
    }

    public K getKey(V value) {
        return inverseMap.get(value);
    }

    @SuppressWarnings("unchecked")
    public RemovedKey<K, V> removeKeyValuePairByKey(K key) {
        if (key == null || !map.containsKey(key)) {
            return RemovedKey.EMPTY_ENTRY;
        }

        V value = map.remove(key);
        inverseMap.remove(value);

        RemovedKey<K, V> removedKey = new RemovedKey<>(key, value);
        return removedKey;

    }

    @SuppressWarnings("unchecked")
    public RemovedKey<K, V> removeKeyValuePairByValue(V value) {
        if (value == null || !inverseMap.containsKey(value)) {
            return RemovedKey.EMPTY_ENTRY;
        }

        K key = inverseMap.remove(value);
        map.remove(key);
        RemovedKey<K, V> removedKey = new RemovedKey<>(key, value);
        return removedKey;

    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return inverseMap.containsKey(value);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int size() {
        return map.size();
    }

    public static class RemovedKey<K, V> {
        K key;
        V value;

        @SuppressWarnings("rawtypes")
        public static final RemovedKey EMPTY_ENTRY = new RemovedKey();

        private RemovedKey() {
            key = null;
            value = null;
        }

        public RemovedKey(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @SuppressWarnings("rawtypes")
        public static RemovedKey getEmptyEntry() {
            return EMPTY_ENTRY;
        }
    }

}