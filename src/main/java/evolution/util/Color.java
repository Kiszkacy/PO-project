package evolution.util;

public enum Color {
    CLEAR("\033[m"),
    DEFAULT("\033[m"),
    WEAK_BLACK("\033[90m"),
    WEAK_RED("\033[31m"),
    WEAK_GREEN("\033[32m"),
    WEAK_YELLOW("\033[33m"),
    WEAK_BLUE("\033[34m"),
    WEAK_VIOLET("\033[35m"),
    WEAK_CYAN("\033[36m"),
    BLACK("\033[30m"),
    RED("\033[91m"),
    GREEN("\033[92m"),
    YELLOW("\033[93m"),
    BLUE("\033[94m"),
    VIOLET("\033[95m"),
    CYAN("\033[96m"),
    GRAY("\033[37m"),
    WHITE("\033[97m");

    private final String val;


    @Override
    public String toString() {
        return this.val;
    }

    Color(String val) {
        this.val = val;
    }
}
