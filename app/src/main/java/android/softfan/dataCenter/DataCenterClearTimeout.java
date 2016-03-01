package android.softfan.dataCenter;

import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataCenterClearTimeout extends Thread {

	private ApDataCenter	apDataCenter;

	private boolean			done;

	public DataCenterClearTimeout(ApDataCenter apDataCenter) {
		super("DataCenterClearTimeout");
		this.apDataCenter = apDataCenter;
	}

	public void run() {
		try {
			while (!isDone()) {
				long minwait = clearTimeout();
				if (minwait < 100) {
					minwait = 100;
				}
				synchronized (this) {
					try {
						this.wait(minwait);
					} catch (InterruptedException e) {
					}
				}
			}
		} finally {
			apDataCenter = null;
			synchronized (this) {
				this.notifyAll();
			}
		}
	}

	public long clearTimeout() {
		long minwait = 120000;
		try {
			List<DataCenterTaskCmd> cmds = new ArrayList<DataCenterTaskCmd>();
			try {
				//清理应答等待超时的命令
				long time = System.currentTimeMillis();
				apDataCenter.fillWaitCmds(cmds);
				for (Iterator<DataCenterTaskCmd> i = cmds.iterator(); i.hasNext();) {
					DataCenterTaskCmd cmd = i.next();
					if (this.isDone()) {
						break;
					}
					if (!cmd.isResponsed()) {
						long timeout = cmd.getTimeout();
						if (timeout == 0) {
							timeout = 60000;//3600000;
						}
						if ((time - cmd.getRunTime()) >= timeout) {
							if (apDataCenter.removeResponseWaitCmd(cmd.getSeq())) {
								wf_Log.sys_log("数据中心应答等待超时：" + cmd.getCmd() + "  " + cmd.getSeq());
								cmd.onTimeout();
								cmd.freeData();
							}
						} else {
							minwait = Math.min(timeout - (time - cmd.getRunTime()), minwait);
						}
					}
				}

				if (this.isDone()) {
					return minwait;
				}

				time = System.currentTimeMillis();
				//清理接收队列超时的命令(仅处理收到的命令，应答不清理)
				for (int level = 5; level >= 1; level--) {
					if (this.isDone()) {
						break;
					}
					cmds.clear();
					apDataCenter.fillReceiveCmds(cmds, level);
					try {
						time = System.currentTimeMillis();
						for (Iterator<DataCenterTaskCmd> i = cmds.iterator(); i.hasNext();) {
							DataCenterTaskCmd cmd = i.next();
							if (this.isDone()) {
								break;
							}
							long timeout = cmd.getTimeout();
							if (timeout > 0) {
								if ((time - cmd.getCreateTime()) >= timeout) {
									if (apDataCenter.removeSendCmd(cmd, level)) {
										wf_Log.sys_log("数据中心待处理超时：" + cmd.getCmd() + "  " + cmd.getSeq());
										cmd.onTimeout();
										cmd.freeData();
									}
								} else {
									minwait = Math.min(timeout - (time - cmd.getRunTime()), minwait);
								}
							}
						}
					} finally {
						cmds.clear();
					}
				}

				time = System.currentTimeMillis();
				//清理发送队列超时的命令
				for (int level = 5; level >= -1; level--) {
					if (this.isDone()) {
						break;
					}
					cmds.clear();
					apDataCenter.fillSendCmds(cmds, level);
					if (this.isDone()) {
						break;
					}
					try {
						time = System.currentTimeMillis();
						for (Iterator<DataCenterTaskCmd> i = cmds.iterator(); i.hasNext();) {
							DataCenterTaskCmd cmd = i.next();
							if (this.isDone()) {
								break;
							}
							long timeout = cmd.getTimeout();
							if (timeout > 0) {
								if ((time - cmd.getCreateTime()) >= timeout) {
									if (apDataCenter.removeSendCmd(cmd, level)) {
										wf_Log.sys_log("数据中心待发送超时：" + cmd.getCmd() + "  " + cmd.getSeq());
										cmd.onTimeout();
										cmd.freeData();
									}
								} else {
									minwait = Math.min(timeout - (time - cmd.getRunTime()), minwait);
								}
							}
						}
					} finally {
						cmds.clear();
					}
				}
			} finally {
				cmds.clear();
				cmds = null;
			}
		} catch (Exception e) {
			wf_Log.sys_log("数据中心请求超时处理器错误：" + systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			wf_Log.sys_log("数据中心请求超时处理器错误：" + systemUtil.getErrorMessage(e));
		}
		return minwait;
	}

	public void doDone() {
		synchronized (this) {
			done = true;
			this.notifyAll();
		}
	}

	public boolean isDone() {
		return done;
	}

	public boolean isFinished() {
		return apDataCenter == null;
	}
}
