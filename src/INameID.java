public interface INameID<I, N, V extends INameID<String, String, V>> {
    I getID();
    N getName();
    void setName(N name);
    void hashCode(int hash);
    Point<V> getPoint();
    void setPoint(Point<V> p);
    V createNull();
}
