package evolution.util;

/**
 * Class that simplifies printing also featuring color print. Mainly used for debugging purposes.
 */
public class EasyPrint {

    public static <T> void p(T arg) {
        System.out.println(arg);
    }

    public static <T> void p(T arg, String end) {
        System.out.print(arg);
        System.out.print(end);
    }

    public static <T> void pcol(Color color, T arg) {
        System.out.print(color);
        System.out.println(arg);
        System.out.print(Color.CLEAR);
    }
}
