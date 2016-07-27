package com.qjq.parser.extractor.exception;

import java.io.PrintStream;

/**
 * @function:simple way to wrap all checked exceptions.
 * @author: qingjiquan
 * @Date: 2014-10-27
 */
public class WrappedException extends RuntimeException {

    private Throwable source;

    public WrappedException(Throwable e) {
        source = e;
    }

    public Throwable getSource() {
        return source;
    }

    public String getMessage() {
        return source.getMessage();
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        source.printStackTrace(s);
    }
}
