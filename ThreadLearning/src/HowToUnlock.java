import java.sql.Time;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class HowToUnlock {
    public static String obj1 = "obj1";
    public static final Semaphore a1 = new Semaphore(1);
    public static String obj2 = "obj2";
    public static final Semaphore a2 = new Semaphore(1);
/*
Semaphore称为信号量，是一个计数器，用于包含共享资源的访问，初始化时能设置其初始量，
如果线程要访问一个资源，则约定其必须先向对应计数器请求一次（tryAcquire），
tryAcquire时可以设定额外参数timeout和timeunit来设定最长请求等待时间，否则以请求发生时为准，
如果当前计数器量大于0，则计数器减一并返回true，否则维持0并返回false，
当release时计数器会回复增加一个单位。
 */

    public static void main(String[] args) {
        LockAa la = new LockAa();
        LockBb lb = new LockBb();
        new Thread(la).start();
        new Thread(lb).start();
    }
}

class LockAa implements Runnable {
    public void run(){
        try {
            System.out.println(new Date().toString()+"Lock A starts");
            while (true) {
                if(HowToUnlock.a1.tryAcquire(1,TimeUnit.SECONDS)){
                    System.out.println(new Date().toString()+"Lock A locks obj1");
                    if(HowToUnlock.a2.tryAcquire(1, TimeUnit.SECONDS)){
                        System.out.println(new Date().toString()+"Lock A locks obj2");
                        Thread.sleep(6000);
                        HowToUnlock.a1.release();           //注意只有当请求成功才需要release对应信息量，否则不能release（引发bug）
                        HowToUnlock.a2.release();
                        break;                              //注意完成正常请求后需要退出死循环，否则两线程的请求冲突无法结束，则永远是请求频率最高者获胜，另一者无法获取资源
                    }
                    else {
                        System.out.println(new Date().toString()+"LockA failed to lock obj2");
                        HowToUnlock.a1.release();           //比如这里请求a2失败则不release a2
                    }
                }//访问结束，release释放资源
                System.out.println("LockA releases");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class LockBb implements Runnable {
    public void run(){
        try {
            System.out.println(new Date().toString()+"Lock B starts");
            while (true) {
                if(HowToUnlock.a2.tryAcquire(1,TimeUnit.SECONDS)) {
                    System.out.println(new Date().toString() + "Lock B locks obj2");
                    if (HowToUnlock.a1.tryAcquire(1, TimeUnit.SECONDS)) {
                        System.out.println(new Date().toString() + "Lock B locks obj1");
                        Thread.sleep(6000);
                        HowToUnlock.a1.release();
                        HowToUnlock.a2.release();
                        break;
                    } else {
                        System.out.println(new Date().toString() + "LockB failed to lock obj1");
                        HowToUnlock.a2.release();
                    }
                }
                System.out.println("LockB releases");
                Thread.sleep(500);                 //本实验中LockA请求频率是立即（即不sleep），LockB请求频率低，故后获得资源
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}