
package android.softfan.dataCenter;

import android.softfan.dataCenter.task.DataCenterTaskCmd;

import java.util.Date;
import java.util.HashMap;

public class DataCenterClientTest extends Thread {

    private ApDataCenter apDataCenter;

    private boolean done;

    public DataCenterClientTest(ApDataCenter apDataCenter) {
        super();
        this.apDataCenter = apDataCenter;
    }

    public void run() {
        try {
            while (!isDone()) {
                try {
                    DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
                    retcmd.setCmd("test");
                    retcmd.setHasResponse(true);
                    retcmd.setLevel(2);
                    HashMap<String, Object> values = new HashMap<String, Object>();
                    values.put("x1", new Integer(3));
                    values.put("x2", "xt");
                    values.put("x3", new Date(System.currentTimeMillis()));
                    retcmd.setValues(values);
                    apDataCenter.addSendCmd(retcmd);
                } catch (Exception e) {
                }
                synchronized (this) {
                    try {
                        this.wait(30);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } finally {
            apDataCenter = null;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    public void doDone() {
        synchronized (this) {
            done = true;
            this.notifyAll();
        }
    }

    public boolean isDone() {
        return done;
    }

    public boolean isFinished() {
        return apDataCenter == null;
    }
}
