
public class ThreadSuspend {
    public static Integer counter = 0;
    public static Integer count() {
        counter++;
        System.out.println(Thread.currentThread().getName()+" counts, now it's "+counter.toString());
        return counter;
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSuspend threadSuspend = new ThreadSuspend();
        Sleeper sleeper = new Sleeper(threadSuspend);
        Waiter waiter = new Waiter(threadSuspend);
        Thread a = new Thread(sleeper, "Sleeper");
        Thread b = new Thread(waiter, "Waiter");

        b.start();
        Thread.sleep(100);
        a.start();
        System.out.println("This message is to show that even though a & b is suspended, main moves on");
    }
}

class Sleeper implements Runnable {
    private ThreadSuspend threadSuspend;

    public Sleeper(ThreadSuspend threadSuspend) {
        this.threadSuspend = threadSuspend;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " starts");
        synchronized (threadSuspend) {
            try {
                ThreadSuspend.count();
                System.out.println(Thread.currentThread().getName() + " is now sleeping");
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                threadSuspend.notifyAll();
                System.out.println(Thread.currentThread().getName() + " wakes up another thread");
            }
            System.out.println(Thread.currentThread().getName() + " is awake");
        }
    }
}

class Waiter implements Runnable {
    private ThreadSuspend threadSuspend;

    public Waiter(ThreadSuspend threadSuspend) {
        this.threadSuspend = threadSuspend;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " starts");
        synchronized (threadSuspend) {
            ThreadSuspend.count();
            System.out.println(Thread.currentThread().getName() + " is now waiting");
            try {
                threadSuspend.wait(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ThreadSuspend.count();
            System.out.println(Thread.currentThread().getName() + " is no longer waiting");
        }
    }
}


