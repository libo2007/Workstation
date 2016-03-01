package android.softfan.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUnit {

	private static final String		TimeIdShort1FormatString	= "yyMM";
	private static final String		TimeIdShortFormatString		= "yyMMdd";
	private static final String		TimeIdFormatString			= "yyyyMMdd";

	private static final String		ShortDate1FormatString		= "yy-MM";
	private static final String		ShortDate2FormatString		= "yyyy-MM";
	private static final String		ShortDateFormatString		= "yy-MM-dd";
	private static final String		NormalDateFormatString		= "yyyy-MM-dd";
	private static final String		LongDateTimeFormatString	= "yyyy-MM-dd HH:mm:ss";
	private static final String		RealDateTimeFormatString	= "yyyy-MM-dd HH:mm:ss:SSS";

	private static final String		ShortDateFormatCNString1	= "yy年MM月dd日";
	private static final String		ShortDateFormatCNString		= "yyyy年MM月dd日";
	private static final String		NormalDateFormatCNString	= "yyyy年MM月dd日 HH:mm";
	private static final String		LongDateTimeFormatCNString	= "yyyy年MM月dd日 HH:mm:ss";
	private static final String		RealDateTimeFormatCNString	= "yyyy年MM月dd日 HH:mm:ss:SSS";

	private static SimpleDateFormat	TimeIdShortFormat			= new SimpleDateFormat(TimeIdShortFormatString);

	private static SimpleDateFormat	TimeIdFormat				= new SimpleDateFormat(TimeIdFormatString);

	private static SimpleDateFormat	ShortDateFormat				= new SimpleDateFormat(ShortDateFormatString);
	private static SimpleDateFormat	NormalDateFormat			= new SimpleDateFormat(NormalDateFormatString);
	private static SimpleDateFormat	LongDateTimeFormat			= new SimpleDateFormat(LongDateTimeFormatString);
	private static SimpleDateFormat	RealDateTimeFormat			= new SimpleDateFormat(RealDateTimeFormatString);

	private static SimpleDateFormat	ShortDateFormatCN1			= new SimpleDateFormat(ShortDateFormatCNString1);
	private static SimpleDateFormat	ShortDateCNFormat			= new SimpleDateFormat(ShortDateFormatCNString);
	private static SimpleDateFormat	NormalDateCNFormat			= new SimpleDateFormat(NormalDateFormatCNString);
	private static SimpleDateFormat	LongDateCNFormat			= new SimpleDateFormat(LongDateTimeFormatCNString);
	private static SimpleDateFormat	RealDateCNFormat			= new SimpleDateFormat(RealDateTimeFormatCNString);

	public static void Init() {
		// DateFormat = new SimpleDateFormat(DateFormatString);
		// DateFormatSymbols sym = new DateFormatSymbols();
		// sym.setLocalPatternChars(DateFormatString);
		// DateFormat.setDateFormatSymbols(sym);
	}

	public static String toShortDateText(Date date) {
		synchronized (ShortDateFormat) {
			return ShortDateFormat.format(date);
		}
	}

	public static String toNormalDateText(Date date) {
		synchronized (NormalDateFormat) {
			return NormalDateFormat.format(date);
		}
	}

	public static String toLongDateText(Date date) {
		synchronized (LongDateTimeFormat) {
			return LongDateTimeFormat.format(date);
		}
	}

	public static String toRealDateText(Date date) {
		synchronized (RealDateTimeFormat) {
			return RealDateTimeFormat.format(date);
		}
	}

	public static String toShortCNDateText(Date date) {
		synchronized (ShortDateCNFormat) {
			return ShortDateCNFormat.format(date);
		}
	}

	public static String toNormalCNDateText(Date date) {
		synchronized (NormalDateCNFormat) {
			return NormalDateCNFormat.format(date);
		}
	}

	public static String toLongCNDateText(Date date) {
		synchronized (LongDateCNFormat) {
			return LongDateCNFormat.format(date);
		}
	}

	public static String toRealCNDateText(Date date) {
		synchronized (LongDateCNFormat) {
			return LongDateCNFormat.format(date);
		}
	}

	public static String toDateText(Date date) {
		synchronized (NormalDateFormat) {
			return NormalDateFormat.format(date);
		}
	}

	public static String toText(Date date) {
		synchronized (LongDateTimeFormat) {
			return LongDateTimeFormat.format(date);
		}
	}

	public static Date toDate(String sdate) throws ParseException {
		if (textUnit.StringIsEmpty(sdate)) {
			return null;
		}

		if (sdate.indexOf('-') > 0) {
			if (sdate.length() == ShortDate1FormatString.length()) {
				synchronized (ShortDateFormat) {
					return ShortDateFormat.parse(sdate + "-01");
				}
			}
			if (sdate.length() == ShortDate2FormatString.length()) {
				synchronized (NormalDateFormat) {
					return NormalDateFormat.parse(sdate + "-01");
				}
			}
			if (sdate.length() <= ShortDateFormatString.length()) {
				synchronized (ShortDateFormat) {
					return ShortDateFormat.parse(sdate);
				}
			}
			if (sdate.length() <= NormalDateFormatString.length()) {
				synchronized (NormalDateFormat) {
					return NormalDateFormat.parse(sdate);
				}
			}
			if (sdate.length() <= LongDateTimeFormatString.length()) {
				synchronized (LongDateTimeFormat) {
					return LongDateTimeFormat.parse(sdate);
				}
			}
			synchronized (RealDateTimeFormat) {
				return RealDateTimeFormat.parse(sdate);
			}
		}

		if (sdate.indexOf('日') > 0) {
			if (sdate.length() <= ShortDateFormatCNString1.length()) {
				synchronized (ShortDateFormatCN1) {
					return ShortDateFormatCN1.parse(sdate);
				}
			}
			if (sdate.length() <= ShortDateFormatCNString.length()) {
				synchronized (ShortDateCNFormat) {
					return ShortDateCNFormat.parse(sdate);
				}
			}
			if (sdate.length() <= NormalDateFormatCNString.length()) {
				synchronized (NormalDateCNFormat) {
					return NormalDateCNFormat.parse(sdate);
				}
			}
			if (sdate.length() <= LongDateTimeFormatCNString.length()) {
				synchronized (LongDateCNFormat) {
					return LongDateCNFormat.parse(sdate);
				}
			}
			synchronized (RealDateCNFormat) {
				return RealDateCNFormat.parse(sdate);
			}
		}

		if (sdate.length() == TimeIdShort1FormatString.length()) {
			synchronized (TimeIdShortFormat) {
				return TimeIdShortFormat.parse(sdate + "01");
			}
		}

		if (sdate.length() == TimeIdShortFormatString.length()) {
			synchronized (TimeIdShortFormat) {
				return TimeIdShortFormat.parse(sdate);
			}
		}

		if (sdate.length() == TimeIdFormatString.length()) {
			synchronized (TimeIdFormat) {
				return TimeIdFormat.parse(sdate);
			}
		}

		return DateFormat.getDateInstance().parse(sdate);
	}

	public static Date toDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		d.set(year, month, day, 0, 0, 0);
		return d.getTime();
	}

	public static Date toDayAndHourAndMinute(Date date) {
		if (date == null) {
			return null;
		}
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		int hh = d.get(Calendar.HOUR_OF_DAY);
		int mm = d.get(Calendar.MINUTE);
		d.set(year, month, day, hh, mm, 0);
		return d.getTime();
	}

	public static Date toDayAndHourAndMinuteAndSecond(Date date) {
		if (date == null) {
			return null;
		}
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		int hh = d.get(Calendar.HOUR_OF_DAY);
		int mm = d.get(Calendar.MINUTE);
		int ss = d.get(Calendar.SECOND);
		d.set(year, month, day, hh, mm, ss);
		return d.getTime();
	}

	public static int toTimeId(Date date) {
		if (date == null) {
			return 0;
		}
		synchronized (TimeIdFormat) {
			return Integer.parseInt(TimeIdFormat.format(date));
		}
	}

	public static int toHourId(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		return d.get(Calendar.HOUR_OF_DAY);
	}

	public static int toTodId(Date date, int space) {
		if (date == null) {
			return 0;
		}
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		int hour = d.get(Calendar.HOUR_OF_DAY);
		int minute = d.get(Calendar.MINUTE);
		return hour * 100 + (((minute / space) * space) / 5) * 5;
	}

	public static int toBirthDay(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		return month * 100 + day;
	}
}