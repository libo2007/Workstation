package android.softfan.db;

import android.database.sqlite.SQLiteDatabase;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

public class wf_Db_Connect {
	private IDbFactory		factory;
	private SQLiteDatabase	connection;
	private boolean			has_update;

	// private List clobList;
	// private List blobList;

	public wf_Db_Connect(SQLiteDatabase connection, IDbFactory factory) {
		this.has_update = false;
		this.connection = connection;
		this.factory = factory;
	}

	public boolean isIs_close() {
		return connection == null;
	}

	public boolean isHas_update() {
		return has_update;
	}

	public void setHas_update(boolean has_update) throws wf_Db_Exception {
		if (this.connection == null) {
			throw new wf_Db_Exception("数据连接已关闭,不能提交");
		}
		if (has_update) {
			if (!this.has_update) {
				this.connection.beginTransaction();
			}
		}
		this.has_update = has_update;
	}

	public SQLiteDatabase getConnect() {
		return this.connection;
	}

	private void freeLob() {
	}

	public void close() {
		try {
			freeLob();
		} finally {
			try {
				if (factory != null) {
					factory.free(this);
				}
			} finally {
				connection = null;
			}
		}
	}

	public void Submit() throws wf_Db_Exception {
		commit();
		freeLob();
	}

	private void commit() throws wf_Db_Exception {
		if (this.connection == null) {
			throw new wf_Db_Exception("数据连接已关闭,不能提交");
		}
		try {
			if (this.has_update) {
				this.connection.setTransactionSuccessful();
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e);
		} catch (Throwable e) {
			throw new wf_Db_Exception(systemUtil.getErrorMessage(e));
		}
	}

	public void Shutdown() {
		try {
			if (this.connection != null) {
				try {
					try {
						if (this.has_update) {
							this.connection.endTransaction();
						}
					} catch (Exception e) {
						wf_Log.sys_log(systemUtil.getErrorMessage(e));
					} catch (Throwable e1) {
						wf_Log.sys_log(systemUtil.getErrorMessage(e1));
					}
				} finally {
					freeLob();
				}
			}
		} finally {
			this.connection = null;
		}
	}

	public IDbFactory getFactory() {
		return factory;
	}
}