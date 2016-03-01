package android.softfan.clientCmd;

import android.softfan.client.ClientCmdDo;
import android.softfan.client.ClientException;
import android.softfan.cmd.doUnLogin;
import android.softfan.util.systemUtil;

public class sessionUnLogin {

	private String	sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void unLogin() throws ClientException {
		try {
			doUnLogin unLoginCmd = new doUnLogin();
			unLoginCmd.setSessionId(sessionId);
			ClientCmdDo.DoExec(unLoginCmd);
		} catch (ClientException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			throw new ClientException(systemUtil.getErrorMessage(e));
		}
	}

}
