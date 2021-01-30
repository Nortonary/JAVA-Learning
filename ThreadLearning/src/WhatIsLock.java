import java.util.Date;

public class WhatIsLock {
    public static String obj1= "obj1";
    public static String obj2= "obj2";
    public static void main(String[] args) {
        LockA la= new LockA();
        LockB lb = new LockB();                                                                 //Lock是Runnable的实现类
        new Thread(la).start();
        new Thread(lb).start();
    }
}
//由于LockA锁住了obj1而LockB锁住了obj2，导致出现死锁
//即A因不能访问obj2而不能放开obj1，B亦是如此
/*
死锁是这样一种情形：多个线程同时被阻塞，它们中的一个或者全部都在等待某个资源被释放。由于线程被无限期地阻塞，因此程序不可能正常终止。
        java 死锁产生的四个必要条件：
        1、互斥使用，即当资源被一个线程使用(占有)时，别的线程不能使用
        2、不可抢占，资源请求者不能强制从资源占有者手中夺取资源，资源只能由资源占有者主动释放。
        3、请求和保持，即当资源请求者在请求其他的资源的同时保持对原有资源的占有。
        4、循环等待，即存在一个等待队列：P1占有P2的资源，P2占有P3的资源，P3占有P1的资源。这样就形成了一个等待环路。
        当上述四个条件都成立的时候，便形成死锁。当然，死锁的情况下如果打破上述任何一个条件，便可让死锁消失。
                                                                                ——来自菜鸟教程
 */
class LockA implements Runnable {
    public void run(){
        try {
            System.out.println(new Date().toString());
            while (true) {
                synchronized (WhatIsLock.obj1) {                                                //synchronized后跟着的变量只能被其后用大括号包起的代码块访问，在其执行完成前其他线程不能访问
                    System.out.println(new Date().toString()+"LockA 锁住了 obj1");
                    Thread.sleep(3000);
                    synchronized (WhatIsLock.obj2){
                        System.out.println(new Date().toString() + "LockA 锁住了 obj2");
                        Thread.sleep(60000);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class LockB implements Runnable {
    public void run(){
        try {
            System.out.println(new Date().toString());
            while (true) {
                synchronized (WhatIsLock.obj2) {
                    System.out.println(new Date().toString()+"LockB 锁住了 obj2");
                    Thread.sleep(3000);
                    synchronized (WhatIsLock.obj1){
                        System.out.println(new Date().toString() + "LockB 锁住了 obj1");
                        Thread.sleep(60000);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

