
package android.softfan.dataCenter;

import android.softfan.dataCenter.task.DataCenterTaskCmd;

public interface IDataCenterProcess {

	void start();

	void processMsg(DataCenterRun dataCenterRun, DataCenterTaskCmd cmd) throws DataCenterException;

	void stop();
}
