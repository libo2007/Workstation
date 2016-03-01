package android.softfan.db;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

public class wf_Db_Factory implements IDbFactory {

	private String	databasetype	= "sqlite";
	@SuppressLint("UseValueOf")
	private Integer	submitLock		= new Integer(1);

	public wf_Db_Factory() {
	}

	public String getDatabaseType() {
		return databasetype;
	}

	public wf_Db_Connect get() throws wf_Db_Exception {
		try {
			SQLiteDatabase conn = wf_Db_Server.getServer().getWritableDatabase();
			return new wf_Db_Connect(conn, this);
		} catch (Exception e) {
			throw new wf_Db_Exception(systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			throw new wf_Db_Exception(systemUtil.getErrorMessage(e));
		}
	}

	public void free(wf_Db_Connect t_connect) {
		try {
			SQLiteDatabase connect = t_connect.getConnect();
			if (connect != null) {
				try {
					t_connect.Shutdown();
				} finally {
					connect.close();
				}
			}
		} catch (RuntimeException e) {
			wf_Log.sys_log(systemUtil.getErrorMessage(e));
		} catch (Exception e) {
			wf_Log.sys_log(systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			wf_Log.sys_log(systemUtil.getErrorMessage(e));
		}
	}

	public Object getSubmitLock() {
		return submitLock;
	}

}