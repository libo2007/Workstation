package android.softfan.db.filter;

import android.database.sqlite.SQLiteStatement;
import android.softfan.db.wf_Db_Exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class expressionBase implements IConditionParam {

	protected Object	value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) throws wf_Db_Exception {
	}

	public void applyParam(int pidx, ArrayList<String> stmt) throws wf_Db_Exception {
		try {
			if (value == null) {
				stmt.add(null);
			} else {
				stmt.add(value.toString());
				if (value instanceof Boolean) {
					stmt.add(((Boolean) value).toString());
				} else if (value instanceof Short) {
					stmt.add(((Short) value).toString());
				} else if (value instanceof Integer) {
					stmt.add(((Integer) value).toString());
				} else if (value instanceof Long) {
					stmt.add(((Long) value).toString());
				} else if (value instanceof Float) {
					stmt.add(((Float) value).toString());
				} else if (value instanceof Double) {
					stmt.add(((Double) value).toString());
				} else if (value instanceof Date) {
					stmt.add(Long.toString(((Date) value).getTime()));
				} else {
					stmt.add(value.toString());
				}
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e);
		}
	}

	public void applyParam(int pidx, SQLiteStatement stmt) throws wf_Db_Exception {
		try {
			if (value != null) {
				if (value instanceof Boolean) {
					stmt.bindString(pidx, ((Boolean) value).toString());
				} else if (value instanceof Double) {
					stmt.bindDouble(pidx, ((Double) value).doubleValue());
				} else if (value instanceof Float) {
					stmt.bindDouble(pidx, ((Float) value).floatValue());
				} else if (value instanceof Integer) {
					stmt.bindLong(pidx, ((Integer) value).intValue());
				} else if (value instanceof Long) {
					stmt.bindLong(pidx, ((Long) value).longValue());
				} else if (value instanceof Short) {
					stmt.bindLong(pidx, ((Short) value).shortValue());
				} else if (value instanceof Byte) {
					stmt.bindLong(pidx, ((Byte) value).byteValue());
				} else if (value instanceof Date) {
					stmt.bindLong(pidx, ((Date) value).getTime());
				} else if (value instanceof byte[]) {
					stmt.bindBlob(pidx, (byte[]) value);
				} else {
					stmt.bindString(pidx, value.toString());
				}
			} else {
				stmt.bindNull(pidx);
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e);
		}
	}

	public abstract expressionBase getCopy();

}
