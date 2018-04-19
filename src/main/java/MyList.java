import java.util.Collection;

public interface MyList<E> {
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    boolean add(E e);
    void add(int index, E element);
    E get(int index);
}
