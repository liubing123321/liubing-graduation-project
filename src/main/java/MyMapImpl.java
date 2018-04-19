import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.hash;
public class MyMapImpl<k,v> implements MyMap<k,v>{

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public v get(Object key) {
        return null;
    }



    public v put(k key, v value) {

        return putVal(hash(key), key, value, false, true);
    }

    /**
     * hashCode是jdk根据对象的地址或者字符串或者数字算出来的int类型的数值
     *java中int类型32位
     * 无符号的右移（>>>）:按照二进制把数字右移指定数位，高位直接补零，低位移除
     * （位异或^）1 ^ 1 = 0   1 ^ 0 = 1
     * (h = key.hashCode()) ^ (h >>> 16)将高16位全部置1低16位保留hashCode的值
     *
     * @param key
     * @return
     */

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 定义节点
     * @param <K>
     * @param <V>
     */
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            //这条判断存在的意义是什么？
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    final v putVal(int hash, k key, v value, boolean onlyIfAbsent, boolean evict) {

    Node<k,v>[] tab;
    Node<k,v> p;
    int n, i;


        return null;
    }




    public v remove(Object key) {
        return null;
    }

    public void clear() {

    }
}
