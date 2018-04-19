import com.sun.glass.ui.View;

import java.util.Arrays;
import java.util.Collection;

public class MyArrayList <E>{
    public transient  E[] elements;

    public int size;

    /**
     * 三种初始化方式
     * @param n
     */
    public MyArrayList(int n){
        if(n>0){
            this.elements=(E[])new Object[n];
            this.size=0;
        }else{
            throw new IllegalArgumentException("Illegal Capacity:"+n);
        }

    }
    public MyArrayList(){
        this.elements=(E[])new Object[10];
        this.size=0;
    }
    public MyArrayList(Collection<Object> c){
        elements=(E[])c.toArray();
        if((size=elements.length)!=0){
            elements=(E[])Arrays.copyOf(elements,size,Object[].class);
        }
    }

    /**
     * 实现容量扩充
     */
    public void ensureCapacity(int newCapacity){
        int curr=elements.length;
        if(newCapacity>curr){
            elements=Arrays.copyOf(elements,newCapacity);
        }
    }
    /**
     * 调整底层数组容量以契合当前元素数量，避免空元素部分太多而浪费内存。
     */
    public void trimToSize(){
        int curr=elements.length;
        if(size<curr){
            elements=Arrays.copyOf(elements,size);
        }
    }
    /**
     * 实现下标检查：避免访问越界内存
     */
    public void rangeCheck(int requestIndex){
        if(requestIndex<0|| requestIndex>size){
                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * 实现基本操作：增 删 改 查
     */
    public void add(int index,E element){
        this.rangeCheck(index);//先检查插入位置的合法性

        //再检查当前数组元素个数是否已经达到最大值，即达到数组容量，是，则扩充数组
        if(size==elements.length){
            ensureCapacity(elements.length*2+1);
        }
        //将插入位置的原数据以及其后的数据往后移动移动，腾出空间
        System.arraycopy(elements,index,elements,index+1,size-index);

        //插入响应位置，并将数组元素个数加一
        elements[index]=element;
        ++size;
    }
    /**
     * 在数组末尾追加一个元素
     */
    public void add(E element){
        this.add(size,element);
    }
    /**
     * 将一个集合插入到数组某位置
     */
    public void addAll(int index,Collection<? extends E> c){
        rangeCheck(index);

        //获取插入元素以及个数
        E[] newEs=(E[])c.toArray();

        int newLength=newEs.length;

        //扩充数组容量
        ensureCapacity(size+newLength+1);

        int move = size-index;
        //将原数组index~size范围的元素移动，腾出位置
        if(move>0){
            System.arraycopy(elements,index,elements,index+newLength,move);
        }
        //将插入数组元素复制到elements数组中腾出的位置
        System.arraycopy(newEs,0,elements,index,newLength);

        size+=newLength;

    }
    //将一个集合插入到数组末尾
    public void addAll(Collection<? extends E> c){
        this.addAll(size, c);//相当于在原数组elements[size]处开始，插入一个集合
    }

    //删
    //移除指定位置的元素
    public E remove(int index){
        rangeCheck(index);
        E oldelement=elements[index];
        System.arraycopy(elements,index+1,elements,index,size-index-1);
        elements[size]=null;
        --size;
        return oldelement;
    }
    //移除某个元素的值
    public boolean remove(E element){
        boolean index=false;
        for(int i=0;i<size;i++){
            if(elements.equals(elements[i])) {
                index = true;
                this.remove(i);
            }

        }
        return index;
    }
    //移除某个范围内的元素
    public void removeRange(int start,int end){
        int move=size-end;
        System.arraycopy(elements,end,elements,start,move);
        //修改左移留下的位置为空
        for(int i=size-1;i>(size-(end-start)-1);--i){
            elements[i]=null;
        }
        size-=(end-start);
    }
    //查
    //获取某位置的元素
    public E get(int index){
        rangeCheck(index);
        return elements[index];
    }
    //改
    //修改某个位置的元素
    public void set(int index,E newElement){
        rangeCheck(index);
        elements[index]=newElement;
    }
    //转换为普通数组
    public E[]  toArray(){
        E[] array=(E[]) new Object[size];
        System.arraycopy(elements,0,array,0,size);
        return array;
    }
    public static void main(String[] args) {
        MyArrayList<Integer> testMyArrayList=new MyArrayList<Integer>();

        System.out.println(testMyArrayList.size);

        testMyArrayList.add(1);
        testMyArrayList.add(2);
        testMyArrayList.add(3);
        testMyArrayList.add(4);
        testMyArrayList.add(5);

        System.out.println(testMyArrayList.size);
        System.out.println(testMyArrayList.get(3));

        testMyArrayList.removeRange(1, 3);

        System.out.println(testMyArrayList.size);
        System.out.println(testMyArrayList.get(2));
        System.out.println("----------------------");
        for(int i=0;i<testMyArrayList.size;i++){
            System.out.println(testMyArrayList.get(i));
        }
        testMyArrayList.add(0,5);
        System.out.println("----------------------");
        for(int i=0;i<testMyArrayList.size;i++){
            System.out.println(testMyArrayList.get(i));
        }
    }
}
