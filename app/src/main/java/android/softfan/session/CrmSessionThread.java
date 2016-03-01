package android.softfan.session;

import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;


public class CrmSessionThread extends Thread {
	private static int	timeOut	= 60 * 1000;

	private CrmSession	session;
	private boolean		done;

	public CrmSessionThread(CrmSession session) {
		super("CrmSessionThread");
		this.session = session;
		this.done = false;
	}

	public CrmSession getSession() {
		return session;
	}

	public void run() {
		try {
			while (!done) {
				try {
					if (!session.onLine()) {
						session.login();
					}
					synchronized (this) {
						try {
							this.wait(timeOut);
						} catch (InterruptedException e) {
						}
					}
				} catch (Exception e) {
					wf_Log.sys_log(systemUtil.getErrorMessage(e));
				} catch (Throwable e) {
					wf_Log.sys_log(systemUtil.getErrorMessage(e));
				}
			}
		} finally {
			try {
				session.unLogin();
			} catch (Exception e) {
				wf_Log.sys_log(systemUtil.getErrorMessage(e));
			} catch (Throwable e) {
				wf_Log.sys_log(systemUtil.getErrorMessage(e));
			}
			session = null;
		}
	}

	public void shutdown() {
		synchronized (this) {
			done = true;
			this.notifyAll();
		}
	}

	public void refresh() {
		synchronized (this) {
			this.notifyAll();
		}
	}

}