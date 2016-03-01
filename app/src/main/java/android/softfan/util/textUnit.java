package android.softfan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class textUnit {

	public static final String	Encoding	= "GBK";

	static public boolean isInFields(String fields, String code) {
		int ci = 0;
		while (ci < fields.length()) {
			ci = fields.indexOf(code, ci);
			if (ci < 0) {
				return false;
			}
			if (ci > 0) {
				if (fields.charAt(ci - 1) != ';') {
					ci += code.length();
					continue;
				}
			}
			ci += code.length();
			if (ci >= fields.length()) {
				return true;
			}
			if (fields.charAt(ci) == ';') {
				return true;
			}
		}
		return false;
	}

	static public String StringsToString(String[] input) {
		if (input == null)
			return "";
		if (input.length < 1)
			return "";
		StringBuilder Temp = new StringBuilder();
		if (input != null) {
			for (int i = 0; i < input.length; i++) {
				if (input[i] != null)
					Temp.append(textUnit.ConverString(input[i]));
			}
			if (Temp.length() > 0)
				return Temp.toString();
		}
		return null;
	}

	static public String StringsToString(String method, String[] input) {
		if (input == null)
			return "";
		if (input.length < 1)
			return "";
		StringBuilder Temp = new StringBuilder();
		if (input != null) {
			for (int i = 0; i < input.length; i++) {
				if (input[i] != null)
					Temp.append(textUnit.ConverString(method, input[i]));
			}
			if (Temp.length() > 0)
				return Temp.toString();
		}
		return null;
	}

	static public String ajaxEscapeJavaScript(String s) throws UnsupportedEncodingException {
		if (s == null) {
			return s;
		}
		return java.net.URLEncoder.encode(s, "UTF-8");
	}

	static public String ConverString(String i, boolean isNewSession) {
		if (isNewSession) {
			return ConverString(i);
		}
		return i;
	}

	static public String ConverString(String method, String i, boolean isNewSession) {
		if (isNewSession) {
			return ConverString(method, i);
		}
		return i;
	}

	static public String ConverString(String i) {
		if (i != null) {
			try {
				return new String(i.getBytes("ISO-8859-1"), "GBK");
			} catch (Exception ex) {
			}
		}
		return i;
	}

	static public String ConverString(String method, String i) {
		if (i != null) {
			if ("GET".equalsIgnoreCase(method)) {
				try {
					i = new String(i.getBytes("ISO-8859-1"), "GBK");
				} catch (Exception ex) {
				}
			}
		}
		return i;
	}

	static public String ConverParamter(String i) {
		if (i != null) {
			try {
				i = new String(i.getBytes("ISO-8859-1"), "GBK");
				try {
					i = URLDecoder.decode(i, "utf8");
				} catch (UnsupportedEncodingException e) {
				}
			} catch (Exception ex) {
			}
		}
		return i;
	}

	static public String ConverStringCheckEmpty(String method, String i) {
		if (i != null) {
			try {
				if (i.length() < 1)
					return null;
				if ("GET".equalsIgnoreCase(method)) {
					return new String(i.getBytes("ISO-8859-1"), "GBK");
				}
			} catch (Exception ex) {
			}
		}
		return i;
	}

	static public String ConverStringTo8859(String i) {
		if (i != null) {
			try {
				return new String(i.getBytes(), "ISO-8859-1");
			} catch (Exception ex) {
			}
		}
		return i;
	}

	static public String ConverStringToGB2312(String i) {
		if (i != null) {
			try {
				return new String(i.getBytes(), "GB2312");
			} catch (Exception ex) {
			}
		}
		return i;
	}

	static public String CheckHtmlString(String i) {
		if ((i != null) && (i.length() > 0)) {
			return i;
		}
		return "&nbsp;";
	}

	static public String makeString(String i) {
		if ((i != null) && (i.length() > 0)) {
			return i;
		}
		return new String("");
	}

	static public int indexOfString(String i, String j) {
		if (i == null) {
			return -1;
		}
		return i.indexOf(j);
	}

	static public boolean existOfString(String i, String j) {
		return findOfString(i, j) >= 0;
	}

	static public int findOfString(String i, String j) {
		if (i == null) {
			return -1;
		}
		int p = i.indexOf(j);
		if (p < 0) {
			return -1;
		}
		if (p > 0) {
			if (i.charAt(p - 1) != ';') {
				return -1;
			}
		}
		if (p < (i.length() - 1)) {
			if (i.charAt(p + j.length()) != ';') {
				return -1;
			}
		}
		return p;
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

	// &amp (&), &lt (<), &gt (>), &quot ("), and &apos (').
	public static String convertToXml(String strSource) {
		if (strSource == null)
			return null;
		String strDest = stringReplace(strSource, "&", "&amp;");
		strDest = stringReplace(strDest, "<", "&lt;");
		strDest = stringReplace(strDest, ">", "&gt;");
		strDest = stringReplace(strDest, "'", "&apos;");
		strDest = stringReplace(strDest, "\"", "&quot;");
		// strDest = stringReplace(strDest, " ", "&#32;");
		// strDest = stringReplace(strDest, "\r", "&#10;");
		// strDest = stringReplace(strDest, "\n", "&#13;");
		strDest = stringReplace(strDest, "\u000b", "");
		strDest = stringReplace(strDest, "\u0008", " ");// "&#8;");
		strDest = stringReplace(strDest, "\u0009", " ");// "&#9;");
		strDest = stringReplace(strDest, "\u001b", " ");// "&#27;");
		return strDest;
	}

	public static String convertToHtmlAttr(String strSource) {
		if (strSource == null)
			return null;
		String strDest = stringReplace(strSource, "&", "&amp;");
		strDest = stringReplace(strDest, "<", "&lt;");
		strDest = stringReplace(strDest, ">", "&gt;");
		strDest = stringReplace(strDest, "'", "&apos;");
		strDest = stringReplace(strDest, "\"", "&quot;");
		// strDest = stringReplace(strDest, " ", "&nbsp;");
		strDest = stringReplace(strDest, "\r", "");
		strDest = stringReplace(strDest, "\n", " ");// "<br>");
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
		strDest = stringReplace(strDest, " ", "&nbsp;");
		strDest = stringReplace(strDest, "\r", "");
		strDest = stringReplace(strDest, "\n", " ");// "<br>");
		return strDest;
	}

	public static String convertToHTMLStr(String strSource) {
		if ((strSource == null) || (strSource.length() < 1))
			return "&nbsp;";
		if (strSource.length() > 6) {
			if (strSource.substring(0, 5).compareToIgnoreCase("HTML:") == 0) {
				String strDest = strSource.substring(5);
				if ((strDest == null) || (strDest.length() < 1))
					return "&nbsp;";
				return strDest;
			}
		}
		String strDest = stringReplace(strSource, "&", "&amp;");
		strDest = stringReplace(strDest, "<", "&lt;");
		strDest = stringReplace(strDest, ">", "&gt;");
		strDest = stringReplace(strDest, "'", "&apos;");
		strDest = stringReplace(strDest, "\"", "&quot;");
		strDest = stringReplace(strDest, " ", "&nbsp;");
		strDest = stringReplace(strDest, "\r", "");
		strDest = stringReplace(strDest, "\n", "<br>");
		return strDest;
	}

	public static String convertToHTMLStrNoSpace(String strSource) {
		if ((strSource == null) || (strSource.length() < 1))
			return "";
		String strDest = stringReplace(strSource, "&", "&amp;");
		strDest = stringReplace(strDest, "<", "&lt;");
		strDest = stringReplace(strDest, ">", "&gt;");
		strDest = stringReplace(strDest, "'", "&apos;");
		strDest = stringReplace(strDest, "\"", "&quot;");
		strDest = stringReplace(strDest, " ", "&nbsp;");
		strDest = stringReplace(strDest, "\r", "");
		strDest = stringReplace(strDest, "\n", "<br>");
		return strDest;
	}

	public static String convertToHTMLDisplay(Object v) {
		if (v == null) {
			return "";
		}
		String strSource = v.toString();
		if ((strSource == null) || (strSource.length() < 1))
			return "";
		String strDest = stringReplace(strSource, "&", "&amp;");
		strDest = stringReplace(strDest, "<", "&lt;");
		strDest = stringReplace(strDest, ">", "&gt;");
		strDest = stringReplace(strDest, "'", "&apos;");
		strDest = stringReplace(strDest, "\"", "&quot;");
		strDest = stringReplace(strDest, " ", "&nbsp;");
		strDest = stringReplace(strDest, "\r", "");
		strDest = stringReplace(strDest, "\n", "<br>");
		return strDest;
	}

	public static String convertToTextArea(String strSource) {
		if ((strSource == null) || (strSource.length() < 1))
			return "";
		String strDest = stringReplace(strSource, "\r", "");
		strDest = stringReplace(strDest, "\n", "");
		return strDest;
	}

	public static String convertToSingleText(String strSource) {
		if ((strSource == null) || (strSource.length() < 1))
			return "";
		String strDest = stringReplace(strSource, "\r", "");
		strDest = stringReplace(strDest, "\n", "");
		return strDest;
	}

	static public boolean httpCompareString(String head, String request, int offset) {
		int len = request.length() - offset;
		for (int ri = 0; ri < head.length(); ri++) {
			if (ri >= len) {
				return false;
			}
			if (head.charAt(ri) != request.charAt(ri + offset)) {
				return false;
			}
		}
		return true;
	}

	static public boolean compareString1(String i, String j) {
		if (i == null) {
			if (j == null)
				return true;
			if (j.length() < 1)
				return true;
			return false;
		}
		if (j == null) {
			if (i.length() < 1)
				return true;
			return false;
		}
		return i.equals(j);
	}

	static public boolean compareString(String i, String j) {
		if (i == null) {
			return j == null;
		}
		if (j == null)
			return false;
		return i.equals(j);
	}

	static public boolean compareIgnoreCaseString(String i, String j) {
		if (i == null) {
			return j == null;
		}
		if (j == null)
			return false;
		return i.equalsIgnoreCase(j);
	}

	static public int sortCompareString(String i, String j) {
		if (i == null) {
			return j == null ? 0 : 1;
		}
		if (j == null)
			return -1;
		return i.compareTo(j);
	}

	static public int sortCompareDate(Date i, Date j) {
		if (i == null) {
			return j == null ? 0 : 1;
		}
		if (j == null)
			return -1;
		return i.compareTo(j);
	}

	public static boolean isEmptyValue(Object v) {
		if (v == null)
			return true;
		if (v instanceof String) {
			return ((String) v).length() < 1;
		}
		if (v instanceof byte[]) {
			return ((byte[]) v).length < 1;
		}
		return false;
	}

	public static boolean StringIsEmpty(String s) {
		return (s == null) || (s.length() < 1);
	}

	public static String StringTrim(String s) {
		if (s == null)
			return s;
		return s.trim();
	}

	public static String StringTrimNoEmpty(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	public static boolean StringToBoolean(String s) {
		return (s != null) && (s.compareTo("true") == 0);
	}

	static public int StringLen(String s) {
		if (s == null)
			return 0;
		Pattern pa = Pattern.compile("[\u4E00-\u9FA0]", Pattern.CANON_EQ);
		int idx = 0;
		int rellen = 0;
		while (idx < s.length()) {
			Matcher matcher = pa.matcher(s.subSequence(idx, idx + 1));
			if (matcher.matches()) {
				rellen += 2;
			} else {
				rellen++;
			}
			idx++;
		}
		return rellen;
	}

	static public String SubString(String s, int rellen) {
		if (s == null)
			return s;
		StringBuffer buf = new StringBuffer();
		Pattern pa = Pattern.compile("[\u4E00-\u9FA0]", Pattern.CANON_EQ);
		int idx = 0;
		while (idx < s.length()) {
			Matcher matcher = pa.matcher(s.subSequence(idx, idx + 1));
			if (matcher.matches()) {
				rellen -= 2;
			} else {
				rellen--;
			}
			if (rellen >= 0) {
				buf.append(s.charAt(idx));
			}
			idx++;
			if (rellen <= 0) {
				buf.append("...");
				break;
			}
		}
		return buf.toString();
	}

	static public String SubStringNoDot(String s, int rellen) {
		if (s == null)
			return s;
		StringBuffer buf = new StringBuffer();
		Pattern pa = Pattern.compile("[\u4E00-\u9FA0]", Pattern.CANON_EQ);
		int idx = 0;
		while (idx < s.length()) {
			Matcher matcher = pa.matcher(s.subSequence(idx, idx + 1));
			if (matcher.matches()) {
				rellen -= 2;
			} else {
				rellen--;
			}
			if (rellen >= 0) {
				buf.append(s.charAt(idx));
			}
			idx++;
			if (rellen <= 0) {
				break;
			}
		}
		return buf.toString();
	}

	static public String RightSubString(String s, int rellen) {
		if (s == null)
			return s;
		String str = "";
		Pattern pa = Pattern.compile("[\u4E00-\u9FA0]", Pattern.CANON_EQ);
		int idx = s.length() - 1;
		while (idx >= 0) {
			Matcher matcher = pa.matcher(s.subSequence(idx, idx + 1));
			if (matcher.matches()) {
				rellen -= 2;
			} else {
				rellen--;
			}
			if (rellen >= 0) {
				str = s.charAt(idx) + str;
			}
			idx--;
			if (rellen <= 0) {
				break;
			}
		}
		if (idx > 0) {
			str = "..." + str;
		}
		return str;
	}

	static public String RightDotSubString(String s, int dotcount, int rellen) {
		if (s == null)
			return s;
		int sp = 0;
		while (dotcount > 0) {
			if (sp < 1)
				sp = s.lastIndexOf('.');
			else
				sp = s.lastIndexOf('.', sp - 1);
			if (sp < 0)
				break;
			dotcount--;
		}
		if (sp > 0) {
			s = s.substring(sp + 1);
		}
		String str = "";
		Pattern pa = Pattern.compile("[\u4E00-\u9FA0]", Pattern.CANON_EQ);
		int idx = s.length() - 1;
		while (idx >= 0) {
			Matcher matcher = pa.matcher(s.subSequence(idx, idx + 1));
			if (matcher.matches()) {
				rellen -= 2;
			} else {
				rellen--;
			}
			if (rellen >= 0) {
				str = s.charAt(idx) + str;
			}
			idx--;
			if (rellen <= 0) {
				break;
			}
		}
		if (idx > 0) {
			str = "..." + str;
		}
		return str;
	}

	static public String formatLenString(String s, int rellen) {
		if (s == null)
			s = "";
		StringBuffer buf = new StringBuffer();
		if (rellen < s.length()) {
			buf.append(s.substring(s.length() - rellen));
		} else {
			int len = rellen - s.length();
			while (len > 0) {
				buf.append("0");
				len--;
			}
			buf.append(s);
		}
		return buf.toString();
	}

	static public Map<String, String> reqParamSplit(String params, String paramsplit) {
		if (!textUnit.StringIsEmpty(params)) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			String str;
			if (!textUnit.StringIsEmpty(paramsplit)) {
				str = systemUtil.replace(params, paramsplit, "&");
			} else {
				str = params;
			}
			java.util.StringTokenizer st = new java.util.StringTokenizer(str, "&");
			while (st.hasMoreElements()) {
				String paramstr = st.nextElement().toString();
				int pos = paramstr.indexOf("=");
				if (pos > 0) {
					if (pos < (paramstr.length() - 1)) {
						parameters.put(paramstr.substring(0, pos).trim(), paramstr.substring(pos + 1).trim());
					} else {
						parameters.put(paramstr.substring(0, pos).trim(), "");
					}
				}
			}
			return parameters;
		}
		return java.util.Collections.emptyMap();

	}

	static public int sortCompareObject(Object m1, Object m2) {
		if (m1 == null) {
			return m2 == null ? 0 : 1;
		}
		if (m2 == null)
			return -1;
		if (m1 instanceof String) {
			return ((String) m1).compareTo(m2.toString());
		}
		if (m1 instanceof Short) {
			return ((Short) m1).compareTo((Short) m2);
		}
		if (m1 instanceof Byte) {
			return ((Byte) m1).compareTo((Byte) m2);
		}
		if (m1 instanceof Integer) {
			return ((Integer) m1).compareTo((Integer) m2);
		}
		if (m1 instanceof Long) {
			return ((Long) m1).compareTo((Long) m2);
		}
		if (m1 instanceof Float) {
			return ((Float) m1).compareTo((Float) m2);
		}
		if (m1 instanceof Double) {
			return ((Double) m1).compareTo((Double) m2);
		}
		if (m1 instanceof Date) {
			return ((Date) m1).compareTo((Date) m2);
		}
		return m1.toString().compareTo(m2.toString());
	}

	static public int compareObject(Object m1, Object m2) throws SoftFanUtilException {
		return superCompareObject(m1, m2);
	}

	static public boolean equalsObject(Object m1, Object m2) {
		return superEqualsObject(m1, m2);
	}

	static public int superCompareObject(Object m1, Object m2) throws SoftFanUtilException {
		if (m1 == null) {
			return m2 == null ? 0 : 1;
		}
		if (m2 == null)
			return 1;
		if (!m1.getClass().equals(m2.getClass())) {
			m2 = systemUtil.convertTo(m1.getClass(), m2);
		}
		if (m1 instanceof Boolean) {
			return ((Boolean) m1).compareTo((Boolean) m2);
		}
		if (m1 instanceof Short) {
			return ((Short) m1).compareTo((Short) m2);
		}
		if (m1 instanceof Byte) {
			return ((Byte) m1).compareTo((Byte) m2);
		}
		if (m1 instanceof Integer) {
			return ((Integer) m1).compareTo((Integer) m2);
		}
		if (m1 instanceof Long) {
			return ((Long) m1).compareTo((Long) m2);
		}
		if (m1 instanceof Float) {
			return ((Float) m1).compareTo((Float) m2);
		}
		if (m1 instanceof Double) {
			return ((Double) m1).compareTo((Double) m2);
		}
		if (m1 instanceof Date) {
			return ((Date) m1).compareTo((Date) m2);
		}
		if (m1 instanceof String) {
			return ((String) m1).compareTo((String) m2);
		}
		return m1.toString().compareTo(m2.toString());
	}

	static public boolean superEqualsObject(Object m1, Object m2) {
		if (m1 == null) {
			return m2 == null;
		}
		if (m2 == null)
			return false;
		if (!m1.getClass().equals(m2.getClass())) {
			try {
				m2 = systemUtil.convertTo(m1.getClass(), m2);
			} catch (SoftFanUtilException e) {
				return false;
			}
		}
		if (m1 instanceof Boolean) {
			return ((Boolean) m1).equals((Boolean) m2);
		}
		if (m1 instanceof Short) {
			return ((Short) m1).equals((Short) m2);
		}
		if (m1 instanceof Byte) {
			return ((Byte) m1).equals((Byte) m2);
		}
		if (m1 instanceof Integer) {
			return ((Integer) m1).equals((Integer) m2);
		}
		if (m1 instanceof Long) {
			return ((Long) m1).equals((Long) m2);
		}
		if (m1 instanceof Float) {
			return ((Float) m1).equals((Float) m2);
		}
		if (m1 instanceof Double) {
			return ((Double) m1).equals((Double) m2);
		}
		if (m1 instanceof Date) {
			return ((Date) m1).equals((Date) m2);
		}
		if (m1 instanceof String) {
			return ((String) m1).equals((String) m2);
		}
		return m1.toString().equals(m2.toString());
	}

	// public static String systemInfo =
	// "WhenBodhisattvaAvalokiteshvarawaspracticingtheprofoundPrajnaParamitaheilluminatedtheFiveSkandhasandsawthattheyareallemptyandhecrossedbeyondallsufferinganddifficultyShariputraformdoesnotdifferfromemptinessemptinessdoesnotdifferfromformFormitselfisemptinessemptinessitselfisformSotooarefeelingcognitionformationandconsciousnessShariputraallDharmasareemptyofcharacteristicsTheyarenotproducednotdestroyednotdefilednotpureandtheyneitherincreasenordiminishThereforeinemptinessthereisnoformfeelingcognitionformationorconsciousnessnoeyesearsnosetonguebodyormindnosightssoundssmellstastesobjectsoftouchorDharmasnofieldoftheeyesuptoandincludingnofieldofmindconsciousnessandnoignoranceorendingofignoranceuptoandincludingnooldageanddeathorendingofoldageanddeathThereisnosufferingnoaccumulatingnoextinctionandnoWayandnounderstandingandnoattainingBecausenothingisattainedtheBodhisattvathroughrelianceonPrajnaParamitaisunimpededinhismindBecausethereisnoimpedimentheisnotafraidandheleavesdistorteddream-thinkingfarbehindUltimatelyNirvana!AllBuddhasofthethreeperiodsoftimeattainAnuttara-samyak-sambodhithroughrelianceonPrajnaParamitaThereforeknowthatPrajnaParamitaisaGreatSpiritualMantraaGreatBrightMantraaSupremeMantraanUnequalledMantraItcanremoveallsufferingitisgenuineandnotfalseThatiswhytheMantraofPrajnaParamitawasspokenReciteitlikethis";

}
