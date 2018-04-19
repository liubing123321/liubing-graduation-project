public class MyStack<E> implements IStack<E> {

    private Object[] data=null;//数据域
    private int top = -1;//栈顶指针初始化为-1；
    private int maxSize=0;//栈最大容量

    public MyStack(int initialSize){
        if(initialSize>=0){
            this.data=new Object[initialSize];//初始化数组
            this.maxSize=initialSize;//设置栈最大容量
            this.top=-1;
        }
    }

    @Override
    public boolean isEmpty() {
        return top == -1? true:false;//根据栈顶值判断，如果栈顶指针没有更新，则为空栈
    }

    @Override
    public boolean isMax() {
        return top >=maxSize-1 ? true : false;//根据栈顶值判断，如果栈顶指针大于最大容量，则为满栈
    }

    @Override
    public boolean push(E e) {
        if(isMax()){
            System.out.println("对不起，。栈已满，无法入栈");
            return false;
        }
        top++;
        data[top]=e;
        return true;
    }

    @Override
    public E pop() {
        if(isEmpty()){
            System.out.println("对不起，目前是空栈，没有元素可以出栈");
            return null;
        }

        E e=(E) data[top];
        top--;
        return e;
    }

    @Override
    public E peek() {
        if(isEmpty()){
            System.out.println("对不起，目前是空栈，无法返回栈顶元素");
            return null;
        }
        return (E) data[top];
    }

    @Override
    public int getIndex(E e) {
        while(top!=-1){
            if(peek().equals(e)){
                return top;
            }
            top--;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.top+1;
    }

    @Override
    public int getStackSize() {
        return this.maxSize;//返回栈实际长度
    }

    @Override
    public void display() {
        while(top!=-1){
            System.out.println(top);
            top--;
        }



        int i=0;
    }
}
