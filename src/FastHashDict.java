import java.util.Iterator;

public class FastHashDict<V extends INameID<String, String, V>> implements IDict<String, V> {
    public static class Index {
        public final int hash;
        public final int index;
        public Index(int h, int i) {
            hash = h;
            index = i;
        }
    }

    ArrayDict<String, V>[] table = new ArrayDict[1];
    public Point<V> tree;
    private final int a, b, p;
    private int length = 0;
    private int index1, index2;

    public FastHashDict(int a, int b, int p) {
        this.a = a;
        this.b = b;
        this.p = p;
    }

    private void resize() {
        int newSize = 2 * length;
        if (newSize > p)
            newSize = p;
        ArrayDict<String, V>[] old = table;
        table = new ArrayDict[newSize];
        for (ArrayDict<String, V> dict: old) {
            if (dict == null)
                continue;
            for (IDict.Entry<String, V> entry: dict) {
                shortAdd(entry.getKey(), entry.getValue());
            }
        }
    }

    private int getHash(String key) {
        return (int)(((a * Long.parseLong(key) + b) % p) % table.length);
    }

    private void shortAdd(String key, V value) {
        int hash = getHash(key);
        value.hashCode(hash);
        if (table[hash] == null)
            table[hash] = new ArrayDict<>();
        table[hash].add(key, value);
    }

    @Override
    public void add(String key, V value) {
        if (length == table.length && table.length < p)
            resize();
        shortAdd(key, value);
        if (tree == null)
            tree = new Point<>(value.createNull(), null);
        tree.add(value);
        length++;
    }

    @Override
    public void setK(String key, V value) {
        table[getHash(key)].setK(key, value);
    }

    @Override
    public void set(Object o, V value) {
        Index index = (Index) o;
        table[index.hash].set(index.index, value);
    }

    @Override
    public V getK(String key) {
        return table[getHash(key)].getK(key);
    }

    @Override
    public V get(Object o) {
        Index index = (Index) o;
        return table[index.hash].get(index.index);
    }

    @Override
    public String getKeyV(V value) {
        return value.getID();
    }

    @Override
    public String getKey(Object o) {
        Index index = (Index) o;
        return table[index.hash].getKey(index.index);
    }

    @Override
    public Object getIndexK(String key) {
        int h = getHash(key);
        return new Index(h, table[h].getIndexK(key));
    }

    @Override
    public Object getIndexV(V value) {
        return getIndexK(value.getID());
    }

    @Override
    public void remove(Object o) {
        Index index = (Index) o;
        table[index.hash].get(index.index).getPoint().delete(null);
        table[index.hash].remove(index.index);
        if (length == table.length / 4 && table.length > 1)
            resize();
        length--;
    }

    @Override
    public void removeK(String key) {
        remove(getIndexK(key));
    }

    @Override
    public void removeV(V value) {
        remove(getIndexV(value));
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public Iterator<Entry<String, V>> iterator() {
        index1 = 0;
        index2 = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        if (index1 < table.length && index2 < table[index1].getLength())
            return true;
        for (index1++, index2 = 0; index1 < table.length; index1++)
            if (table[index1] != null && table[index1].getLength() > 0)
                return true;
        return false;
    }

    @Override
    public Entry<String, V> next() {
        V v = null;
        if (index1 < table.length && index2 < table[index1].getLength())
            v = table[index1].get(index2++);
        for (index1++, index2 = 1; index1 < table.length; index1++)
            if (table[index1] != null && table[index1].getLength() > 0)
                v = table[index1].get(0);
        if (v == null)
            throw new IndexOutOfBoundsException();
        return new Entry<>(v.getID(), v);
    }

    @Override
    public V search(String name) {
        return tree.search(name);
    }
}
