import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDict<K,V> implements IDict<K, V> {
    private Class<K> clazzK;
    private Class<V> clazzV;
    private K[] keys;
    private V[] values;
    private int length = 0;
    private int IteratorIndex = 0;

    private void resize() {
        K[] newK = (K[]) Array.newInstance(clazzK, 2 * length);
        V[] newV = (V[]) Array.newInstance(clazzV, 2 * length);
        for (int i = 0; i < length; i++) {
            newK[i] = keys[i];
            newV[i] = values[i];
        }
        keys = newK;
        values = newV;
    }

    @Override
    public void add(K key, V value) {
        if (keys == null) {
            clazzK = (Class<K>) key.getClass();
            keys = (K[]) Array.newInstance(clazzK, 8);
            clazzV = (Class<V>) value.getClass();
            values = (V[]) Array.newInstance(clazzV, 8);
        } else if (length == keys.length)
            resize();
        keys[length] = key;
        values[length] = value;
        length++;
    }

    @Override
    public void setK(K key, V value) {
        set(getIndexK(key), value);
    }

    @Override
    public void set(Object o, V value) {
        values[(int) o] = value;
    }

    @Override
    public V getK(K key) {
        for (int i = 0; i < length; i++)
            if (key.equals(keys[i]))
                return values[i];
        return null;
    }

    @Override
    public V get(Object o) {
        int i = (int) o;
        if (i >= length || i < 0)
            throw new IndexOutOfBoundsException("index " + i + " out of bound for length " + length);
        return values[i];
    }

    @Override
    public K getKeyV(V value) {
        for (int i = 0; i < length; i++)
            if (value.equals(values[i]))
                return keys[i];
        return null;
    }

    @Override
    public K getKey(Object o) {
        int i = (int) o;
        if (i >= length || i < 0)
            throw new IndexOutOfBoundsException("index " + i + " out of bound for length " + length);
        return keys[i];
    }

    @Override
    public Integer getIndexK(K key) {
        for (int i = 0; i < length; i++)
            if (key.equals(keys[i]))
                return i;
        return -1;
    }

    @Override
    public Integer getIndexV(V value) {
        for (int i = 0; i < length; i++)
            if (value.equals(values[i]))
                return i;
        return -1;
    }

    @Override
    public void remove(Object o) {
        int index = (int) o;
        if (index >= length || index < 0)
            throw new IndexOutOfBoundsException("index " + index + " out of bound for length " + length);
        length--;
        for (int i = index; i < length; i++) {
            keys[i] = keys[i+1];
            values[i] = values[i+1];
        }
        if (length == keys.length / 4 && keys.length > 8)
            resize();
    }

    @Override
    public void removeK(K key) {
        int i = getIndexK(key);
        if (i != -1)
            remove(i);
    }

    @Override
    public void removeV(V value) {
        int i = getIndexV(value);
        if (i != -1)
            remove(i);
    }

    @Override
    public boolean hasNext() {
        return IteratorIndex < length;
    }

    @Override
    public Entry<K, V> next() {
        if (IteratorIndex >= length || IteratorIndex < 0)
            throw new IndexOutOfBoundsException();
        int index = IteratorIndex++;
        return new Entry<>(keys[index], values[index]);
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Iterator<IDict.Entry<K, V>> iterator() {
        IteratorIndex = 0;
        return this;
    }
}
