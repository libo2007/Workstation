
package android.softfan.dataCenter.send;

import android.softfan.client.ClientCmdDo;
import android.softfan.dataCenter.ApDataCenter;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.dataCenter.task.DataCenterTaskException;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendRun extends Thread {

	private DataCenterRun		dataCenter;

	private DataOutputStream	outStream;

	public SendRun(DataCenterRun dataCenter, DataOutputStream outStream) {
		super("DataCenter-SendRun");
		this.dataCenter = dataCenter;
		this.outStream = outStream;
	}

	private DataCenterTaskCmd makeKeepLineCmd() {
		DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
		retcmd.setSeq(Long.toString(System.currentTimeMillis()));
		retcmd.setCmd("online");
		retcmd.setLevel(1);
		return retcmd;
	}

	public void run() {
		ApDataCenter apDataCenter = this.dataCenter.getApDataCenter();
		try {
			sendKeepLine();
			long keeptime = System.currentTimeMillis();
			long logtime = keeptime;
			long t1, t2;
			int sendcount = 0;
			while (this.dataCenter.isRuning()) {
				DataCenterTaskCmd cmd = null;
				try {
					cmd = apDataCenter.popSendCmd();
					if (cmd != null) {
						DataCenterTaskCmd retrycmd = cmd;
						try {
							cmd.onSend();
							t1 = System.currentTimeMillis();
							sendData(cmd);
							retrycmd = null;
							sendcount++;
							t2 = System.currentTimeMillis();
							wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】发送【" + cmd.getCmd() + "  " + cmd.getSeq() + "】耗时:" + (t2 - t1) + "毫秒");
							if (cmd.isDoResponse()) {
								apDataCenter.addResponseWaitCmd(cmd);
							} else {
								try {
									cmd.freeData();
								} catch (Exception e) {
									wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】发送器错误:" + systemUtil.getErrorMessage(e));
								} catch (Throwable e) {
									wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】发送器错误:" + systemUtil.getErrorMessage(e));
								}
							}
						} finally {
							if (retrycmd != null) {
								apDataCenter.addSendRetryCmd(retrycmd);
								retrycmd = null;
							}
						}
					}
				} catch (Exception e) {
					this.dataCenter.onError(1);
					wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】发送器错误:" + systemUtil.getErrorMessage(e));
				} catch (Throwable e) {
					this.dataCenter.onError(1);
					wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】发送器错误:" + systemUtil.getErrorMessage(e));
				}
				if (this.dataCenter.isRuning()) {
					long t = System.currentTimeMillis();

					long minwait = 15000;

					//空闲发送心跳
					if (cmd == null) {
						if ((t - keeptime) >= 15000) {
							sendKeepLine();
							keeptime = t;
						} else {
							minwait = Math.min(15000 - (t - keeptime), minwait);
						}
					} else {
						keeptime = t;
					}

					if ((t - logtime) >= 30000) {
						wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】   总计发送：" + sendcount + "   等待中：" + apDataCenter.sizeOfSendCmd());
						logtime = t;
					} else {
						minwait = Math.min(30000 - (t - logtime), minwait);
					}

					if (cmd == null) {
						if (minwait < 100) {
							minwait = 100;
						}
						apDataCenter.waitSend(minwait);
					}
				}
			}
		} finally {
			this.dataCenter.setSenderFinished(true);
			this.dataCenter = null;
			this.outStream = null;
		}
	}

	private void sendKeepLine() {
		try {
			DataCenterTaskCmd cmd = makeKeepLineCmd();
			sendData(cmd);
		} catch (Exception e) {
			this.dataCenter.onError(1);
			wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】接口错误:" + systemUtil.getErrorMessage(e));
		} catch (Throwable e) {
			this.dataCenter.onError(1);
			wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】接口错误:" + systemUtil.getErrorMessage(e));
		}
	}

	private void sendData(DataCenterTaskCmd cmd) throws DataCenterTaskException, IOException {
		byte[] command;
		if (this.dataCenter.getApDataCenter().isXmlMsg()) {
			command = cmd.buildXmlCommand();
		} else {
			command = cmd.buildCommand();
		}
		cmd.setRunTime(System.currentTimeMillis());
		int length = command.length;
		outStream.writeInt(0xF3B6);
		outStream.flush();
		outStream.writeInt(length);
		outStream.flush();
		int pos = 0;
		int count;
		while (pos < length) {
			count = length - pos;
			if (count > ClientCmdDo.Socket_Buf_Size) {
				count = ClientCmdDo.Socket_Buf_Size;
			}
			outStream.write(command, pos, count);
			outStream.flush();
			pos += count;
		}
	}

}