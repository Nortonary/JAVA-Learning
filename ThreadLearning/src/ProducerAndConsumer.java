import java.util.Stack;

public class ProducerAndConsumer {
    public static void main(String[] args) throws Exception{
        Buffer buf = new Buffer();
        Producer producer = new Producer(buf);
        Consumer consumer = new Consumer(buf);

        Thread p = new Thread(producer, "Producer");
        Thread c = new Thread(consumer, "Consumer");
        p.start();
        c.start();
        Thread.sleep(10000);
    }
}

class Buffer {
    private Stack<Integer> contents;

    Buffer(){
        this.contents=new Stack<>();
    }

    public synchronized Integer get() {
        while (contents.empty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        return contents.pop();
    }

    public synchronized void put(Integer input) {
        contents.push(input);
        notifyAll();
    }
}

class Producer implements Runnable {
    Buffer buf;

    Producer(Buffer buf) {
        this.buf = buf;
    }

    @Override
    public void run() {
        Integer i = 0;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            buf.put(i);
            System.out.println(Thread.currentThread().getName() + " put " + i.toString());
            i++;
        }
    }
}

class Consumer implements Runnable {
    Buffer buf;

    Consumer(Buffer buf) {
        this.buf = buf;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " get " + buf.get().toString());
        }
    }
}

