class ThreadObject1 extends Thread {
    public static void whatAmIDoing() {
        Thread t = Thread.currentThread();
        System.out.println("I am doing " + t.getName() + "'s job now");
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            whatAmIDoing();//被调用时说明该线程在工作
        }
    }
}

public class IsThisThreadAlive {
    public static void main(String[] args) {
        ThreadObject1 threadObject = new ThreadObject1();
        threadObject.setName("Bob's second son");
        System.out.println("Before start, Bob's second son is " + threadObject.isAlive());
        threadObject.start();
        System.out.println("After start, Bob's second son is " + threadObject.isAlive());
        for (int i = 0; i < 100; i++) {//用100是因为太少时看不出来穿插执行的特点
            ThreadObject1.whatAmIDoing();//看做main线程在工作，使用静态方法是为了更清晰地展示
        }
        System.out.println("At the end of all these, Bob's second son is " + threadObject.isAlive());
    }
}
