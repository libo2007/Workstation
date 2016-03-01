
package android.softfan.dataCenter.task;

import android.softfan.dataCenter.DataCenterException;

public interface IDataCenterNotify {

	void onSend(DataCenterTaskCmd selfCmd) throws DataCenterException;

	void onResponse(DataCenterTaskCmd selfCmd, DataCenterTaskCmd responseCmd) throws DataCenterException;

	void onFree(DataCenterTaskCmd selfCmd);

	void onTimeout(DataCenterTaskCmd selfCmd);

}
