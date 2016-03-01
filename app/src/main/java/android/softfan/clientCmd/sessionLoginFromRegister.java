package android.softfan.clientCmd;

import android.softfan.client.ClientCmdDo;
import android.softfan.client.ClientException;
import android.softfan.cmd.doLoginFromRegister;
import android.softfan.util.systemUtil;
import android.softfan.util.xmlHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class sessionLoginFromRegister {

	private String	userCode;
	private String	userPassword;
	private String	userType;

	private String	userId;
	private String	userName;

	private String	userOrg;
	private String	userOrgCode;
	private String	userOrgName;
	private String	userOrgPath;
	private String	userOrgClassify;

	private String	userLocale;

	private String	userWorkspace;

	private String	vhost;

	private String	portal;

	private String	remote;

	private String	fromSessionId;

	private String	sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserOrg() {
		return userOrg;
	}

	public String getUserOrgCode() {
		return userOrgCode;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public String getUserOrgPath() {
		return userOrgPath;
	}

	public String getUserOrgClassify() {
		return userOrgClassify;
	}

	public String getUserLocale() {
		return userLocale;
	}

	public String getUserWorkspace() {
		return userWorkspace;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getVhost() {
		return vhost;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getRemote() {
		return remote;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public String getPortal() {
		return portal;
	}

	public String getFromSessionId() {
		return fromSessionId;
	}

	public void login() throws ClientException {
		try {
			doLoginFromRegister loginCmd = new doLoginFromRegister();
			loginCmd.setUserId(userCode);
			loginCmd.setUserPassword(userPassword);
			loginCmd.setUserType(userType);
			loginCmd.setVhost(vhost);
			loginCmd.setRemote(remote);
			loginCmd.setPortal(portal);

			String xmlData = ClientCmdDo.DoExec(loginCmd);

			StringReader cmd_in_buffer = new StringReader(xmlData);
			InputSource is = new InputSource(cmd_in_buffer);

			DocumentBuilderFactory docBFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBFactory.newDocumentBuilder();

			Document doc = docBuilder.parse(is);

			Element docroot = doc.getDocumentElement();

			if (!docroot.getTagName().equals("RespondRoot"))
				throw new ClientException("信息格式错误");

			Node content = xmlHelper.findFirstElementByName(docroot,"Session");
			if (content == null)
				throw new ClientException("信息格式错误");

			sessionId = xmlHelper.readTextTag(content, "SESSIONID");
			userId = xmlHelper.readTextTag(content, "USERID");
			userCode = xmlHelper.readTextTag(content, "USERCODE");
			userName = xmlHelper.readTextTag(content, "USERNAME");
			userLocale = xmlHelper.readTextTag(content, "USERLOCALE");
			userOrg = xmlHelper.readTextTag(content, "USERORG");
			userOrgCode = xmlHelper.readTextTag(content, "USERORGCODE");
			userOrgName = xmlHelper.readTextTag(content, "USERORGNAME");
			userOrgPath = xmlHelper.readTextTag(content, "USERORGPATH");
			userOrgClassify = xmlHelper.readTextTag(content, "USERORGCLASSIFY");
			userWorkspace = xmlHelper.readTextTag(content, "USERWORKSPACE");
			vhost = xmlHelper.readTextTag(content, "VHOST");
			remote = xmlHelper.readTextTag(content, "REMOTE");
			portal = xmlHelper.readTextTag(content, "PORTAL");
			fromSessionId = xmlHelper.readTextTag(content, "FROMSESSIONID");
			userType = xmlHelper.readTextTag(content, "USERTYPE");

		} catch (ClientException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			throw new ClientException(systemUtil.getErrorMessage(e));
		}
	}
}
