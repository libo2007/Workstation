package android.softfan.cmd;

import android.softfan.client.ClientCmd;
import android.softfan.util.SoftFanUtilException;
import android.softfan.util.xmlHelper;

import org.xmlpull.v1.XmlSerializer;

public class doUnLogin extends ClientCmd {
	private String	sessionId;

	public doUnLogin() {
		super();
		this.setOperator("unLogin");
	}

	public void BuildParameter(XmlSerializer content) throws SoftFanUtilException {
		xmlHelper.writeTextTag(content, "SESSIONID", sessionId);
	}

	public String getSessionId() {
		if (sessionId == null)
			return "";
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
