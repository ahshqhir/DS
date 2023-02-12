public class Point<V extends INameID<String, String, V>> {
    private V value;
    private Point<V> right;
    private Point<V> left;
    int rightHeight;
    int leftHeight;
    private final Point<V> parent;
    int length = 1;

    public Point(V value, Point<V> parent) {
        this.value = value;
        value.setPoint(this);
        this.parent = parent;
    }

    public void add(V value) {
        if (this.value.getName().compareTo(value.getName()) > 0)
            addLeft(value);
        else
            addRight(value);
        length++;
    }

    private void addRight(V value) {
        if (right == null)
            right = new Point<>(value, this);
        else
            right.add(value);
    }

    private void addLeft(V value) {
        if (left == null)
            left = new Point<>(value, this);
        else
            left.add(value);
    }

    public Point<V> getMin() {
        return left == null? this: left.getMin();
    }

    public Point<V> getMax() {
        return right == null? this: right.getMax();
    }

    public void delete(Point<V> p0) {
        value.setPoint(p0);
        this.value = null;
        if (right != null) {
            Point<V> p = right.getMin();
            value = p.value;
            p.delete(this);
            return;
        }
        if (left != null) {
            Point<V> p = left.getMax();
            value = p.value;
            p.delete(this);
            return;
        }
        if (parent.right == this)
            parent.right = null;
        else
            parent.left = null;
    }

    public  V search(String s) {
        int r = value.getName().compareTo(s);
        if (r > 0)
            return left.search(s);
        if (r < 0)
            return right.search(s);
        return this.value;
    }
}
