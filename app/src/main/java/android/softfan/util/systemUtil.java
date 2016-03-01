package android.softfan.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class systemUtil {

	public static final String	nl	= System.getProperty("line.separator");

	public static String extratFileName(String fileName) {
		String fname = fileName;
		while (fname != null) {
			int pos = fname.lastIndexOf("\\");
			if (pos > 0) {
				fname = fname.substring(pos + 1);
				break;
			}
			pos = fname.lastIndexOf("/");
			if (pos > 0) {
				fname = fname.substring(pos + 1);
				break;
			}
			break;
		}
		return fname;
	}

	public static String replace(String s, String find, String replace) {
		// let's be optimistic
		if (s == null)
			return null;
		int found = s.indexOf(find);
		if (found == -1) {
			return s;
		}
		StringBuffer sb = new StringBuffer(s.length() + 20);
		int start = 0;
		char[] chars = s.toCharArray();
		final int step = find.length();
		if (step == 0) {
			// Special case where find is "".
			sb.append(s);
			replace(sb, 0, find, replace);
		} else {
			for (;;) {
				sb.append(chars, start, found - start);
				if (found == s.length()) {
					break;
				}
				sb.append(replace);
				start = found + step;
				found = s.indexOf(find, start);
				if (found == -1) {
					found = s.length();
				}
			}
		}
		return sb.toString();
	}

	public static StringBuffer replace(StringBuffer buf, int start, String find, String replace) {

		// Search and replace from the end towards the start, to avoid O(n ^ 2)
		// copying if the string occurs very commonly.
		int findLength = find.length();
		if (findLength == 0) {
			// Special case where the seek string is empty.
			for (int j = buf.length(); j >= 0; --j) {
				buf.insert(j, replace);
			}
			return buf;
		}
		int k = buf.length();
		while (k > 0) {
			int i = buf.lastIndexOf(find, k);
			if (i < start) {
				break;
			}
			buf.replace(i, i + find.length(), replace);
			// Step back far enough to ensure that the beginning of the section
			// we just replaced does not cause a match.
			k = i - findLength;
		}
		return buf;
	}

	public static boolean isEmpty(String s) {
		return (s == null) || (s.length() == 0);
	}

	public static boolean isEmptyValue(Object v) {
		if (v == null)
			return true;
		if (v instanceof String)
			return isEmpty((String) v);
		return false;
	}

	public static String singleQuoteString(String val) {
		StringBuffer buf = new StringBuffer(64);
		singleQuoteString(val, buf);
		return buf.toString();
	}

	public static void singleQuoteString(String val, StringBuffer buf) {
		buf.append('\'');

		String s0 = replace(val, "'", "''");
		buf.append(s0);

		buf.append('\'');
	}

	public static String[] convertStackToString(Throwable e) {
		List<String> list = new ArrayList<String>();
		while (e != null) {
			String sMsg = getErrorMessage(e);
			list.add(sMsg);
			e = e.getCause();
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String getStackTraceMessage(int count, int skip) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		int ix = skip + 1;
		int c = count;
		while (ix < stack.length) {
			StackTraceElement frame = stack[ix];
			String cname = frame.getClassName();
			String mname = frame.getMethodName();
			if (cname.equals("java.util.logging.Logger")) {
				break;
			}
			int linenumber = frame.getLineNumber();
			pw.println("");
			pw.print(cname + ":" + mname + ":" + linenumber);
			c--;
			if (c < 1)
				break;
			ix++;
		}
		return sw.toString();
	}

	public static String getStackTraceErrorMessage(Throwable err) {
		boolean prependClassName = !(err instanceof java.sql.SQLException || err.getClass() == Exception.class);
		return getStackTraceErrorMessage(err, prependClassName);
	}

	public static String getStackTraceErrorMessage(Throwable err, boolean prependClassName) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		err.printStackTrace(pw);
		return sw.toString();
	}

	public static String getErrorMessage(Throwable err) {
		if (err instanceof java.sql.SQLException) {
			return getStackTraceErrorMessage(err);
		}
		if (err instanceof NullPointerException) {
			return getStackTraceErrorMessage(err);
		}
		if (err instanceof java.io.EOFException) {
			return getStackTraceErrorMessage(err);
		}
		boolean prependClassName = !(err instanceof java.sql.SQLException || err.getClass() == Exception.class);
		return getErrorMessage(err, prependClassName);
	}

	public static String getSingleErrorMessage(Throwable err) {
		boolean prependClassName = !(err instanceof java.sql.SQLException || err.getClass() == Exception.class);
		return getErrorMessage(err, prependClassName);
	}

	public static String getErrorMessage(Throwable err, boolean prependClassName) {
		String errMsg = err.getMessage();
		if (errMsg != null) {
			return errMsg;
		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			if (err instanceof NullPointerException) {
				getErrorMessageByStackTrace(pw, err);
			} else {
				getSignErrorMessageByStackTrace(pw, err);
			}
			return sw.toString();
		}
	}

	public static void getSignErrorMessageByStackTrace(PrintWriter pw, Throwable err) {
		Throwable ourCause = err.getCause();
		if (ourCause != null) {
			String errMsg = ourCause.getMessage();
			if (!textUnit.StringIsEmpty(errMsg)) {
				pw.print(errMsg);
				pw.print("\n");
				return;
			}
			getErrorMessageByStackTrace(pw, ourCause);
		} /*
		 * else { StackTraceElement[] stackTrace = err.getStackTrace(); if
		 * (stackTrace != null) getErrorStackTraceMessage(stackTrace,pw,12); }
		 */
	}

	public static void getErrorMessageByStackTrace(PrintWriter pw, Throwable err) {
		err.printStackTrace(pw);
	}

	public static void getErrorStackTraceMessage(StackTraceElement stack[], PrintWriter pw, int count) {
		int ix = stack.length - 1;
		int c = count;
		while (ix > 0) {
			StackTraceElement frame = stack[ix];
			String cname = frame.getClassName();
			if (cname.equals("java.util.logging.Logger")) {
				break;
			}
			String mname = frame.getMethodName();
			int linenumber = frame.getLineNumber();
			pw.print(cname + ":" + mname + ":" + linenumber);
			pw.print("\n");
			c--;
			if (c < 1)
				break;
			ix--;
		}
	}

	public static URL toURL(File file) throws MalformedURLException {
		String path = file.getAbsolutePath();
		// This is a bunch of weird code that is required to
		// make a valid URL on the Windows platform, due
		// to inconsistencies in what getAbsolutePath returns.
		String fs = System.getProperty("file.separator");
		if (fs.length() == 1) {
			char sep = fs.charAt(0);
			if (sep != '/') {
				path = path.replace(sep, '/');
			}
			if (path.charAt(0) != '/') {
				path = '/' + path;
			}
		}
		path = "file://" + path;
		return new URL(path);
	}

	public static Object convertTo(Class<?> typeClass, Object value) throws SoftFanUtilException {
		if (value == null)
			return null;
		if (typeClass == value.getClass()) {
			return value;
		}
		if (typeClass == String.class) {
			if (value instanceof Date) {
				return DateUnit.toDateText((Date) value);
			}
			return value.toString();
		}
		if (typeClass == Boolean.class) {
			if (value instanceof Boolean)
				return value;
			return Boolean.parseBoolean(value.toString());
		}
		if (typeClass == Byte.class) {
			if (value instanceof Byte)
				return value;
			if (value instanceof Short)
				return new Byte(((Short) value).byteValue());
			if (value instanceof Integer)
				return new Byte(((Integer) value).byteValue());
			if (value instanceof Long)
				return new Byte(((Long) value).byteValue());
			if (value instanceof Float)
				return new Byte(((Float) value).byteValue());
			if (value instanceof Double)
				return new Byte(((Double) value).byteValue());
			if (value instanceof BigDecimal)
				return new Byte(((BigDecimal) value).byteValue());
			if (value instanceof BigInteger)
				return new Byte(((BigInteger) value).byteValue());
			if (value instanceof String) {
				try {
					return new Byte(Byte.parseByte((String) value));
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		if (typeClass == Short.class) {
			if (value instanceof Short)
				return value;
			if (value instanceof Byte)
				return new Short(((Byte) value).shortValue());
			if (value instanceof Integer)
				return new Short(((Integer) value).shortValue());
			if (value instanceof Long)
				return new Short(((Long) value).shortValue());
			if (value instanceof Float)
				return new Short(((Float) value).shortValue());
			if (value instanceof Double)
				return new Short(((Double) value).shortValue());
			if (value instanceof BigDecimal)
				return new Short(((BigDecimal) value).shortValue());
			if (value instanceof BigInteger)
				return new Short(((BigInteger) value).shortValue());
			if (value instanceof String) {
				try {
					return new Short(Short.parseShort((String) value));
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		if (typeClass == Integer.class) {
			if (value instanceof Integer)
				return value;
			if (value instanceof Byte)
				return new Integer(((Byte) value).intValue());
			if (value instanceof Short)
				return new Integer(((Short) value).intValue());
			if (value instanceof Long)
				return new Integer(((Long) value).intValue());
			if (value instanceof Float)
				return new Integer(((Float) value).intValue());
			if (value instanceof Double)
				return new Integer(((Double) value).intValue());
			if (value instanceof BigDecimal)
				return new Integer(((BigDecimal) value).intValue());
			if (value instanceof BigInteger)
				return new Integer(((BigInteger) value).intValue());
			if (value instanceof String) {
				try {
					return new Integer(Integer.parseInt((String) value));
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		if (typeClass == Long.class) {
			if (value instanceof Long)
				return value;
			if (value instanceof Byte)
				return new Long(((Byte) value).longValue());
			if (value instanceof Integer)
				return new Long(((Integer) value).longValue());
			if (value instanceof Float)
				return new Long(((Float) value).longValue());
			if (value instanceof Double)
				return new Long(((Double) value).longValue());
			if (value instanceof BigDecimal)
				return new Long(((BigDecimal) value).longValue());
			if (value instanceof BigInteger)
				return new Long(((BigInteger) value).longValue());
			if (value instanceof String) {
				try {
					return new Long(Long.parseLong((String) value));
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		if (typeClass == Float.class) {
			if (value instanceof Float)
				return value;
			if (value instanceof Byte)
				return new Float(((Byte) value).floatValue());
			if (value instanceof Short)
				return new Float(((Short) value).floatValue());
			if (value instanceof Long)
				return new Float(((Long) value).floatValue());
			if (value instanceof Double)
				return new Float(((Double) value).floatValue());
			if (value instanceof BigDecimal)
				return new Float(((BigDecimal) value).floatValue());
			if (value instanceof BigInteger)
				return new Float(((BigInteger) value).floatValue());
			if (value instanceof String) {
				try {
					return new Float(Float.parseFloat((String) value));
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		if (typeClass == Double.class) {
			if (value instanceof Double)
				return value;
			if (value instanceof Byte)
				return new Double(((Byte) value).doubleValue());
			if (value instanceof Short)
				return new Double(((Short) value).doubleValue());
			if (value instanceof Long)
				return new Double(((Long) value).doubleValue());
			if (value instanceof Float)
				return new Double(((Float) value).doubleValue());
			if (value instanceof BigDecimal)
				return new Double(((BigDecimal) value).doubleValue());
			if (value instanceof BigInteger)
				return new Double(((BigInteger) value).doubleValue());
			if (value instanceof String) {
				try {
					return new Double(Double.parseDouble((String) value));
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		if (typeClass == Date.class) {
			if (value instanceof Date)
				return value;
			if (value instanceof Byte)
				return new Date(((Byte) value).longValue());
			if (value instanceof Short)
				return new Date(((Short) value).longValue());
			if (value instanceof Integer)
				return new Date(((Integer) value).longValue());
			if (value instanceof Long)
				return new Date(((Long) value).longValue());
			if (value instanceof Float)
				return new Date(((Float) value).longValue());
			if (value instanceof Double)
				return new Date(((Double) value).longValue());
			if (value instanceof BigDecimal)
				return new Date(((BigDecimal) value).longValue());
			if (value instanceof BigInteger)
				return new Date(((BigDecimal) value).longValue());
			if (value instanceof String) {
				try {
					return DateUnit.toDate((String) value);
				} catch (ParseException e) {
				}
				try {
					double v = Double.parseDouble((String) value);
					return new Double(v);
				} catch (NumberFormatException e) {
					throw new SoftFanUtilException("参数类型不能转换:" + e.getMessage());
				}
			}
			throw new SoftFanUtilException("参数类型不能转换");
		}
		return value;
	}

	public static Object createNewValue(Class<?> typeClass) {
		if (typeClass == Boolean.class) {
			return new Boolean(false);
		}
		if (typeClass == Byte.class) {
			return new Byte((byte) 0);
		}
		if (typeClass == Short.class) {
			return new Short((short) 0);
		}
		if (typeClass == Integer.class) {
			return new Integer(0);
		}
		if (typeClass == Long.class) {
			return new Long(0);
		}
		if (typeClass == Float.class) {
			return new Float(0);
		}
		if (typeClass == Double.class) {
			return new Double(0);
		}
		if (typeClass == Date.class) {
			return new Date(System.currentTimeMillis());
		}
		return new String("");
	}

	public static void deleteFiles(String src) {
		File srcFolder = new File(src);
		deleteFiles(srcFolder);
	}

	public static void deleteFiles(File srcFolder) {
		if (srcFolder.isDirectory()) {
			File[] files = srcFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolder(files[i]);
				}
				files[i].delete();
			}
			srcFolder.delete();
		} else if (srcFolder.isFile()) {
			srcFolder.delete();
		}
	}

	public static void deleteFolder(String src) {
		File srcFolder = new File(src);
		if (srcFolder.isDirectory()) {
			deleteFiles(srcFolder);
		}
	}

	public static void deleteFolder(File srcFolder) {
		if (srcFolder.isDirectory()) {
			deleteFiles(srcFolder);
		}
	}

	public static void copyFolder(String src, String dest) throws IOException {
		File srcFolder = new File(src);
		if (srcFolder.isDirectory()) {
			File destFolder = new File(dest);
			if (destFolder.isDirectory()) {
				File[] files = srcFolder.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						File destfolder = new File(destFolder, files[i].getName());
						destfolder.mkdir();
						copyFolder(files[i], destfolder);
					} else {
						File file = new File(destFolder, files[i].getName());
						copyFile(files[i], file);
					}
				}
			}
		}
	}

	public static void copyFolder(File srcFolder, File destFolder) throws IOException {
		if (srcFolder.isDirectory()) {
			if (destFolder.isDirectory()) {
				File[] files = srcFolder.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						File dest = new File(destFolder, files[i].getName());
						dest.mkdir();
						copyFolder(files[i], dest);
					} else {
						File dest = new File(destFolder, files[i].getName());
						copyFile(files[i], dest);
					}
				}
			}
		}
	}

	public static void copyFolder1(File srcFolder, File destFolder) throws IOException {
		if (srcFolder.isDirectory()) {
			if (destFolder.isDirectory()) {
				File[] files = srcFolder.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						File dest = new File(destFolder, files[i].getName());
						dest.mkdir();
						copyFolder(files[i], dest);
					} else {
						File dest = new File(destFolder, files[i].getName());
						copyFile1(files[i], dest);
					}
				}
			}
		}
	}

	public static boolean copyFile(String src, String dest) throws IOException {
		File srcFile = new File(src);
		if (srcFile.isFile()) {
			File destFile = new File(dest);
			copyFile(srcFile, destFile);
		}
		return false;
	}

	public static boolean copyFile(File srcFile, File destFile) throws IOException {
		if (srcFile.isFile()) {
			java.io.FileInputStream is = new java.io.FileInputStream(srcFile);
			try {
				java.io.FileOutputStream os = new java.io.FileOutputStream(destFile);
				try {
					byte[] buf = new byte[1024 * 100];
					int count;
					while (true) {
						count = is.read(buf);
						if (count < 1)
							break;
						os.write(buf, 0, count);
						if (count < buf.length)
							break;
					}
				} finally {
					os.close();
				}
				destFile.setLastModified(srcFile.lastModified());
				return true;
			} finally {
				is.close();
			}
		}
		return false;
	}

	public static boolean copyFile1(String src, String dest) throws IOException {
		File srcFile = new File(src);
		if (srcFile.isFile()) {
			File destFile = new File(dest);
			copyFile1(srcFile, destFile);
		}
		return false;
	}

	public static boolean copyFile1(File srcFile, File destFile) throws IOException {
		if (srcFile.isFile()) {
			if (destFile.exists()) {
				if (destFile.lastModified() >= srcFile.lastModified()) {
					return true;
				}
			}
			java.io.FileInputStream is = new java.io.FileInputStream(srcFile);
			try {
				java.io.FileOutputStream os = new java.io.FileOutputStream(destFile);
				try {
					byte[] buf = new byte[1024 * 100];
					int count;
					while (true) {
						count = is.read(buf);
						if (count < 1)
							break;
						os.write(buf, 0, count);
						if (count < buf.length)
							break;
					}
				} finally {
					os.close();
				}
				destFile.setLastModified(srcFile.lastModified());
			} finally {
				is.close();
			}
			return true;
		}
		return false;
	}

	public static void makeFolder(String root, String resource) {
		File file = new File(root);
		StringTokenizer st = new StringTokenizer(resource, "/");
		while (st.hasMoreElements()) {
			String tempstr = (String) st.nextElement();
			file = new File(file, tempstr);
			file.mkdir();
		}
	}

	public static File makeFolder(File root, String filename) throws IOException {
		if (!root.isDirectory()) {
			throw new IOException("输出文件路径错误");
		}
		StringTokenizer st = new StringTokenizer(systemUtil.replace(filename, "\\", "/"), "/");
		File file = root;
		String tempstr;
		while (st.hasMoreElements()) {
			tempstr = (String) st.nextElement();
			file = new File(file, tempstr);
			if (!file.isDirectory()) {
				file.mkdir();
			}
		}
		return file;
	}

	public static File makeFile(File root, String filename) throws IOException {
		if (!root.isDirectory()) {
			throw new IOException("输出文件路径错误");
		}
		StringTokenizer st = new StringTokenizer(systemUtil.replace(filename, "\\", "/"), "/");
		File file = root;
		String tempstr;
		while (st.hasMoreElements()) {
			tempstr = (String) st.nextElement();
			if (!st.hasMoreElements()) {
				file = new File(file, tempstr);
				break;
			}
			file = new File(file, tempstr);
			if (st.hasMoreElements()) {
				if (!file.isDirectory()) {
					file.mkdir();
				}
			}
		}
		return file;
	}

	public static File makeFile(String filename) throws IOException {
		StringTokenizer st = new StringTokenizer(systemUtil.replace(filename, "\\", "/"), "/");
		if (!st.hasMoreElements()) {
			throw new IOException("输出文件路径错误");
		}
		String tempstr = (String) st.nextElement();
		File file = new File(tempstr);
		if (st.hasMoreElements()) {
			if (!file.isDirectory()) {
				file.mkdir();
			}
		}
		while (st.hasMoreElements()) {
			tempstr = (String) st.nextElement();
			file = new File(file, tempstr);
			if (!st.hasMoreElements()) {
				break;
			}
			if (st.hasMoreElements()) {
				if (!file.isDirectory()) {
					file.mkdir();
				}
			}
		}
		return file;
	}
}