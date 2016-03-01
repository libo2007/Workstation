package android.softfan.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CrmShareData {
	private static CrmShareData	instance	= new CrmShareData();

	synchronized public final static CrmShareData getInstance() {
		return instance;
	}

	private String	server;
	private String	crmUrl;
	private String	sessionId;

	public CrmShareData() {
	}

	public void setServer(String server) {
		this.server = server;
		this.crmUrl = null;
	}

	public String getServer() {
		return server;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getCrmUrl() {
		if (crmUrl == null) {
			crmUrl = "http://" + this.server + ":8580/SoftfanExt/Public/android/book/";
		}
		return crmUrl;
	}

	public void readConfig(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		server = sharedPref.getString("crmServer", "");
		sessionId = sharedPref.getString("crmSessionId", "");
		crmUrl = null;
	}
}
