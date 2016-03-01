package android.softfan.dataCenter;

import java.io.PrintStream;
import java.io.PrintWriter;

public class DataCenterException extends Exception {
    private static final long serialVersionUID = 1L;
    private Exception ex;

    public DataCenterException(Exception ex) {
        super(ex.getMessage(),ex);
        this.ex = ex;
    }

    public DataCenterException(String s) {
        super(s);
        this.ex = null;
    }

    public String getMessage() {
        if (super.getMessage() != null)
            return super.getMessage();
        if (ex != null) {
            if (ex.getMessage() != null) {
                return ex.getMessage();
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
        System.err.println("caused by:");
        if (ex != null)
            ex.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        s.println("caused by:");
        if (ex != null)
            ex.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        s.println("caused by:");
        if (ex != null)
            ex.printStackTrace(s);
    }

}