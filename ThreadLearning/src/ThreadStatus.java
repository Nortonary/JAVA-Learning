class ThreadObject2 extends Thread {
    boolean waiting = true;
    boolean ready = false;

    ThreadObject2() {
    }

    public void run() {
        String thrdName = Thread.currentThread().getName();
        System.out.println(thrdName + " starting.");
        while (waiting)
            System.out.println("waiting:" + waiting);
        System.out.println("waiting...");
        startWait();
        try {
            Thread.sleep(1000);
        } catch (Exception exc) {
            System.out.println(thrdName + " interrupted.");
        }
        System.out.println(thrdName + " terminating.");
    }

    synchronized void startWait() {
        try {
            while (!ready) wait();
        } catch (InterruptedException exc) {
            System.out.println("wait() interrupted");
        }
    }

    synchronized void notice() {
        ready = true;
        notify();
    }
}

class Counter {
    private int count;

    Counter() {
        this.count = 0;
    }

    public int count() {
        this.count++;
        return this.count;
    }
}

public class ThreadStatus {


    public static Counter c = new Counter();

    public static void main(String args[])
            throws Exception {
        ThreadObject2 thrd = new ThreadObject2();
        thrd.setName("MyThread #1");
        showThreadStatus(thrd);
        thrd.start();                                                 //从这里线程启动，并开始异步于main之外执行
        Thread.sleep(1);                                        //main线程进入sleep，thrd则由于waiting为true进入循环
        showThreadStatus(thrd);
        thrd.waiting = false;                                         //修改thrd的waiting值，使其跳出循环，然后进入了startWaiting的循环
        Thread.sleep(10);
        showThreadStatus(thrd);
        thrd.notice();                                                //notice修改了ready值，使其跳出循环并进入实际工作状态
        Thread.sleep(10);
        showThreadStatus(thrd);                                       //输出中的TIMED_WAITING说明当前thrd线程正在等待Thread.sleep结束
        while (thrd.isAlive()) ;                                      //main在等待thrd结束工作
        showThreadStatus(thrd);
    }

    static void showThreadStatus(Thread thrd) {
        System.out.println(c.count() + "    " + thrd.getName() + "Alive:=" + thrd.isAlive() + " State:=" + thrd.getState());
    }
}