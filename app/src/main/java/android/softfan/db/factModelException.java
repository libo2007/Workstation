package android.softfan.db;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class factModelException extends Exception {
	private static final long	serialVersionUID	= -6583234963942848784L;
	private Exception         ex               = null;
    private boolean           ignoreError      = false;

    public factModelException() {
        super();
    }

    public factModelException(Exception ex) {
        super(ex.getMessage());
        this.ex = ex;
    }

    public factModelException(String message) {
        super(message);
    }

    public factModelException(boolean ignoreError) {
        super();
        this.ignoreError = ignoreError;
    }

    public factModelException(Exception ex, boolean ignoreError) {
        super(ex.getMessage());
        this.ex = ex;
        this.ignoreError = ignoreError;
    }

    public factModelException(String message, boolean ignoreError) {
        super(message);
        this.ignoreError = ignoreError;
    }

    public boolean isIgnoreError() {
        return ignoreError;
    }

    public void setIgnoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
    }

    public String getMessage() {
        if (super.getMessage() != null)
            return super.getMessage();
        if (ex != null) {
            if (ex instanceof InvocationTargetException) {
                if (((InvocationTargetException) ex).getTargetException() != null) {
                    if (((InvocationTargetException) ex).getMessage() != null) {
                        return ((InvocationTargetException) ex).getMessage();
                    }
                }
            } else {
                if (ex.getMessage() != null) {
                    return ex.getMessage();
                }
            }
        }
        if (getCause() != null) {
            if (getCause().getMessage() != null)
                return getCause().getMessage();
        }
        return "";
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (ex != null)
            ex.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (ex != null)
            ex.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (ex != null)
            ex.printStackTrace(s);
    }

}