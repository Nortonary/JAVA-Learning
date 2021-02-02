public class InterruptThread {
    public static void main(String[] args) throws Exception {
        InteruptedThread thread = new InteruptedThread();
        thread.start();
        System.in.read();
        thread.interrupt();
        System.out.println("Thread is out");
    }
}

class InteruptedThread extends Thread {
    public void run() {
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            System.out.println("Oh, I am interrupted! Is that you?");
        }
    }
}
