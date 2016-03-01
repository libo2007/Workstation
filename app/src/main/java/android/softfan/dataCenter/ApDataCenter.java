
package android.softfan.dataCenter;

import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ApDataCenter {

	private Integer								responseLocked		= new Integer(1);
	private HashMap<String, DataCenterTaskCmd>	responseWaitCmds	= new HashMap<String, DataCenterTaskCmd>();

	private Integer								receiveLocked		= new Integer(1);
	private List<DataCenterTaskCmd>				receiveResponseCmds	= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				receiveCmds_1		= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				receiveCmds_2		= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				receiveCmds_3		= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				receiveCmds_4		= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				receiveCmds_5		= new LinkedList<DataCenterTaskCmd>();

	private Integer								sendLocked			= new Integer(1);
	private List<DataCenterTaskCmd>				sendCmds_response	= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				sendCmds_retry		= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				sendCmds_1			= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				sendCmds_2			= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				sendCmds_3			= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				sendCmds_4			= new LinkedList<DataCenterTaskCmd>();
	private List<DataCenterTaskCmd>				sendCmds_5			= new LinkedList<DataCenterTaskCmd>();

	//本地
	private String								apCode;
	//本地
	private String								orgId;

	//服务器
	private String								svrApCode;
	//服务器
	private String								svrOrgId;

	private boolean								cppMode;
	private boolean								xmlMsg;

	private IDataCenter							dataCenter;

	private String								hostAddr;
	private int									hostPort;

	//同步时间戳
	private Integer								currTimestampLocked	= new Integer(1);
	private long								currTimestamp;

	private DataCenterClearTimeout				timeoutProcess;

	private boolean								onLine;

	public ApDataCenter(String apCode, String orgId, String svrApCode, String svrOrgId, IDataCenter dataCenter) {
		this.apCode = apCode;
		this.orgId = orgId;
		this.svrApCode = svrApCode;
		this.svrOrgId = svrOrgId;
		this.dataCenter = dataCenter;
		this.timeoutProcess = new DataCenterClearTimeout(this);
		this.timeoutProcess.start();
	}

	public boolean isCppMode() {
		return cppMode;
	}

	public void setCppMode(boolean cppMode) {
		this.cppMode = cppMode;
	}

	public void setXmlMsg(boolean xmlMsg) {
		this.xmlMsg = xmlMsg;
	}

	public boolean isXmlMsg() {
		return xmlMsg;
	}

	public void freeData() {
		if (timeoutProcess != null) {
			timeoutProcess.doDone();
			while (true) {
				if (timeoutProcess.isFinished()) {
					break;
				}
				synchronized (timeoutProcess) {
					try {
						timeoutProcess.wait(3000);
					} catch (InterruptedException e) {
					}
				}
			}
			timeoutProcess = null;
		}
		freeCmds(sendCmds_response);
		freeCmds(sendCmds_retry);
		freeCmds(sendCmds_1);
		freeCmds(sendCmds_2);
		freeCmds(sendCmds_3);
		freeCmds(sendCmds_4);
		freeCmds(sendCmds_5);
		freeCmds(receiveResponseCmds);
		freeCmds(receiveCmds_1);
		freeCmds(receiveCmds_2);
		freeCmds(receiveCmds_3);
		freeCmds(responseWaitCmds);
	}

	public boolean isOnLine() {
		return onLine;
	}

	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}

	public void setHost(String hostAddr, int hostPort) {
		this.hostAddr = hostAddr;
		this.hostPort = hostPort;
	}

	public String getHostAddr() {
		return hostAddr;
	}

	public int getHostPort() {
		return hostPort;
	}

	public String getApCode() {
		return apCode;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getSvrApCode() {
		return svrApCode;
	}

	public String getSvrOrgId() {
		return svrOrgId;
	}

	private void freeCmds(HashMap<String, DataCenterTaskCmd> cmds) {
		List<DataCenterTaskCmd> tmpcmds = new ArrayList<DataCenterTaskCmd>();
		try {
			synchronized (cmds) {
				tmpcmds.addAll(cmds.values());
				cmds.clear();
			}
			for (Iterator<DataCenterTaskCmd> i = tmpcmds.iterator(); i.hasNext();) {
				DataCenterTaskCmd cmd = i.next();
				try {
					cmd.freeData();
				} catch (Exception e) {
					wf_Log.sys_log("数据中心【" + getSvrApCode() + "," + getHostAddr() + "】错误:" + systemUtil.getErrorMessage(e));
				} catch (Throwable e) {
					wf_Log.sys_log("数据中心【" + getSvrApCode() + "," + getHostAddr() + "】错误:" + systemUtil.getErrorMessage(e));
				}
			}
		} finally {
			tmpcmds.clear();
			tmpcmds = null;
		}
	}

	private void freeCmds(List<DataCenterTaskCmd> cmds) {
		List<DataCenterTaskCmd> tmpcmds = new ArrayList<DataCenterTaskCmd>();
		try {
			synchronized (cmds) {
				tmpcmds.addAll(cmds);
				cmds.clear();
			}
			for (Iterator<DataCenterTaskCmd> i = tmpcmds.iterator(); i.hasNext();) {
				DataCenterTaskCmd cmd = i.next();
				try {
					cmd.freeData();
				} catch (Exception e) {
					wf_Log.sys_log("数据中心【" + getSvrApCode() + "," + getHostAddr() + "】错误:" + systemUtil.getErrorMessage(e));
				} catch (Throwable e) {
					wf_Log.sys_log("数据中心【" + getSvrApCode() + "," + getHostAddr() + "】错误:" + systemUtil.getErrorMessage(e));
				}
			}
		} finally {
			tmpcmds.clear();
			tmpcmds = null;
		}
	}

	public IDataCenter getDataCenter() {
		synchronized (this) {
			return dataCenter;
		}
	}

	public void setDataCenter(IDataCenter dataCenter) {
		synchronized (this) {
			this.dataCenter = dataCenter;
		}
	}

	public long getCurrTimestamp() {
		return currTimestamp;
	}

	public void markCurrTimestamp() {
		synchronized (currTimestampLocked) {
			this.currTimestamp = System.currentTimeMillis();
			currTimestampLocked.notifyAll();
		}
	}

	public void waitMarkTimestamp(long dirtyTimestamp, long minwait) {
		synchronized (currTimestampLocked) {
			if (this.currTimestamp <= dirtyTimestamp) {
				try {
					currTimestampLocked.wait(minwait);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void fillWaitCmds(List<DataCenterTaskCmd> cmds) {
		synchronized (responseLocked) {
			cmds.addAll(responseWaitCmds.values());
		}
	}

	public DataCenterTaskCmd popResponseWaitCmd(String seq) {
		synchronized (responseLocked) {
			DataCenterTaskCmd cmd = responseWaitCmds.remove(seq);
			if (cmd != null) {
				cmd.markResponse();
			}
			return cmd;
		}
	}

	public void addResponseWaitCmd(DataCenterTaskCmd taskcmd) {
		synchronized (responseLocked) {
			responseWaitCmds.put(taskcmd.getSeq(), taskcmd);
		}
	}

	public boolean removeResponseWaitCmd(String seq) {
		synchronized (responseLocked) {
			DataCenterTaskCmd cmd = responseWaitCmds.remove(seq);
			if (cmd == null) {
				return false;
			}
			if (cmd.isResponsed()) {
				return false;
			}
			return true;
		}
	}

	public void fillReceiveCmds(List<DataCenterTaskCmd> cmds, int level) {
		synchronized (receiveLocked) {
			switch (level) {
				case 0:
					cmds.addAll(receiveResponseCmds);
					break;
				case 1:
					cmds.addAll(receiveCmds_1);
					break;
				case 2:
					cmds.addAll(receiveCmds_2);
					break;
				case 3:
					cmds.addAll(receiveCmds_3);
					break;
				case 4:
					cmds.addAll(receiveCmds_4);
					break;
				default:
					cmds.addAll(receiveCmds_5);
					break;
			}
		}
	}

	public boolean removeReceiveCmd(DataCenterTaskCmd cmd, int level) {
		synchronized (receiveLocked) {
			switch (level) {
				case 0:
					return receiveResponseCmds.remove(cmd);
				case 1:
					return receiveCmds_1.remove(cmd);
				case 2:
					return receiveCmds_2.remove(cmd);
				case 3:
					return receiveCmds_3.remove(cmd);
				case 4:
					return receiveCmds_4.remove(cmd);
				default:
					return receiveCmds_5.remove(cmd);
			}
		}
	}

	public int sizeOfReceiveCmd() {
		synchronized (receiveLocked) {
			int c = 0;
			c += receiveResponseCmds.size();
			c += receiveCmds_1.size();
			c += receiveCmds_2.size();
			c += receiveCmds_3.size();
			c += receiveCmds_4.size();
			c += receiveCmds_5.size();
			return c;
		}
	}

	public DataCenterTaskCmd popReceiveCmd() {
		synchronized (receiveLocked) {
			if (!receiveResponseCmds.isEmpty()) {
				return receiveResponseCmds.remove(0);
			}
			if (!receiveCmds_1.isEmpty()) {
				return receiveCmds_1.remove(0);
			}
			if (!receiveCmds_2.isEmpty()) {
				return receiveCmds_2.remove(0);
			}
			if (!receiveCmds_3.isEmpty()) {
				return receiveCmds_3.remove(0);
			}
			if (!receiveCmds_4.isEmpty()) {
				return receiveCmds_4.remove(0);
			}
			if (!receiveCmds_5.isEmpty()) {
				return receiveCmds_5.remove(0);
			}
			return null;
		}
	}

	public void addReceiveResponseCmd(DataCenterTaskCmd cmd) {
		if (cmd == null) {
			return;
		}
		synchronized (receiveLocked) {
			receiveResponseCmds.add(cmd);
			receiveLocked.notifyAll();
		}
	}

	public void addReceiveCmd(DataCenterTaskCmd cmd) {
		if (cmd == null) {
			return;
		}
		synchronized (receiveLocked) {
			switch (cmd.getLevel()) {
				case 1:
					receiveCmds_1.add(cmd);
					break;
				case 2:
					receiveCmds_2.add(cmd);
					break;
				case 3:
					receiveCmds_3.add(cmd);
					break;
				case 4:
					receiveCmds_4.add(cmd);
					break;
				default:
					receiveCmds_5.add(cmd);
					break;
			}
			receiveLocked.notifyAll();
		}
	}

	public void waitReceive(long minwait) {
		synchronized (receiveLocked) {
			if (sizeOfReceiveCmd() < 1) {
				try {
					receiveLocked.wait(minwait);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void fillSendCmds(List<DataCenterTaskCmd> cmds, int level) {
		synchronized (sendLocked) {
			switch (level) {
				case -1:
					cmds.addAll(sendCmds_response);
					break;
				case 0:
					cmds.addAll(sendCmds_retry);
					break;
				case 1:
					cmds.addAll(sendCmds_1);
					break;
				case 2:
					cmds.addAll(sendCmds_2);
					break;
				case 3:
					cmds.addAll(sendCmds_3);
					break;
				case 4:
					cmds.addAll(sendCmds_4);
					break;
				default:
					cmds.addAll(sendCmds_5);
					break;
			}
		}
	}

	public boolean removeSendCmd(DataCenterTaskCmd cmd, int level) {
		synchronized (sendLocked) {
			switch (level) {
				case -1:
					return sendCmds_response.remove(cmd);
				case 0:
					return sendCmds_retry.remove(cmd);
				case 1:
					return sendCmds_1.remove(cmd);
				case 2:
					return sendCmds_2.remove(cmd);
				case 3:
					return sendCmds_3.remove(cmd);
				case 4:
					return sendCmds_4.remove(cmd);
				default:
					return sendCmds_5.remove(cmd);
			}
		}
	}

	public int sizeOfSendCmd() {
		synchronized (sendLocked) {
			int c = 0;
			c += sendCmds_response.size();
			c += sendCmds_retry.size();
			c += sendCmds_1.size();
			c += sendCmds_2.size();
			c += sendCmds_3.size();
			c += sendCmds_4.size();
			c += sendCmds_5.size();
			return c;
		}
	}

	public DataCenterTaskCmd popSendCmd() {
		synchronized (sendLocked) {
			if (!sendCmds_response.isEmpty()) {
				return sendCmds_response.remove(0);
			}
			if (!sendCmds_retry.isEmpty()) {
				return sendCmds_retry.remove(0);
			}
			if (!sendCmds_1.isEmpty()) {
				return sendCmds_1.remove(0);
			}
			if (!sendCmds_2.isEmpty()) {
				return sendCmds_2.remove(0);
			}
			if (!sendCmds_3.isEmpty()) {
				return sendCmds_3.remove(0);
			}
			if (!sendCmds_4.isEmpty()) {
				return sendCmds_4.remove(0);
			}
			if (!sendCmds_5.isEmpty()) {
				return sendCmds_5.remove(0);
			}
			return null;
		}
	}

	public void addSendCmd(DataCenterTaskCmd cmd) {
		synchronized (sendLocked) {
			if (cmd.getSeq() == null) {
				cmd.setSeq(UUID.randomUUID().toString());
			}
			switch (cmd.getLevel()) {
				case 1:
					sendCmds_1.add(cmd);
					break;
				case 2:
					sendCmds_2.add(cmd);
					break;
				case 3:
					sendCmds_3.add(cmd);
					break;
				case 4:
					sendCmds_4.add(cmd);
					break;
				default:
					sendCmds_5.add(cmd);
					break;
			}
			sendLocked.notifyAll();
		}
	}

	public void addSendRetryCmd(DataCenterTaskCmd cmd) {
		synchronized (sendLocked) {
			sendCmds_retry.add(cmd);
			sendLocked.notifyAll();
		}
	}

	public void addSendResponseCmd(DataCenterTaskCmd cmd) {
		synchronized (sendLocked) {
			sendCmds_response.add(cmd);
			sendLocked.notifyAll();
		}
	}

	public void waitSend(long minwait) {
		synchronized (sendLocked) {
			if (sizeOfSendCmd() < 1) {
				try {
					sendLocked.wait(minwait);
				} catch (InterruptedException e) {
				}
			}
			sendLocked.notifyAll();
		}
	}

	public void notifySelfAll() {
		synchronized (sendLocked) {
			sendLocked.notifyAll();
		}
		synchronized (receiveLocked) {
			receiveLocked.notifyAll();
		}
		synchronized (responseLocked) {
			responseLocked.notifyAll();
		}
		synchronized (currTimestampLocked) {
			currTimestampLocked.notifyAll();
		}
		synchronized (this) {
			this.notifyAll();
		}
	}

}
