
package android.softfan.dataCenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataCenterClientTestService implements IDataCenterPushThread {

    private ApDataCenter apDataCenter;

    private List<DataCenterClientTest> tests = new ArrayList<DataCenterClientTest>();

    public DataCenterClientTestService(ApDataCenter apDataCenter) {
        super();
        this.apDataCenter = apDataCenter;
    }

    public ApDataCenter getApDataCenter() {
        return apDataCenter;
    }

    public void setApDataCenter(ApDataCenter apDataCenter) {
        this.apDataCenter = apDataCenter;
    }

    public void startup() {
        for (int i = 0; i < 3; i++) {
            DataCenterClientTest test = new DataCenterClientTest(apDataCenter);
            tests.add(test);
            test.start();
        }
    }

    public void shutdown() {
        for (Iterator<DataCenterClientTest> i = tests.iterator(); i.hasNext(); ) {
            DataCenterClientTest test = i.next();
            i.remove();
            test.doDone();
        }
    }
}
