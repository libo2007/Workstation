package android.softfan.util;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

public class wf_Log {

	private static final wf_Log			myLogger	= new wf_Log(System.out);
	private java.text.SimpleDateFormat	myformat	= new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	private Writer						log;

	private boolean						outInfo;
	private boolean						outInterface;
	private boolean						outDatabase;
	private boolean						outError;

	private PrintWriter					logStream;

	public static final void sys_log(String s) {
		myLogger.log(s);
	}

	public static final void sys_log(Object o) {
		myLogger.log(o);
	}

	public static final void setLogOut(Writer out) {
		myLogger.setLog(out);
	}

	public wf_Log(OutputStream logStream) {
		this.setLogStream(logStream);
	}

	public PrintWriter getLogStream() {
		return logStream;
	}

	public synchronized void setLogStream(OutputStream out) {
		if (out != null)
			logStream = new PrintWriter(out);
		else
			logStream = null;
	}

	protected void log(String s) {
		if (log != null) {
			try {
				log.write(s);
				log.write('\n');
			} catch (IOException e) {
			}
			return;
		}
		//if (logStream != null) {
			String date;
			synchronized (myformat) {
				date = myformat.format(new Date());
			}
			log(date, s);
		//}
	}

	protected synchronized void log(String date, String s) {
		Log.e("["+date+"]",s);
	}

	protected void log(Object o) {
		log(o.toString());
	}

	public boolean isOutDatabase() {
		return outDatabase;
	}

	public void setOutDatabase(boolean outDatabase) {
		this.outDatabase = outDatabase;
	}

	public boolean isOutError() {
		return outError;
	}

	public void setOutError(boolean outError) {
		this.outError = outError;
	}

	public boolean isOutInfo() {
		return outInfo;
	}

	public void setOutInfo(boolean outInfo) {
		this.outInfo = outInfo;
	}

	public boolean isOutInterface() {
		return outInterface;
	}

	public void setOutInterface(boolean outInterface) {
		this.outInterface = outInterface;
	}

	public static wf_Log getMyLogger() {
		return myLogger;
	}

	public Writer getLog() {
		return log;
	}

	public void setLog(Writer log) {
		this.log = log;
	}
}