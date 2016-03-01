package android.softfan.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class ClientCmdDo {

	private static String	SESSIONSERVERADDR		= "127.0.0.1";
	private static int		SESSIONSERVERPORT		= 10011;
	private static String	SESSIONSERVERUSER		= "admin";
	private static String	SESSIONSERVERPASSWORD	= "admin";

	public static int		Socket_Buf_Size			= 5 * 1024;
	public static int		Connect_TimeOut			= 5 * 60000;
	public static int		Connect_SoLinger		= 2;

	public static boolean	closeRetry				= true;

	public static String DoExec(ClientCmd cmd_operator) throws ClientException {
		ClientCmdDo wf_Cmd_Do = new ClientCmdDo(SESSIONSERVERADDR, SESSIONSERVERPORT);
		return wf_Cmd_Do.Do(cmd_operator);
	}

	public static String DoExec(String command) throws ClientException {
		ClientCmdDo wf_Cmd_Do = new ClientCmdDo(SESSIONSERVERADDR, SESSIONSERVERPORT);
		return wf_Cmd_Do.Do(command);
	}

	public ClientCmdDo(String host, int port) {
		this.setHost(host);
		this.setPort(port);
	}

	public String Do(ClientCmd cmd_operator) throws ClientException {
		try {
			String command = cmd_operator.getCommand();
			byte[] cmdtxt = command.getBytes("GB2312");
			int length = cmdtxt.length;
			InetAddress address = InetAddress.getByName(getHost());
			Socket s;
			while (true) {
				try {
					s = new Socket();
					s.setReuseAddress(true);
					s.setKeepAlive(true);
					s.setTcpNoDelay(true);
					s.setSoTimeout(ClientCmdDo.Connect_TimeOut);
					//s.setSoLinger(true, ClientCmdDo.Connect_TimeOut);
					s.setReceiveBufferSize(ClientCmdDo.Socket_Buf_Size + 1024);
					s.setSendBufferSize(ClientCmdDo.Socket_Buf_Size + 1024);
					s.setPerformancePreferences(0, 1, 2);
					s.connect(new InetSocketAddress(address, port),10000);
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
				s = null;
				if (closeRetry) {
					throw new ClientException("连接到 " + getHost() + ":" + port + " 失败!重试已关闭!");
				}
				synchronized (this) {
					try {
						this.wait(200);
					} catch (InterruptedException e) {
					}
				}
			}
			try {
				DataInputStream in = new DataInputStream(s.getInputStream());
				try {
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					try {
						out.writeInt(0xF0F0);
						out.flush();
						out.writeInt(length);
						out.flush();
						int pos = 0;
						int count;
						while (pos < length) {
							count = length - pos;
							if (count > ClientCmdDo.Socket_Buf_Size) {
								count = ClientCmdDo.Socket_Buf_Size;
							}
							out.write(cmdtxt, pos, count);
							out.flush();
							pos += count;
						}
						int res_code = in.readInt();
						length = in.readInt();
						byte[] res = null;
						int end_code = 0xFFAA;
						if (length > 0) {
							res = new byte[length];
							pos = 0;
							while (pos < length) {
								count = length - pos;
								if (count > ClientCmdDo.Socket_Buf_Size) {
									count = ClientCmdDo.Socket_Buf_Size;
								}
								count = in.read(res, pos, count);
								pos += count;
							}
						}
						out.writeInt(end_code);
						out.flush();
						if (res_code != 0) {
							if (res == null)
								throw new ClientException("系统错误");
							throw new ClientException(new String(res, "GB2312"));
						}
						if (res == null)
							return new String("");
						return new String(res, "GB2312");
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}
			} finally {
				s.close();
			}
		} catch (IOException e) {
			throw new ClientException(e.getMessage());
		}
	}

	public String Do(String command) throws ClientException {
		try {
			InetAddress address = InetAddress.getByName(getHost());
			Socket s;
			while (true) {
				try {
					s = new Socket();
					s.setReuseAddress(true);
					s.setKeepAlive(true);
					s.setTcpNoDelay(true);
					s.setSoTimeout(60000);
					s.setSoLinger(true, ClientCmdDo.Connect_SoLinger);
					s.setReceiveBufferSize(ClientCmdDo.Socket_Buf_Size + 1024);
					s.setSendBufferSize(ClientCmdDo.Socket_Buf_Size + 1024);
					s.setPerformancePreferences(0, 1, 2);
					s.connect(new InetSocketAddress(address, port));
					break;
				} catch (IOException e) {
				}
				s = null;
				if (closeRetry) {
					throw new ClientException("连接到 " + getHost() + ":" + port + " 失败!重试已关闭!");
				}
				synchronized (this) {
					try {
						this.wait(200);
					} catch (InterruptedException e) {
					}
				}
			}
			try {
				DataInputStream in = new DataInputStream(s.getInputStream());
				try {
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					try {
						byte[] cmdtxt = command.getBytes("GB2312");
						int length = cmdtxt.length;
						out.writeInt(length);
						out.flush();
						int pos = 0;
						int count;
						while (pos < length) {
							count = length - pos;
							if (count > ClientCmdDo.Socket_Buf_Size) {
								count = ClientCmdDo.Socket_Buf_Size;
							}
							out.write(cmdtxt, pos, count);
							out.flush();
							pos += count;
						}
						out.write(cmdtxt);
						int res_code = in.readInt();
						length = in.readInt();
						byte[] res = null;
						int end_code = 0xFFAA;
						if (length > 0) {
							res = new byte[length];
							pos = 0;
							while (pos < length) {
								count = length - pos;
								if (count > ClientCmdDo.Socket_Buf_Size) {
									count = ClientCmdDo.Socket_Buf_Size;
								}
								count = in.read(res, pos, count);
								pos += count;
							}
						}
						out.writeInt(end_code);
						out.flush();
						if (res_code != 0) {
							if (res == null)
								throw new ClientException("系统错误");
							throw new ClientException(new String(res, "GB2312"));
						}
						if (res == null)
							return new String("");
						return new String(res, "GB2312");
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}
			} finally {
				s.close();
			}
		} catch (IOException e) {
			throw new ClientException(e.getMessage());
		}
	}

	public String getText(List<?> textlist) {
		String line = "";
		boolean first = true;
		for (Iterator<?> i = textlist.listIterator(); i.hasNext();) {
			String t = (String) i.next();
			if (first)
				first = false;
			else
				line += "\n";
			line += t;
		}
		return line;
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

	private String	host;
	private int		port;

	public static String getSESSIONSERVERADDR() {
		return SESSIONSERVERADDR;
	}

	public static void setSESSIONSERVERADDR(String wfenginaddr) {
		SESSIONSERVERADDR = wfenginaddr;
	}

	public static int getSESSIONSERVERPORT() {
		return SESSIONSERVERPORT;
	}

	public static void setSESSIONSERVERPORT(int wfenginport) {
		SESSIONSERVERPORT = wfenginport;
	}

	public static String getSESSIONSERVERUSER() {
		return SESSIONSERVERUSER;
	}

	public static void setSESSIONSERVERUSER(String wfenginuser) {
		SESSIONSERVERUSER = wfenginuser;
	}

	public static String getSESSIONSERVERPASSWORD() {
		return SESSIONSERVERPASSWORD;
	}

	public static void setSESSIONSERVERPASSWORD(String wfenginpassword) {
		SESSIONSERVERPASSWORD = wfenginpassword;
	}
}