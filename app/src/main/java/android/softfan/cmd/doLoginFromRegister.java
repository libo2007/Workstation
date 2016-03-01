package android.softfan.cmd;

import android.softfan.client.ClientCmd;
import android.softfan.util.SoftFanUtilException;
import android.softfan.util.xmlHelper;

import org.xmlpull.v1.XmlSerializer;

public class doLoginFromRegister extends ClientCmd {

	private String	userId;
	private String	userPassword;
	private String	userType;
	private String	vhost;
	private String	remote;
	private String	portal;

	public doLoginFromRegister() {
		super();
		this.setOperator("doLogin");
	}

	public void BuildParameter(XmlSerializer content) throws SoftFanUtilException {
		xmlHelper.writeTextTag(content, "USERID", userId);
		xmlHelper.writeTextTag(content, "PASSWORD", userPassword);
		xmlHelper.writeTextTag(content, "USERTYPE", userType);
		xmlHelper.writeTextTag(content, "VHOST", vhost);
		if (remote != null) {
			xmlHelper.writeTextTag(content, "REMOTE", remote);
		}
		if (portal != null) {
			xmlHelper.writeTextTag(content, "PORTAL", portal);
		}
	}

	public String getUserId() {
		if (userId == null)
			return "";
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		if (userPassword == null)
			return "";
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String loginType) {
		this.userType = loginType;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

}
