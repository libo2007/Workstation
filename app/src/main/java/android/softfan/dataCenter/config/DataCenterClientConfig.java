package android.softfan.dataCenter.config;

import android.softfan.util.systemUtil;
import android.softfan.util.textUnit;
import android.softfan.util.wf_Log;
import android.softfan.util.xmlHelper;

import org.w3c.dom.Node;

public class DataCenterClientConfig {

	private String		addr		= null;
	private int			port		= 10014;

	private String		org			= null;
	private String		ap			= null;
	private String		password	= "admin";

	private String		serverAp	= null;
	private String		serverOrg	= null;

	private Class<?>	pushThreadClass;
	private Class<?>	processClass;

	public void readXml(Node client) {
		addr = xmlHelper.readTextTag(client, "Addr");
		String sport = xmlHelper.readTextTag(client, "Port");
		if (!textUnit.StringIsEmpty(sport)) {
			port = Integer.parseInt(sport);
		}

		ap = xmlHelper.readTextTag(client, "Ap");
		org = xmlHelper.readTextTag(client, "Org");

		password = xmlHelper.readTextTag(client, "Password");

		serverAp = xmlHelper.readTextTag(client, "ServerAp");
		serverOrg = xmlHelper.readTextTag(client, "ServerOrg");

		String dataCenterPushThreadClassName = xmlHelper.readTextTag(client, "PushThreadClassName");
		if (!textUnit.StringIsEmpty(dataCenterPushThreadClassName)) {
			try {
				pushThreadClass = Class.forName(dataCenterPushThreadClassName, true, DataCenterClientConfig.class.getClassLoader());
			} catch (ClassNotFoundException e) {
				wf_Log.sys_log(systemUtil.getErrorMessage(e));
			}
		}

		String dataCenterProcessClassName = xmlHelper.readTextTag(client, "ProcessClassName");
		if (!textUnit.StringIsEmpty(dataCenterProcessClassName)) {
			try {
				processClass = Class.forName(dataCenterProcessClassName, true, DataCenterClientConfig.class.getClassLoader());
			} catch (ClassNotFoundException e) {
				wf_Log.sys_log(systemUtil.getErrorMessage(e));
			}
		}
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getAp() {
		return ap;
	}

	public void setAp(String ap) {
		this.ap = ap;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerAp() {
		return serverAp;
	}

	public void setServerAp(String serverAp) {
		this.serverAp = serverAp;
	}

	public String getServerOrg() {
		return serverOrg;
	}

	public void setServerOrg(String serverOrg) {
		this.serverOrg = serverOrg;
	}

	public Class<?> getPushThreadClass() {
		return pushThreadClass;
	}

	public void setPushThreadClass(Class<?> pushThreadClass) {
		this.pushThreadClass = pushThreadClass;
	}

	public Class<?> getProcessClass() {
		return processClass;
	}

	public void setProcessClass(Class<?> processClass) {
		this.processClass = processClass;
	}

}
