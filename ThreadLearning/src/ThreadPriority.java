class SimplePriority extends Thread {
    private int countDown = 5;
    private volatile double d = 0;

    public SimplePriority(int priority) {
        setPriority(priority);
        setName(Integer.toString(priority)+"Thread");
        start();
    }

    public String toString() {
        return super.toString() + ":" + countDown;
    }

    public void run() {
        while (true) {
            for (int i = 0; i < 100000; i++) d = d + (Math.PI + Math.E) / (double) i;//模拟任务延迟
            System.out.println(this.toString());
            if (--countDown == 0) return;
        }
    }
}


public class ThreadPriority {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            SimplePriority spl = new SimplePriority(Thread.MIN_PRIORITY);
        }
        SimplePriority sph = new SimplePriority(Thread.MAX_PRIORITY);//可见即使该线程后于所有线程被初始化，其在内存中仍然首先被执行
    }

}
