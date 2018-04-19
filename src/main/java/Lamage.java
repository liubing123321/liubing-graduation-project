import java.util.*;

public class Lamage {
    public static void main(String[] args) {
//        int count=2;
//        for(int i=1;i<=128;i++){
//            count=count*2;
//        }
//        System.out.println(count);
       // Optional<String> name = Optional.of("Sanaulla");
        Random random=new Random();
        for(int i=0;i<500;i++) {
            int randomDelayMillis = random.nextInt(1000);
            System.out.println(randomDelayMillis);
        }
      //  new Thread(
//                ()-> System.out.println("Thread run()")
//        ).start();

//        new Thread(new Runnable(){
//            public void run() {
//                System.out.println("Thread run()");
//            }
  //      }).start();


        /**
         * List承诺将元素维护在特定的序列中。
         ArrayList :它长于随机访问元素，但是list的中间插入和一处元素时较慢。
         LinkedList:提供代价较低的list中间进行插入和删除操作，提供了优化的顺序访问。但随机访问方面相对比较慢。
         List 接口提供了特殊的迭代器，称为 ListIterator，除了允许 Iterator 接口提供的正常操作外，该迭代器还允许元素插入和替换，以及双向访问。还提供了一个方法来获取从列表中指定位置开始的列表迭代器。
         */
        List list;
        /**
         * 一个不包含重复元素的 collection。
         */
        Set set;
        /**
         * Map 接口提供三种collection 视图，允许以键集、值集或键-值映射关系集的形式查看某个映射的内容。
         */
       // Map map;


        /**
         * 1）、列表  List接口（继承于Collection接口）及其实现类

         List接口及其实现类是容量可变的列表，可按索引访问集合中的元素。

         特点：集合中的元素有序、可重复；

         列表在数据结构中分别表现为：数组和向量、链表、堆栈、队列。

         实现类：
         ArrayList  实现一个数组，它的规模可变并且能像链表一样被访问。它提供的功能类似Vector类但不同步，它是以Array方式实现的List，允许快速随机存取。

         LinkedList实现一个链表，提供最佳顺序存取，适合插入和移除元素。由这个类定义的链表也可以像栈或队列一样被使用。提供最佳顺序存取，适合插入和移除元素。

         2）、集 Set接口（继承于Collection接口）及其实现类

         特点：集合中的元素不按特定方式排序，只是简单的把对象加入集合中，就像往口袋里放东西。

         对Set中成员的访问和操作是通过Set中对象的引用进行的，所以集中不能有重复对象。

         Set也有多种变体，可以实现排序等功能，如TreeSet，它把对象添加到集中的操作将变为按照某种比较规则将其插入到有序的对象序列中。            它实现的是SortedSet接口，也就是加入了对象比较的方法。通过对集中的对象迭代，我们可以得到一个升序的对象集合。

         实现类：

         HashSet 能够快速定位一个元素，要注意的是：存入HashSet中的对象必须实现HashCode()方法；

         TreeSet 将放入其中的元素按序存放。

         3）、映射 Map接口及其实现类

         Map是一个单独的接口，不继承于Collection。Map是一种把键对象和值对象进行关联的容器。

         特点：key不允许重复。

         映射与集或列表有明显区别，映射中每个项都是成对的，Map是把键对象和值对象进行关联的容器。映射中存储的每个对象都有一个相关的关键字(Key)对象，关键字决定了对象在映射中的存储位置，检索对象时必须提供相应的关键字，就像在字典中查单词一样。关键字应该是唯一的，也就是说Map中的键对象不允许重复，这是为了保证查询结果的一致性。

         关键字本身并不能决定对象的存储位置，它需要对过一种散列(hashing)技术来处理，产生一个被称作散列码(hash code)的整数值，散列码通常用作一个偏置量，该偏置量是相对于分配给映射的内存区域起始位置的，由此确定关键字/对象对的存储位置。理想情况下，散列处理应该产生给定范围内均匀分布的值，而且每个关键字应得到不同的散列码。

         实现类：

         HashMap 实现一个键到值映射的哈希表，通过键取得值对象，没有顺序，通过get(key)来获取value，允许存储空对象，而且允许键是空(由于键必须是唯一的，当然只能有一个)；

         HashTable 实现一个映象，所有的键必须非空。为了能高效的工作，定义键的类必须实现hashcode()方法和equal()方法。这个类是前面java实现的一个继承，并且通常能在实现映象的其他类中更好的使用。

         当元素的顺序很重要时选用TreeMap，当元素不必以特定的顺序进行存储时，使用HashMap。Hashtable的使用不被推荐，因为HashMap提供了所有类似的功能，并且速度更快。当你需要在多线程环境下使用时，HashMap也可以转换为同步的。

         Properties 一般是把属性文件读入流中后，以键-值对的形式进行保存，以方便读取其中的数据。

         4）、Iterator接口

         Iterator接口位于java.util包中，它是一个对集合进行迭代的迭代器。

         集合容器（如：List、Set、Map等）本身提供了处理元素置入和取出的方式，但是单一选取元素的方法很受限制。所以我们要用Iterator去选取容器中的元素，它将容器转换成一个序列。

         Iterator iter=Object.iterator();

         while(iter.hasNext()){   }



         5)、Collection、Set和List的区别？
         Collection对象之间没有指定的顺序，允许有重复元素和多个null元素对象；它是Set和List接口的父类，是一种最通用型的集合接口;
         Set各个元素对象之间没有指定的顺序，不允许有重复元素，最多允许有一个null元素对象；

         List各个元素对象之间有指定的顺序，允许重复元素和多个null元素对象；
         */
    }
}
