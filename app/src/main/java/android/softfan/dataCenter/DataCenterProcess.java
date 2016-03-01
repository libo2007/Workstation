
package android.softfan.dataCenter;

import android.softfan.dataCenter.task.DataCenterTaskCmd;

public class DataCenterProcess implements IDataCenterProcess {

	public void processMsg(DataCenterRun dataCenterRun, DataCenterTaskCmd cmd) throws DataCenterException {
		if (cmd.isHasResponse()) {
			DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
			retcmd.setSeq(cmd.getSeq());
			retcmd.setCmd("response");
			dataCenterRun.sendResponseCmd(retcmd);
		}
	}

	public void start() {
	}

	public void stop() {
	}
}
