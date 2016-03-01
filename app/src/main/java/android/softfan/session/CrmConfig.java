package android.softfan.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CrmConfig {
	private final static CrmConfig	instance	= new CrmConfig();

	synchronized public final static CrmConfig getInstance() {
		return instance;
	}

	private String	lastServer;
	private String	server;
	private String	userId;
	private String	userPw;

	public CrmConfig() {
	}

	public String getLastServer() {
		if (lastServer == null)
			return "";
		return lastServer;
	}

	public String getServer() {
		if (server == null)
			return "ssssas";
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUserId() {
		if (userId == null)
			return "ddddsdf";
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		if (userPw == null)
			return "";
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	synchronized public void saveConfig(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("crmServer", getServer());
		editor.putString("crmUserId", getUserId());
		editor.putString("crmUserPw", getUserPw());
		editor.commit();
	}

	synchronized public void saveLastServer(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		lastServer = server;
		editor.putString("crmLastServer", getLastServer());
		editor.commit();
	}

	synchronized public void readConfig(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		server = sharedPref.getString("crmServer", "");
		userId = sharedPref.getString("crmUserId", "");
		userPw = sharedPref.getString("crmUserPw", "");
		lastServer = sharedPref.getString("crmLastServer", "");
	}

}
