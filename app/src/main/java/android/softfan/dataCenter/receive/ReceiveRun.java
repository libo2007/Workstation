
package android.softfan.dataCenter.receive;

import android.softfan.client.ClientCmdDo;
import android.softfan.dataCenter.ApDataCenter;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.io.DataInputStream;

public class ReceiveRun extends Thread {

    private static int MaxSize = 200 * 1024 * 1024;

    private DataInputStream inputStream;

    private DataCenterRun dataCenter;

    public ReceiveRun(DataCenterRun dataCenter, DataInputStream inputStream) {
        super("DataCenter-ReceiveRun");
        this.dataCenter = dataCenter;
        this.inputStream = inputStream;
    }

    public void run() {
        ApDataCenter apDataCenter = this.dataCenter.getApDataCenter();
        try {
            while (this.dataCenter.isRuning()) {
                try {
                    int res_code = inputStream.readInt();
                    if (res_code != 0xF3B6) {
                        throw new Exception("通信数据格式错误！");
                    }
                    int length = inputStream.readInt();
                    if (length < 0) {
                        throw new Exception("通信数据格式错误！");
                    }
                    if (length > MaxSize) {
                        throw new Exception("通信数据包太大（" + length + "）！");
                    }
                    byte[] data = null;
                    if (length > 0) {
                        data = new byte[length];
                        int pos = 0;
                        while (pos < length) {
                            int count = length - pos;
                            if (count > ClientCmdDo.Socket_Buf_Size) {
                                count = ClientCmdDo.Socket_Buf_Size;
                            }
                            count = inputStream.read(data, pos, count);
                            pos += count;
                        }
                        DataCenterTaskCmd cmd = new DataCenterTaskCmd();
                        if (this.dataCenter.getApDataCenter().isXmlMsg()) {
                            cmd.extractXmlCommand(data);
                        } else {
                            cmd.extractCommand(data);
                        }
                        if ("online".equals(cmd.getCmd())) {
                            //心跳
                            byte[] c = data;
                            String s = new String(c);
                            wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】：在线");
                        } else if ("response".equals(cmd.getCmd())) {
                            wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】收到应答：" + cmd.getSeq());
                            apDataCenter.addReceiveResponseCmd(cmd);
                        } else {
                            wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】收到命令：" + cmd.getCmd() + "," + cmd.getSeq());
                            apDataCenter.addReceiveCmd(cmd);
                        }
                    }
                } catch (Exception e) {
                    this.dataCenter.onError(1);
                    wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】接收器错误:" + systemUtil.getErrorMessage(e));
                } catch (Throwable e) {
                    this.dataCenter.onError(1);
                    wf_Log.sys_log("数据中心【" + this.dataCenter.getHost() + "】接收器错误:" + systemUtil.getErrorMessage(e));
                }
            }
        } finally {
            this.dataCenter.setRecverFinished(true);
            this.dataCenter = null;
            this.inputStream = null;
        }
    }

}