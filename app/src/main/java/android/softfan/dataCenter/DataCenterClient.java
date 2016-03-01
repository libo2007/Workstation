
package android.softfan.dataCenter;

import android.softfan.dataCenter.config.DataCenterClientConfig;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DataCenterClient {

    private static int Socket_Buf_Size = 5 * 1024;
    private static int Connect_TimeOut = 60000;
    private static int Connect_SoLinger = 0;

    private DataCenterClientConfig config;

    private IDataCenterPushThread dataCenterPushThread;
    private IDataCenterProcess dataCenterProcess;

    private ApDataCenter apDataCenter;

    private String host;
    private int port;

    private Socket client;

    private DataCenterRun dataCenter;

    public DataCenterClient(DataCenterClientConfig config, IDataCenterPushThread dataCenterPushThread, IDataCenterProcess dataCenterProcess, ApDataCenter apDataCenter, String host, int port) {
        this.config = config;
        this.dataCenterPushThread = dataCenterPushThread;
        this.dataCenterProcess = dataCenterProcess;
        this.apDataCenter = apDataCenter;
        this.setHost(host);
        this.setPort(port);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void run() throws DataCenterException {
        try {
            InetAddress address = InetAddress.getByName(getHost());
            Socket s;
            while (true) {
                try {
                    s = new Socket();
                    s.setReuseAddress(true);
                    s.setKeepAlive(true);
                    s.setTcpNoDelay(true);
                    s.setSoTimeout(Connect_TimeOut);
                    s.setSoLinger(true, Connect_SoLinger);
                    s.setReceiveBufferSize(Socket_Buf_Size + 1024);
                    s.setSendBufferSize(Socket_Buf_Size + 1024);
                    s.setPerformancePreferences(0, 1, 2);
                    s.connect(new InetSocketAddress(address, port));
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                s = null;
                wf_Log.sys_log("连接到数据中心(" + getHost() + ":" + port + ")失败!");
                return;
            }
            client = s;
            try {
                DataInputStream in = new DataInputStream(client.getInputStream());
                try {
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                    try {
                        {
                            //发送登陆信息
                            out.writeInt(0xFAFA);
                            out.flush();
                            if (this.apDataCenter.isCppMode()) {
                                out.writeInt(0x1);
                            } else {
                                out.writeInt(0x0);
                            }
                            out.flush();
                            if (this.apDataCenter.isXmlMsg()) {
                                out.writeInt(0x1);
                            } else {
                                out.writeInt(0x0);
                            }
                            out.flush();
                            writeString(out, this.config.getAp());
                            writeString(out, this.config.getPassword());
                            writeString(out, this.config.getOrg());

                            //读登陆信息信息头
                            int flag = in.readInt();
                            if (flag != 0xCDAB) {
                                throw new DataCenterException("数据中心登录失败！");
                            }
                            String res = readString(in);
                            if (!"ok".equals(res)) {
                                throw new DataCenterException("数据中心登录失败！");
                            }
                        }

                        dataCenter = new DataCenterRun(this.dataCenterProcess, this.apDataCenter, getHost(), in, out, false);
                        apDataCenter.setOnLine(true);
                        try {
                            apDataCenter.setHost(getHost(), getPort());
                            if (dataCenterPushThread != null) {
                                dataCenterPushThread.startup();
                            }
                            try {
                                dataCenterProcess.start();
                                try {
                                    dataCenter.run();
                                } finally {
                                    dataCenterProcess.stop();
                                }
                            } finally {
                                if (dataCenterPushThread != null) {
                                    dataCenterPushThread.shutdown();
                                }
                            }
                        } finally {
                            apDataCenter.setOnLine(false);
                            dataCenter = null;
                        }
                    } finally {
                        out.close();
                    }
                } finally {
                    in.close();
                }
            } finally {
                try {
                    if (client != null) {
                        Socket _client = client;
                        client = null;
                        _client.close();
                    }
                } catch (Exception e) {
                } catch (Throwable e) {
                }
            }
        } catch (DataCenterException e) {
            throw e;
        } catch (Exception e) {
            throw new DataCenterException("数据中心连接器错误:" + systemUtil.getErrorMessage(e));
        } catch (Throwable e) {
            throw new DataCenterException("数据中心连接器错误:" + systemUtil.getErrorMessage(e));
        }
    }

    public void doDone() {
        try {
            if (client != null) {
                Socket _client = client;
                client = null;
                _client.close();
            }
        } catch (Exception e) {
        } catch (Throwable e) {
        }
    }

    protected void writeString(DataOutputStream out, String msg) throws UnsupportedEncodingException, IOException {
        byte[] msgdata = msg.getBytes("GBK");
        int length = msgdata.length;
        out.writeInt(length);
        out.flush();
        int pos = 0;
        int count;
        while (pos < length) {
            count = length - pos;
            if (count > Socket_Buf_Size) {
                count = Socket_Buf_Size;
            }
            out.write(msgdata, pos, count);
            out.flush();
            pos += count;
        }
    }

    protected String readString(DataInputStream in) throws IOException {
        String textData;
        int length = in.readInt();
        if ((length < 0) || (length > 4096)) {
            wf_Log.sys_log("包错误：" + length);
            throw new IOException("包错误：" + length);
        }
        byte[] mydata = new byte[length];
        int pos = 0;
        int count;
        while (pos < length) {
            count = length - pos;
            if (count > Socket_Buf_Size) {
                count = Socket_Buf_Size;
            }
            count = in.read(mydata, pos, count);
            pos += count;
        }
        textData = new String(mydata, "GB2312");
        return textData;
    }

}