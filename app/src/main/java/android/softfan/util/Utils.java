package android.softfan.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Utils {

	public static int getStringArrayIndex(String[] names, String value) {
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}

	public static String getStringArrayValue(String[] names, int value) {
		if (value >= 0 && value < names.length) {
			return names[value];
		}
		return "";
	}

	public static byte[] toFtpSendTxt(String txt) {
		if (txt == null)
			return new byte[0];
		try {
			return txt.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
		}
		return txt.getBytes();
	}

	public static String toFtpRecvTxt(byte[] txt) {
		if (txt == null)
			return new String("");
		try {
			return new String(txt, "GB2312");
		} catch (UnsupportedEncodingException e) {
		}
		return new String(txt);
	}

	public static byte[] toSendTxt(String txt) {
		if (txt == null)
			return new byte[0];
		try {
			return txt.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return txt.getBytes();
	}

	public static byte[] toUtf8(String txt) {
		if (txt == null)
			return new byte[0];
		try {
			return txt.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return txt.getBytes();
	}

	public static String toRecvTxt(byte[] txt) {
		if (txt == null)
			return new String("");
		try {
			return new String(txt, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return new String(txt);
	}

	public static String formatNumber(long num, int len, boolean leadingZeros) {
		StringBuffer ret = new StringBuffer(String.valueOf(num));
		if (leadingZeros) {
			while (ret.length() < len) {
				ret.insert(0, '0');
			}
		} else {
			while (ret.length() < len) {
				ret.append('0');
			}
		}
		return ret.toString();
	}

	public static String formatDayName(Calendar c) {
		return Integer.toString(c.get(Calendar.YEAR)) + formatNumber(c.get(Calendar.MONTH) + 1, 2, true) + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2, true)
				+ formatNumber(c.get(Calendar.HOUR_OF_DAY), 2, true);
	}

	public static String formatTimeName(Calendar c) {
		return formatNumber(c.get(Calendar.HOUR_OF_DAY), 2, true) + formatNumber(c.get(Calendar.MINUTE), 2, true) + formatNumber(c.get(Calendar.SECOND), 2, true);
	}

	public static String formatFileNameDate(Calendar c) {
		return Integer.toString(c.get(Calendar.YEAR)) + formatNumber(c.get(Calendar.MONTH) + 1, 2, true) + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2, true) + "-"
				+ formatNumber(c.get(Calendar.HOUR_OF_DAY), 2, true) + formatNumber(c.get(Calendar.MINUTE), 2, true) + formatNumber(c.get(Calendar.SECOND), 2, true);
	}

	public static String formatLongDate(Calendar c) {
		return Integer.toString(c.get(Calendar.YEAR)) + "-" + formatNumber(c.get(Calendar.MONTH) + 1, 2, true) + "-" + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2, true) + " "
				+ formatNumber(c.get(Calendar.HOUR_OF_DAY), 2, true) + ":" + formatNumber(c.get(Calendar.MINUTE), 2, true) + ":" + formatNumber(c.get(Calendar.SECOND), 2, true);
	}

	public static String formatShortDate(Calendar c) {
		return formatNumber(c.get(Calendar.HOUR_OF_DAY), 2, true) + ":" + formatNumber(c.get(Calendar.MINUTE), 2, true) + ":" + formatNumber(c.get(Calendar.SECOND), 2, true);
	}

	public static String formatShortTime(Calendar c) {
		return formatNumber(c.get(Calendar.MINUTE), 2, true) + ":" + formatNumber(c.get(Calendar.SECOND), 2, true);
	}

	public static String formatLongDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		return formatLongDate(calendar);
	}

	public static String stringReplace(String strSource, String strFrom, String strTo) {
		String strDest = "";
		int intFromLen = strFrom.length();
		int i;
		while ((i = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest + strSource.substring(0, i);
			strDest = strDest + strTo;
			strSource = strSource.substring(i + intFromLen);
		}
		strDest = strDest + strSource;
		return strDest;
	}

	public static String convertToHtml(String strSource) {
		if (strSource == null)
			return null;
		String strDest = stringReplace(strSource, "&", "&amp;");
		strDest = stringReplace(strDest, "<", "&lt;");
		strDest = stringReplace(strDest, ">", "&gt;");
		strDest = stringReplace(strDest, "'", "&apos;");
		strDest = stringReplace(strDest, "\"", "&quot;");
		strDest = stringReplace(strSource, " ", "&nbsp;");
		strDest = stringReplace(strDest, "\r", "");
		strDest = stringReplace(strDest, "\n", "<br>");
		return strDest;
	}

	public static boolean isEqualString(String s1, String s2) {
		if (s1 == null)
			return s2 == null;
		if (s2 == null)
			return false;
		return s1.equals(s2);
	}

	public static boolean isEmptyString(String s) {
		if (s == null)
			return true;
		if (s.length() < 1)
			return true;
		return false;
	}

	public static void logError(String msg) {
		try {
			// if (Config.getConfig().getIpt() != null)
			// Config.getConfig().getIpt().showAlert("ϵͳ����", msg,
			// AlertType.ERROR, Alert.FOREVER);
		} catch (Exception e) {
		} catch (Throwable e) {
		}
	}

	public static void logError(Throwable ex) {
		/*
		 * try { if (Config.getConfig().getIpt() != null)
		 * Config.getConfig().getIpt().showAlert("ϵͳ����", "������Ϣ: " +
		 * ex.toString(), AlertType.ERROR, Alert.FOREVER); } catch
		 * (java.lang.Exception e) { } catch (java.lang.Throwable e) { }
		 */
		// ex.printStackTrace();
	}

	public static void logError(String title, Throwable ex) {
		/*
		 * try { if (Config.getConfig().getIpt() != null)
		 * Config.getConfig().getIpt().showAlert(title, "������Ϣ: " +
		 * ex.toString(), AlertType.ERROR, Alert.FOREVER); } catch
		 * (java.lang.Exception e) { } catch (java.lang.Throwable e) { }
		 */
		// ex.printStackTrace();
	}

	public static void logDebugError(Throwable ex) {
		// ex.printStackTrace();
	}

	public static String[] splitURL(String url) throws Exception {
		StringBuffer u = new StringBuffer(url);
		String[] result = new String[6];
		for (int i = 0; i <= 5; i++) {
			result[i] = "";
		}
		// get protocol
		// boolean protFound = false;
		int index = url.indexOf(":");
		if (index > 0) {
			result[0] = url.substring(0, index);
			u.delete(0, index + 1);
			// protFound = true;
		} else if (index == 0) {
			throw new Exception("url format error - protocol");
		}
		// check for host/port
		if (u.length() > 2 && u.charAt(0) == '/' && u.charAt(1) == '/') {
			// found domain part
			u.delete(0, 2);
			int slash = u.toString().indexOf('/');
			if (slash < 0) {
				slash = u.length();
			}
			int colon = u.toString().indexOf(':');
			int endIndex = slash;
			if (colon >= 0) {
				if (colon > slash) {
					throw new Exception("url format error - port");
				}
				endIndex = colon;
				result[2] = u.toString().substring(colon + 1, slash);
			}
			result[1] = u.toString().substring(0, endIndex);
			u.delete(0, slash);
		}
		// get filename
		if (u.length() > 0) {
			url = u.toString();
			int slash = url.lastIndexOf('/');
			if (slash > 0) {
				result[3] = url.substring(0, slash);
			}
			if (slash < url.length() - 1) {
				String fn = url.substring(slash + 1, url.length());
				int anchorIndex = fn.indexOf("#");
				if (anchorIndex >= 0) {
					result[4] = fn.substring(0, anchorIndex);
					result[5] = fn.substring(anchorIndex + 1);
				} else {
					result[4] = fn;
				}
			}
		}
		return result;
	}

	public static String mergeURL(String[] url) {
		return ((url[0] == "") ? "" : url[0] + ":/") + ((url[1] == "") ? "" : "/" + url[1]) + ((url[2] == "") ? "" : ":" + url[2]) + url[3] + "/" + url[4] + ((url[5] == "") ? "" : "#" + url[5]);
	}

	public static String guessContentType(String url) throws Exception {
		// guess content type
		String[] sURL = splitURL(url);
		String ext = "";
		String ct = "";
		int lastDot = sURL[4].lastIndexOf('.');
		if (lastDot >= 0) {
			ext = sURL[4].substring(lastDot + 1).toLowerCase();
		}
		if (ext.equals("mpg") || url.equals("avi")) {
			ct = "video/mpeg";
		} else if (ext.equals("mid") || ext.equals("kar")) {
			ct = "audio/midi";
		} else if (ext.equals("wav")) {
			ct = "audio/x-wav";
		} else if (ext.equals("jts")) {
			ct = "audio/x-tone-seq";
		} else if (ext.equals("txt")) {
			ct = "audio/x-txt";
		} else if (ext.equals("amr")) {
			ct = "audio/amr";
		} else if (ext.equals("awb")) {
			ct = "audio/amr-wb";
		} else if (ext.equals("gif")) {
			ct = "image/gif";
		}
		return ct;
	}

	public static String ExtractParamProperty(String str, String propertyName) {
		if (Utils.isEmptyString(str))
			return null;
		int pos = str.indexOf(propertyName);
		if (pos < 0)
			return null;
		pos += propertyName.length();
		if (pos >= str.length())
			return null;
		pos = str.indexOf("=", pos);
		if (pos < 0)
			return null;
		pos++;
		pos = SeekSpace(pos, str);
		int pos1 = str.indexOf("&", pos);
		if (pos1 < 0) {
			return str.substring(pos);
		}
		return str.substring(pos, pos1);
	}

	public static Vector<String> stringSplit(String source, String symbol) {
		Vector<String> tmp = new Vector<String>();
		int p1 = 0;
		int p2 = 0;
		int len = source.length();
		while (true) {
			p2 = source.indexOf(symbol, p1);
			if (p2 < p1) {
				break;
			}
			if ((p2 - p1) > 0) {
				tmp.addElement(source.substring(p1, p2));
			}
			p1 = p2 + 1;
			if (p1 >= len) {
				break;
			}
		}
		if (p1 < source.length()) {
			tmp.addElement(source.substring(p1));
		}
		return tmp;
	}

	public static Vector<String> SplitStringBySpace(String str) {
		if (Utils.isEmptyString(str))
			return null;
		Vector<String> s = new Vector<String>();
		int idx1 = SeekSpace(0, str);
		int idx2 = idx1;
		while (idx1 < str.length()) {
			idx2 = ToSpace(idx1, str);
			if (idx2 <= idx1) {
				break;
			}
			s.addElement(str.substring(idx1, idx2));
			idx1 = SeekSpace(idx2, str);
		}
		return s;
	}

	public static boolean IsSpaceChar(char c) {
		if (c == ' ')
			return true;
		if (c == '\r')
			return true;
		if (c == '\n')
			return true;
		if (c == '\t')
			return true;
		if (c == ',')
			return true;
		if (c == ';')
			return true;
		return false;
	}

	public static int SeekSpace(int pos, String str) {
		while (pos < str.length()) {
			if (!IsSpaceChar(str.charAt(pos))) {
				return pos;
			}
			pos++;
		}
		return pos;
	}

	public static int ToSpace(int pos, String str) {
		while (pos < str.length()) {
			if (IsSpaceChar(str.charAt(pos))) {
				return pos;
			}
			pos++;
		}
		return pos;
	}

	public String getHashUrl(Calendar calendar) {
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(calendar.get(Calendar.DATE));
		return year + "/" + month + "/" + day;
	}

	static public String SubString(String s, int rellen) {
		if (s == null)
			return s;
		int idx = 0;
		while (idx < s.length()) {
			char c = s.charAt(idx);
			if ((c >= '\u4E00') && (c <= '\u9FA0')) {
				rellen -= 2;
			} else {
				rellen--;
			}
			idx++;
			if (rellen <= 0) {
				break;
			}
		}
		return s.substring(0, idx);
	}
}
