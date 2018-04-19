public class LinkedStack {
    //定义节点类
    private class Node{
        public Object data = null;//数据域
        public Node next =null;//指针域

        public Node(){}

        public Node(Object data,Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node top =null;//定义栈顶
    private int size=0;//定义栈节点数量

    //判断栈空
    public boolean isEmpty(){
        return size==0 ?true:false;
    }

    //压栈
    public boolean push(Object obj){
        top=new Node(obj,top);//压栈是节点插入到栈顶之前，也就是更新头节点。改变指针方向
        size++;
        return true;
    }
    //出栈
    public Object pop(){
        if(isEmpty()){
            System.out.println("对不起，目前是空栈，不能出栈");
            return null;
        }
        Node temp=top;//头节点引用
        top=top.next;
        temp.next=null;
        size--;
        return temp.data;
    }

    //返回栈顶元素，但不弹出栈
    public Object peek(){
        return this.top.data;//直接返回栈顶元素
    }

    //遍历栈并打印
    public void display(){
        while(top!=null){
            System.out.println(top.data);
            top = top.next;
        }
    }

    //返回元素在栈中的位置
    public int getIndex(Object obj){
        int i=0;
        while(top!=null){
            if(peek().equals(obj)){
             return i;
            }
            top=top.next;
            i++;
        }
        return -1;
    }

    public int getSize(){
        return this.size;
    }
}
