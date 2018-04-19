/**
 * java.lang.StackOverflowError 一般发生在当应用程序递归太深而发生堆栈溢出时
 */
public class StackOverflowErrorMain {
    int stackLength = 0;

    public StackOverflowErrorMain() {
    }

    public void addStackLength() {
        stackLength++;
        addStackLength();
    }

    public static void main(String[] args) {
        StackOverflowErrorMain sofem = new StackOverflowErrorMain();
        try {
            sofem.addStackLength();
            int a = 1+1;
        } catch (Throwable e) {
            System.out.println(sofem.stackLength);
            e.printStackTrace();
        }
    }
}

/**
 * 不断创建新的线程的实践中会出现OutofMemoryError的错误
 */
//public class StackOverflowErrorMain {
//    int threadCount = 0;
//
//    public StackOverflowErrorMain() {
//    }
//
//    public void addNewThread(){
//        while (true) {
//            threadCount++; //线程的数量
//            new Thread() {
//                @Override
//                public void run() {
//                    while (true) ; //线程执行不能停
//                }
//            }.start();
//        }
//    }
//
//    public static void main(String[] args){
//        StackOverflowErrorMain sofem = new StackOverflowErrorMain();
//        try {
//            sofem.addNewThread();
//        }catch (Throwable e){
//            System.out.println(sofem.threadCount);
//            e.printStackTrace();
//        }
//    }
//}