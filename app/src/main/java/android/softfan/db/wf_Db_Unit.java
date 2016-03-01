package android.softfan.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.softfan.util.DateUnit;
import android.softfan.util.textUnit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class wf_Db_Unit {
	public static final int	DT_BOOLEAN	= 1;
	public static final int	DT_INT		= 2;
	public static final int	DT_FLOAT	= 3;
	public static final int	DT_DOUBLE	= 4;
	public static final int	DT_DATETIME	= 5;
	public static final int	DT_STRING	= 6;
	public static final int	DT_LONG		= 7;
	public static final int	DT_CLOB		= 8;
	public static final int	DT_BLOB		= 9;
	public static final int	DT_SHORTINT	= 10;

	public static void ApplySQLParam(ArrayList<String> stmt, Object value) throws SQLException {
		if (value != null) {
			if (value instanceof Boolean) {
				stmt.add(((Boolean) value).booleanValue() ? "true" : "false");
			} else if (value instanceof Double) {
				stmt.add(value.toString());
			} else if (value instanceof Float) {
				stmt.add(value.toString());
			} else if (value instanceof Integer) {
				stmt.add(value.toString());
			} else if (value instanceof Long) {
				stmt.add(value.toString());
			} else if (value instanceof Short) {
				stmt.add(value.toString());
			} else if (value instanceof Byte) {
				stmt.add(value.toString());
			} else if (value instanceof Date) {
				stmt.add(Long.toString(((Date) value).getTime()));
			} else {
				stmt.add(value.toString());
			}
		} else {
			stmt.add(null);
		}
	}

	public static void ApplySQLParam(SQLiteStatement stmt, int pidx, Object value) throws SQLException {
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
	}

	public static void ApplySQLParam(wf_Db_Connect db_connect, SQLiteStatement stm, int pidx, Object value, int dt) throws wf_Db_Exception {
		try {
			if (value == null) {
				stm.bindNull(pidx);
				return;
			}
			switch (dt) {
				case DT_BOOLEAN: {
					if (value instanceof Boolean) {
						stm.bindString(pidx, ((Boolean) value).booleanValue() ? "true" : "false");
					} else if (value instanceof Short) {
						stm.bindString(pidx, ((Short) value).shortValue() != 0 ? "true" : "false");
					} else if (value instanceof Integer) {
						stm.bindString(pidx, ((Integer) value).intValue() != 0 ? "true" : "false");
					} else if (value instanceof Long) {
						stm.bindString(pidx, ((Long) value).intValue() != 0 ? "true" : "false");
					} else if (value instanceof Float) {
						stm.bindString(pidx, ((Float) value).intValue() != 0 ? "true" : "false");
					} else if (value instanceof Double) {
						stm.bindString(pidx, ((Double) value).intValue() != 0 ? "true" : "false");
					} else
						stm.bindString(pidx, Boolean.parseBoolean(value.toString()) ? "true" : "false");
					break;
				}
				case DT_SHORTINT:
				case DT_INT:
				case DT_LONG:
					if (value instanceof Short) {
						stm.bindLong(pidx, ((Short) value).shortValue());
					} else if (value instanceof Integer) {
						stm.bindLong(pidx, ((Integer) value).intValue());
					} else if (value instanceof Long) {
						stm.bindLong(pidx, ((Long) value).longValue());
					} else if (value instanceof Float) {
						stm.bindLong(pidx, ((Float) value).longValue());
					} else if (value instanceof Double) {
						stm.bindLong(pidx, ((Double) value).longValue());
					} else
						stm.bindLong(pidx, Short.parseShort(value.toString()));
					break;
				case DT_FLOAT:
				case DT_DOUBLE: {
					if (value instanceof Double) {
						stm.bindDouble(pidx, ((Double) value).doubleValue());
					} else if (value instanceof Float) {
						stm.bindDouble(pidx, ((Float) value).floatValue());
					} else if (value instanceof Integer) {
						stm.bindDouble(pidx, ((Integer) value).intValue());
					} else if (value instanceof Short) {
						stm.bindDouble(pidx, ((Short) value).shortValue());
					} else if (value instanceof Long) {
						stm.bindDouble(pidx, ((Long) value).longValue());
					} else
						stm.bindDouble(pidx, Double.parseDouble(value.toString()));
					break;
				}
				case DT_DATETIME: {
					if (value instanceof Date) {
						stm.bindLong(pidx, ((Date) value).getTime());
					} else {
						Date date = DateUnit.toDate(value.toString());
						stm.bindLong(pidx, date.getTime());
					}
					break;
				}
				case DT_STRING: {
					stm.bindString(pidx, value.toString());
					break;
				}
				case DT_CLOB: {
					stm.bindString(pidx, value.toString());
					break;
				}
				case DT_BLOB: {
					if (value instanceof byte[])
						stm.bindBlob(pidx, (byte[]) value);
					else
						throw new wf_Db_Exception("数据类型不合法");
					break;
				}
				default: {
					throw new wf_Db_Exception("数据类型不合法");
				}
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e.getMessage());
		}
	}

	public static byte[] buildEmptyUpload() {
		byte[] v = new byte[2];
		v[0] = (byte) 0xFF;
		v[1] = (byte) 0xAA;
		return v;
	}

	public static boolean isEmptyUpload(Object v) {
		if (v instanceof byte[]) {
			if (((byte[]) v).length == 2) {
				if (((byte[]) v)[0] == (byte) 0xFF) {
					if (((byte[]) v)[1] == (byte) 0xAA) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static byte[] buildUploadBlob(String fileName, String contentType, byte[] data) throws IOException {
		if (data == null)
			return null;
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		try {
			java.io.DataOutputStream os = new java.io.DataOutputStream(bos);
			try {
				os.writeUTF(fileName);
				os.writeUTF(contentType);
				os.writeInt(data.length);
				if (data.length > 0) {
					os.write(data);
				}
			} finally {
				os.close();
			}
			return bos.toByteArray();
		} finally {
			bos.close();
		}
	}

	public static String getUploadBlobFileName(byte[] data) {
		if (data == null) {
			return "空";
		}
		if (isEmptyUpload(data)) {
			return "未加载";
		}
		String filename = "";
		java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(data);
		try {
			try {
				java.io.DataInputStream in = new java.io.DataInputStream(bin);
				try {
					filename = in.readUTF();
				} finally {
					in.close();
				}
			} finally {
				bin.close();
			}
		} catch (IOException e) {
		}
		return filename;
	}

	public static String buildLimitSql(wf_Db_Connect db_connect, String sql, int position, int limit) {
		StringBuffer buf = new StringBuffer();
		if (limit < 1) {
			buf.append("select ");
			buf.append(sql);
		} else {
			buf.append("select ");
			buf.append(sql);
			buf.append(" limit ");
			buf.append(Integer.toString(position));
			buf.append(",");
			buf.append(Integer.toString(limit));
		}
		return buf.toString();
	}

	public static String buildLimitSqlStart(wf_Db_Connect db_connect, int position, int limit) {
		if (limit < 1)
			return "";
		return "";
	}

	public static String buildLimitSqlEnd(wf_Db_Connect db_connect, int position, int limit) {
		if (limit < 1)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append(" limit ");
		buf.append(Integer.toString(position));
		buf.append(",");
		buf.append(Integer.toString(limit));
		buf.append(" ");
		return buf.toString();
	}

	public static String buildLimitSqlOnWhere(int databaseTypeDrv, int position, int limit) {
		if (limit < 1)
			return "";
		return "";
	}

	public static String embedSqlWhere(String srcSql, String whereSql) {
		if (textUnit.StringIsEmpty(whereSql)) {
			return srcSql;
		}

		int whereStartPos = srcSql.indexOf(" where ");
		if (whereStartPos > 0) {
			whereStartPos += 7;
		}

		int whereEndPos = -1;
		{
			int orderpos;
			if (whereStartPos > 0) {
				orderpos = srcSql.indexOf(" order ", whereStartPos);
			} else {
				orderpos = srcSql.indexOf(" order ");
			}

			int grouppos;
			if (whereStartPos > 0) {
				grouppos = srcSql.indexOf(" group ", whereStartPos);
			} else {
				grouppos = srcSql.indexOf(" group ");
			}

			if (grouppos >= 0) {
				if (orderpos >= 0)
					whereEndPos = Math.min(orderpos, grouppos);
				else
					whereEndPos = grouppos;
			} else {
				whereEndPos = orderpos;
			}
		}

		StringBuffer buf = new StringBuffer();

		if (whereStartPos > 0) {
			buf.append(srcSql.substring(0, whereStartPos));
		} else {
			if (whereEndPos > 0) {
				buf.append(srcSql.substring(0, whereEndPos));
			} else {
				buf.append(srcSql);
			}
			buf.append(" where ");
		}

		buf.append(whereSql);
		if (whereStartPos > 0) {
			buf.append(" and ");
			buf.append(srcSql.substring(whereStartPos));
		} else {
			if (whereEndPos > 0) {
				buf.append(srcSql.substring(whereEndPos));
			}
		}

		return buf.toString();
	}

	public static boolean hasTable(wf_Db_Connect db_connect, String tablename) {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from ");
			sql.append(tablename);
			sql.append(" where (1=2)");
			Cursor rs = db_connect.getConnect().rawQuery(sql.toString(), null);
			try {
				return true;
			} finally {
				rs.close();
			}
		} catch (Exception e) {
		}
		return false;
	}
}