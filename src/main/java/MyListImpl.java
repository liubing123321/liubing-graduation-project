import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MyListImpl<E> implements MyList<E> {
    /**
     * ArrayList的默认容量是10
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Shared empty array instance used for empty instances.
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;

    protected transient int modCount = 0;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //--------------------------------------------------------------------------------------------
    @Override
    public int size() {

        return size;
    }


    public MyListImpl(){

        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;

    }


    //--------------------------------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return size==0;
    }
    //--------------------------------------------------------------------------------------------

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    //--------------------------------------------------------------------------------------------
    @Override
    public void add(int index, E element) {

    }


    @Override
    public boolean add(E e) {
        isCapacityEnough(size + 1);  // Increments（增量） modCount!!确保容量内部    判断是否需要扩容

        elementData[size++] = e;
        return true;
    }
    private void isCapacityEnough(int size){
        if (size > DEFAULT_CAPACITY){
            explicitCapacity(size);
        }
        if (size < 0){
            throw new OutOfMemoryError();
        }
    }
    private final static int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    private void explicitCapacity(int capacity){
        int newLength = elementData.length * 2;
        if (newLength - capacity < 0){
            newLength = capacity;
        }
        if (newLength > (MAX_ARRAY_LENGTH)){
            newLength = (capacity > MAX_ARRAY_LENGTH ? Integer.MAX_VALUE : MAX_ARRAY_LENGTH);
        }
        elementData = Arrays.copyOf(elementData, newLength);
    }



    //--------------------------------------------------------------------------------------------


    @Override
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }
    //范围盘点查看所要取得的元素索引是否大于数组的大小
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    E elementData(int index) {
        return (E) elementData[index];
    }


}
