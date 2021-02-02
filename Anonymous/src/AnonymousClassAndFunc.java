interface SomeKindOfInterface {
    void AnonymousFunc(String arg);
}

public class AnonymousClassAndFunc {
    public static void main(String[] args) {
        Test test1 = new Test(new SomeKindOfInterface() {
            @Override
            public void AnonymousFunc(String arg) {
                System.out.println(arg);
            }
        });
        /*
        对任意一个接口，由于接口不能直接实例化，所以需要利用“匿名内部类“的形式，
        即采用以上方式实现接口的方法，从而实现一个匿名的内部类实例
         */

        Test test2 = new Test((arg) -> {
            System.out.println(arg);
        });
        /*
        对一个只有一个内部方法的接口，则还可以通过匿名函数的方法进行，
        匿名方法默认为其内部方法，
        采用(形式参数)->{方法实现}的方式，代替类的实例
         */
    }
}

class Test {
    Test(SomeKindOfInterface i) {
        i.AnonymousFunc("test");
    }
}
