package android.softfan.clientCmd;

import android.softfan.client.ClientCmdDo;
import android.softfan.client.ClientException;
import android.softfan.cmd.doSessionConvert;
import android.softfan.util.systemUtil;
import android.softfan.util.xmlHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class sessionLoginConvert {

	private String	sessionId;
	private String	remote;
	private String	newRemote;
	private String	vhost;

	private String	userCode;
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

	private String	portal;
	private String	fromSessionId;

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

	public String getNewRemote() {
		return newRemote;
	}

	public void setNewRemote(String newRemote) {
		this.newRemote = newRemote;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public String getUserName() {
		return userName;
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

	public String getUserType() {
		return userType;
	}

	public String getUserWorkspace() {
		return userWorkspace;
	}

	public String getVhost() {
		return vhost;
	}

	public String getPortal() {
		return portal;
	}

	public String getFromSessionId() {
		return fromSessionId;
	}

	public void login() throws ClientException {
		try {
			doSessionConvert loginCmd = new doSessionConvert();
			loginCmd.setSessionId(sessionId);
			loginCmd.setRemote(remote);
			loginCmd.setVhost(vhost);
			String xmlData = ClientCmdDo.DoExec(loginCmd);

			StringReader cmd_in_buffer = new StringReader(xmlData);
			try {
				InputSource is = new InputSource(cmd_in_buffer);

				DocumentBuilderFactory docBFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBFactory.newDocumentBuilder();

				Document doc = docBuilder.parse(is);

				Element docroot = doc.getDocumentElement();

				if (!docroot.getTagName().equals("RespondRoot"))
					throw new ClientException("信息格式错误");

				Node content = xmlHelper.findFirstElementByName(docroot, "Session");
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
			} finally {
				cmd_in_buffer.close();
			}
		} catch (ClientException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientException(systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			throw new ClientException(systemUtil.getErrorMessage(e));
		}
	}

}
