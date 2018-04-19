import com.sun.tools.javac.code.Attribute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArrayQueue<E> {
    Object [] queue;
    int size;

    public ArrayQueue(){
        queue=new Object[10];
    }

    public boolean isEmpty(){
        return size==0;
    }

    public E poll(){
        if(isEmpty()){
            return null;
        }
        E data = (E)queue[0];

        System.arraycopy(queue,1,queue,0,size);
        size--;
        return data;
    }
    //实现容量扩充
    private void ensureCapacity(int size){
        if(size>queue.length){
            int len = queue.length+10;
            queue= Arrays.copyOf(queue,len);
        }
    }

    public void offer(E data){
        ensureCapacity(size+1);
        queue[size++]=data;
    }
    Map a=new HashMap<>();
}
