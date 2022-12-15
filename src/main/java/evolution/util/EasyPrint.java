package evolution.util;

public class EasyPrint {

    public static <T> void p(T arg) {
        System.out.println(arg);
    }

    public static <T> void pcol(Color color, T arg) {
        System.out.print(color);
        System.out.println(arg);
        System.out.print(Color.CLEAR);
    }
}
