
package android.softfan.dataCenter;

import android.softfan.dataCenter.receive.ReceiveRun;
import android.softfan.dataCenter.send.SendRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DataCenterRun implements IDataCenter {

	private IDataCenterProcess	dataCenterProcess;

	private ApDataCenter		apDataCenter;

	private String				hostAddr;

	private DataInputStream		inputStream;
	private DataOutputStream	outStream;
	private boolean				done;
	private boolean				doneFinished;

	private int					connState;

	private ReceiveRun			receive;
	private boolean				recverFinished;

	private SendRun				send;
	private boolean				senderFinished;

	private boolean				serverMode;

	public DataCenterRun(IDataCenterProcess dataCenterProcess, ApDataCenter apDataCenter, String hostaddr, DataInputStream inputStream, DataOutputStream outStream, boolean serverMode) {
		this.dataCenterProcess = dataCenterProcess;
		this.apDataCenter = apDataCenter;
		this.hostAddr = hostaddr;
		this.inputStream = inputStream;
		this.outStream = outStream;
		this.serverMode = serverMode;
		this.done = false;
		this.connState = 0;
	}

	public boolean isServerMode() {
		return serverMode;
	}

	public ApDataCenter getApDataCenter() {
		return apDataCenter;
	}

	public String getHost() {
		if (this.apDataCenter != null) {
			return this.apDataCenter.getSvrApCode() + "," + hostAddr;
		}
		return hostAddr;
	}

	public String getHostAddr() {
		return hostAddr;
	}

	public void run() {
		try {
			apDataCenter.setDataCenter(this);
			this.done = false;
			this.doneFinished = false;
			try {
				synchronized (this) {
					receive = new ReceiveRun(this, inputStream);
					send = new SendRun(this, outStream);
				}
				recverFinished = true;
				senderFinished = true;
				try {
					try {
						//启动
						send.start();
						senderFinished = false;

						receive.start();
						recverFinished = false;

						//执行
						while (isRuning()) {
							DataCenterTaskCmd cmd = apDataCenter.popReceiveCmd();
							if (cmd != null) {
								try {
									if ("response".equals(cmd.getCmd())) {
										//消息收到通知
										DataCenterTaskCmd resCmd = apDataCenter.popResponseWaitCmd(cmd.getSeq());
										if (resCmd != null) {
											wf_Log.sys_log("数据中心收到：" + cmd.getCmd() + "  " + cmd.getSeq() + " 应答来源：" + resCmd.getCmd());
											try {
												resCmd.onResponse(cmd);
											} finally {
												resCmd.freeData();
											}
										} else {
											wf_Log.sys_log("数据中心收到：" + cmd.getCmd() + "  " + cmd.getSeq() + " 没有应答源");
										}
									} else {
										//消息收到通知
										wf_Log.sys_log("数据中心【" + getHost() + "】收到：" + cmd.getCmd() + "  " + cmd.getSeq());
										if (dataCenterProcess != null) {
											dataCenterProcess.processMsg(this, cmd);
										} else {
											if (cmd.isHasResponse()) {
												DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
												retcmd.setSeq(cmd.getSeq());
												retcmd.setCmd("response");
												sendResponseCmd(retcmd);
											}
										}
									}
								} finally {
									cmd.freeData();
								}
							} else {
								apDataCenter.waitReceive(30000);
							}
						}
					} finally {
						//关闭
						apDataCenter.notifySelfAll();
						while (true) {
							if (senderFinished && recverFinished) {
								break;
							}
							synchronized (this) {
								try {
									this.wait(30000);
								} catch (InterruptedException e) {
								}
							}
						}
					}
				} finally {
					synchronized (this) {
						receive = null;
						send = null;
					}
				}
			} catch (Exception e) {
				this.onError(1);
				wf_Log.sys_log("数据中心处理器错误(" + getHost() + "):" + systemUtil.getErrorMessage(e));
			} catch (Throwable e) {
				this.onError(1);
				wf_Log.sys_log("数据中心处理器错误(" + getHost() + "):" + systemUtil.getErrorMessage(e));
			}
		} finally {
			doneFinished = true;
			apDataCenter.setDataCenter(null);
			synchronized (this) {
				this.notifyAll();
			}
			inputStream = null;
			outStream = null;
		}
	}

	public boolean isDoneFinished() {
		return doneFinished;
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

	public boolean isRecverFinished() {
		return recverFinished;
	}

	public void setRecverFinished(boolean recverFinished) {
		synchronized (this) {
			this.recverFinished = recverFinished;
			this.notifyAll();
		}
	}

	public boolean isSenderFinished() {
		return senderFinished;
	}

	public void setSenderFinished(boolean senderFinished) {
		synchronized (this) {
			this.senderFinished = senderFinished;
			this.notifyAll();
		}
	}

	public boolean isRuning() {
		if (this.isDone()) {
			return false;
		}
		return connState == 0;
	}

	public void onError(int code) {
		connState = 1;
	}

	public void sendCmd(DataCenterTaskCmd cmd) throws DataCenterException {
		if (!isRuning()) {
			throw new DataCenterException("数据中心发送接口已关闭！");
		}
		apDataCenter.addSendCmd(cmd);
	}

	public void sendResponseCmd(DataCenterTaskCmd cmd) throws DataCenterException {
		if (!isRuning()) {
			throw new DataCenterException("数据中心发送接口已关闭！");
		}
		apDataCenter.addSendResponseCmd(cmd);
	}

}