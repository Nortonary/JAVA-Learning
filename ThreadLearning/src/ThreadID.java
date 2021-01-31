public class ThreadID {
    public static void main(String[] args) {
        TID tid = new TID();                                        //这是一个公共变量，为三个线程共享
        RunnableObject1 shared = new RunnableObject1(tid);          //这是一个可执行函数

        try {
            Thread threadA = new Thread(shared, "threadA");
            threadA.start();

            Thread.sleep(500);

            Thread threadB = new Thread(shared, "threadB");
            threadB.start();

            Thread.sleep(500);

            Thread threadC = new Thread(shared, "threadC"); //跑三个线程，都对tid有交互
            threadC.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class RunnableObject1 implements Runnable {
    private TID var;

    public RunnableObject1(TID v) {
        this.var = v;
    }

    private static void print(String msg) {
        String name = Thread.currentThread().getName();
        System.out.println(name + " : " + msg);
    }

    public void run() {
        try {
            print("var getThreadID =" + var.getThreadID());
            Thread.sleep(2000);
            print("var getThreadID =" + var.getThreadID());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TID extends ThreadLocal {
    private int nextID;

    public TID() {
        this.nextID = 10001;                            //初始值为10001
    }

    private static void print(String msg) {
        String name = Thread.currentThread().getName();
        System.out.println(name + " : " + msg);
    }

    private synchronized Integer getNewID() {           //这个方法改变了公共变量
        Integer id = new Integer(nextID);
        nextID++;
        return id;
    }

    @Override
    protected Object initialValue() {
        print("in initialValue()");
        return getNewID();                              //在initialValue()中调用getNewID()会使得该公共变量被改变
    }

    public int getThreadID() {
        Integer id = (Integer) get();                   //get()方法，在当前线程是第一次访问该公共变量时，调用initialValue()方法并获取其返回值
        return id.intValue();
    }

}
