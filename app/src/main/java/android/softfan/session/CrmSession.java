package android.softfan.session;

import android.softfan.client.ClientCmdDo;
import android.softfan.client.ClientException;
import android.softfan.clientCmd.sessionLoginFromRegister;
import android.softfan.clientCmd.sessionOnLine;
import android.softfan.clientCmd.sessionUnLogin;
import android.softfan.service.CrmShareData;
import android.softfan.util.textUnit;

public class CrmSession {
	private static final CrmSession	instance	= new CrmSession();

	public synchronized final static CrmSession getInstance() {
		return instance;
	}

	private String				userId;
	private String				userPw;

	private String				sessionId;
	private String				userName;
	private String				userOrgPath;

	private boolean				crmOnline;

	private CrmSessionThread	thread;

	public CrmSession() {
	}

	public void reset(String server, String userId, String userPw) {
		synchronized (this) {
			this.userId = userId;
			this.userPw = userPw;
			this.sessionId = null;
			this.userName = null;
			this.userOrgPath = null;
			this.crmOnline = false;

			CrmShareData.getInstance().setServer(server);
			CrmShareData.getInstance().setSessionId(null);
			ClientCmdDo.setSESSIONSERVERADDR(server);
		}
	}

	public void login() throws ClientException {
		synchronized (this) {
			if (textUnit.StringIsEmpty(userId)) {
				return;
			}
			if (textUnit.StringIsEmpty(userPw)) {
				return;
			}

			sessionLoginFromRegister login = new sessionLoginFromRegister();
			login.setUserCode(userId);
			login.setUserPassword(userPw);
			login.setUserType("MANAGER");
			login.setVhost("localhost");
			login.setRemote("AC");
			login.login();

			sessionId = login.getSessionId();
			userName = login.getUserName();
			userOrgPath = login.getUserOrgPath();
			crmOnline = true;

			saveConfig();
		}
	}

	public void unLogin() throws ClientException {
		synchronized (this) {
			if (!textUnit.StringIsEmpty(sessionId)) {
				sessionUnLogin unlogin = new sessionUnLogin();
				unlogin.setSessionId(sessionId);
				unlogin.unLogin();

				sessionId = null;
				userName = null;
				userOrgPath = null;
				crmOnline = false;
			}
		}
	}

	public boolean onLine() throws ClientException {
		synchronized (this) {
			if (!textUnit.StringIsEmpty(sessionId)) {
				sessionOnLine online = new sessionOnLine();
				online.setSessionId(sessionId);
				crmOnline = online.onLine();
				if (crmOnline) {
					return crmOnline;
				}
			}
			crmOnline = false;
			sessionId = null;
			return false;
		}
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserOrgPath() {
		return userOrgPath;
	}

	public void start() {
		synchronized (this) {
			if (thread == null) {
				thread = new CrmSessionThread(this);
				thread.start();
			}
		}
	}

	public void stop() {
		synchronized (this) {
			thread.shutdown();
			thread = null;
		}
	}

	public void refresh() {
		synchronized (this) {
			if (thread != null) {
				thread.refresh();
			}
		}
	}

	public boolean isCrmOnline() {
		return crmOnline;
	}

	public void saveConfig() {
		CrmShareData.getInstance().setSessionId(getSessionId());
	}

}
