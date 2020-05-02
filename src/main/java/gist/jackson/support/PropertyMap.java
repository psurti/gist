package gist.jackson.support;

import java.util.*;

public class PropertyMap<V> extends AnyDTO implements Map<String, V> {
    private Map<String,V> data = new LinkedHashMap<>();

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return data.get(key);
    }

    @Override
    public V put(String key, V value) {
        return data.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        data.putAll(m);
        data.put("id", (V) getId());
        data.put("name",(V) getName());
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<V> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return data.entrySet();
    }
}
