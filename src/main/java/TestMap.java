import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class TestMap {
    public static void main(String[] args) throws Exception{
        detectSex(Person.class);
    }

    public static <T extends Person> void detectSex(Class<T> type) throws Exception {
        Constructor<T> person = type.getConstructor(int.class);
        Map<Person, Sex> map = new HashMap<>();  //创建一个HashMap用于存放人和性别的键值对
        for (int i = 0; i < 10; i++) {
            map.put(person.newInstance(i), new Sex());
        }
        System.out.println("map:" + map);
        Person p = person.newInstance(3);  //创建一个编号为3的人，并在map中查找此人性别
        System.out.println("查找某人性别" + p);
        if (map.containsKey(p))
            System.out.println(map.get(p));
        else
            System.out.println("无此人");
    }
}
//定义人这个类，设定一个编号
class Person {
    private int cardId;
    public Person(int cardId) {this.cardId= cardId;}

    @Override
    public String toString() {
        return "Person#" + cardId;  //返回编号
    }
    @Override
    public boolean equals(Object obj) {
        return  (obj instanceof Person && (((Person) obj).cardId == cardId));

    }

    @Override
    public int hashCode() {
        return cardId;  //把唯一的编号作为散列码
    }
}
//人的性别
class Sex {
    private int  i = (int) (Math.random() * 2);

    @Override
    public String toString() {
        return i == 0? "male" : "female";  //随机返回一个性别
    }

}

