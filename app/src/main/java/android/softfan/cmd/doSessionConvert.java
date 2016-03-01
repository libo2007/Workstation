
package android.softfan.cmd;

import android.softfan.client.ClientCmd;
import android.softfan.util.SoftFanUtilException;
import android.softfan.util.xmlHelper;

import org.xmlpull.v1.XmlSerializer;


public class doSessionConvert extends ClientCmd {

	private String	sessionId;
	private String	remote;
	private String	newRemote;
	private String	vhost;

	public doSessionConvert() {
		super();
		this.setOperator("doSessionConvert");
	}

	public void BuildParameter(XmlSerializer content) throws SoftFanUtilException {
		xmlHelper.writeTextTag(content, "SESSIONID", sessionId);
		xmlHelper.writeTextTag(content, "REMOTE", remote);
		if (newRemote != null)
			xmlHelper.writeTextTag(content, "NEWREMOTE", newRemote);
		xmlHelper.writeTextTag(content, "VHOST", vhost);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

}
