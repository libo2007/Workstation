
package android.softfan.dataCenter;

import android.softfan.dataCenter.task.DataCenterTaskCmd;

public interface IDataCenter {

	void sendCmd(DataCenterTaskCmd cmd) throws DataCenterException;

}
