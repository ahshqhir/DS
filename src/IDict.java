import java.util.Iterator;

public interface IDict<K, V> extends Iterator<IDict.Entry<K,V>>, Iterable<IDict.Entry<K,V>> {
    class Entry<K,V> {
        private final K key;
        private final V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() { return key; }
        public V getValue() { return value; }
    }
    void add(K key, V value);
    void setK(K key, V value);
    void set(Object o, V value);
    V getK(K key);
    V get(Object o);
    K getKeyV(V value);
    K getKey(Object o);
    Object getIndexK(K key);
    Object getIndexV(V value);
    void remove(Object o);
    void removeK(K key);
    void removeV(V value);
    int getLength();
    default V search(String name) {
        throw  new UnsupportedOperationException("search");
    }
}
