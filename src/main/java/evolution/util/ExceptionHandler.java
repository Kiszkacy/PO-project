package evolution.util;

import static evolution.util.EasyPrint.pcol;

/**
 * Mainly used in debugging process. Acts as an elegant wrapper for methods handling exceptions.
 */
public class ExceptionHandler {

    /**
     * Prints exception's message and information on where it has happened in code.
     * @param e exception which information will be printed
     */
    static public void printCriticalInfo(Exception e) {
        pcol(Color.RED, e.getMessage());
        pcol(Color.RED, "at: " + e.getStackTrace()[0].getClassName());
        pcol(Color.RED, "inside: " + e.getStackTrace()[0].getMethodName());
        pcol(Color.RED, "line: " + e.getStackTrace()[0].getLineNumber());
    }
}
