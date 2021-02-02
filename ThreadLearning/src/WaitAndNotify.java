public class WaitAndNotify {
    public static void main(String[] args) throws Exception{
        /*
        采用一个经典的ABC打印机问题来说明
        ABC三个线程要循环打印ABC三个字符
        因此将每个线程设置为必须获得两个锁才能运行
        仅当之前的一个线程释放自身对应的锁以及当前线程对应的锁闲置时才会执行
         */
        Object a = new boolean[0];
        Object b = new boolean[0];
        Object c = new boolean[0];

        ABCPrinter A=new ABCPrinter("A",c,a);
        ABCPrinter B=new ABCPrinter("B",a,b);
        ABCPrinter C=new ABCPrinter("C ",b,c);

        new Thread(A).start();
        Thread.sleep(100);
        new Thread(B).start();
        Thread.sleep(100);
        new Thread(C).start();
        Thread.sleep(100);

    }
}

class ABCPrinter implements Runnable {
    private String name;
    private Object prev;        //前一个线程锁的对象
    private Object self;        //当前线程锁的对象

    ABCPrinter(String name,Object prev,Object self){
        this.name=name;
        this.prev=prev;
        this.self=self;
    }

    @Override
    public void run() {
        int count = 10;
        while(count>0){
            synchronized (prev){
                synchronized (self){
                    System.out.print(name);
                    count--;

                    self.notify();      //唤醒下一个线程，记为X线程。此时X线程由于没获得所而处于锁池
                }
                try {
                    prev.wait();        //释放锁，此时X线程获得锁进入Runnable状态。
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
