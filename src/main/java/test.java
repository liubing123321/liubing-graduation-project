

public class test {
    public static void main(String[] args) {
       MyList myList=new MyListImpl();
       myList.add("1");


        System.out.println(myList.isEmpty());
        System.out.println(myList.size());
        System.out.println(myList.contains(1));



    }
}
