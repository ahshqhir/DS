import java.util.Iterator;

public class Set implements Iterator<Integer>, Iterable<Integer> {
    private boolean iterable = true;
    private boolean reset = true;
    private boolean rightNext = false;
    private boolean leftNext = false;
    private final int value;
    private final Set parent;
    private Set right;
    private Set left;
    private int length = 1;

    public Set(int value, Set parent) {
        this.value = value;
        this.parent = parent;
    }

    public boolean add(int value) {
        int nothing;
        if (value == 10002)
            nothing = 0;
        int r = Integer.compare(this.value, value);
        boolean res = false;
        if (r > 0)
            if (addLeft(value))
                res = true;
        if (r < 0)
            if (addRight(value))
                res = true;
        if (r == 0)
            return false;
        if (res)
            length++;
        return res;
    }

    private boolean addRight(int value) {
        if (right == null) {
            right = new Set(value, this);
            rightNext = true;
            return true;
        }
        else
            return right.add(value);
    }

    private boolean addLeft(int value) {
        if (left == null) {
            left = new Set(value, this);
            leftNext = true;
            return true;
        }
        else
            return left.add(value);
    }

    public boolean contains(int i) {
        int r = Integer.compare(this.value, i);
        if (r > 0)
            return left != null && left.contains(i);
        if (r < 0)
            return right != null && right.contains(i);
        return true;
    }

    private void reset() {
        if (reset)
            return;
        iterable = true;
        reset = true;
        if (left != null) {
            leftNext = true;
            left.reset();
        }
        if (right != null) {
            rightNext = true;
            right.reset();
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        reset();
        return this;
    }

    @Override
    public boolean hasNext() {
        return leftNext || iterable || rightNext;
    }

    @Override
    public Integer next() {
        Integer res;
        reset = false;
        if (leftNext)
            res = left.next();
        else if (iterable) {
            iterable = false;
            res = value;
        }
        else if (rightNext)
            res = right.next();
        else
            throw new IndexOutOfBoundsException();
        if (!hasNext() && parent != null) {
            if (parent.right == this)
                parent.rightNext = false;
            else
                parent.leftNext = false;
        }
        return res;
    }

    public int getLength() {
        return length;
    }
}
