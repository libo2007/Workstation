package android.softfan.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.softfan.util.textUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FactJDBCUnit {

	public static int size(wf_Db_Connect connect, String table, String filter) throws wf_Db_Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select count(*) as c ");
			sql.append(" from ");
			sql.append(table);
			if (!textUnit.StringIsEmpty(filter)) {
				sql.append(" where ");
				sql.append(filter);
			}
			SQLiteStatement stmt = connect.getConnect().compileStatement(sql.toString());
			try {
				Cursor rs = connect.getConnect().rawQuery(sql.toString(), null);
				try {
					if (rs.moveToNext()) {
						return rs.getInt(0);
					}
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e.getMessage());
		}
		return 0;
	}

	public static int size(wf_Db_Connect connect, String table, android.softfan.db.filter.expressionBase expression) throws wf_Db_Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select count(*) as c ");
			sql.append(" from ");
			sql.append(table);
			StringBuffer where_sql = new StringBuffer();
			List<android.softfan.db.filter.IConditionParam> conditionParamList = new ArrayList<android.softfan.db.filter.IConditionParam>();
			try {
				expression.build(where_sql, conditionParamList);
				if (where_sql.length() > 0) {
					sql.append(" where ");
					sql.append(where_sql.toString());
				}
				ArrayList<String> params = new ArrayList<String>();
				int pidx = 1;
				for (Iterator<?> i = conditionParamList.iterator(); i.hasNext();) {
					android.softfan.db.filter.IConditionParam param = (android.softfan.db.filter.IConditionParam) i.next();
					param.applyParam(pidx, params);
					pidx++;
				}
				String[] conditions = new String[params.size()];
				params.toArray(conditions);
				Cursor rs = connect.getConnect().rawQuery(where_sql.toString(), conditions);
				try {
					if (rs.moveToNext()) {
						return rs.getInt(0);
					}
				} finally {
					rs.close();
				}
			} finally {
				where_sql = null;
				conditionParamList.clear();
				conditionParamList = null;
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e.getMessage());
		}
		return 0;
	}

	public static int sizeDistinct(wf_Db_Connect connect, String table, String fields, String filter) throws wf_Db_Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select count(*) as c ");
			sql.append(" from (select distinct ");
			sql.append(fields);
			sql.append(" from ");
			sql.append(table);
			if (!textUnit.StringIsEmpty(filter)) {
				sql.append(" where ");
				sql.append(filter);
			}
			sql.append(") as temp");
			Cursor rs = connect.getConnect().rawQuery(sql.toString(), null);
			try {
				if (rs.moveToNext()) {
					return rs.getInt(0);
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e.getMessage());
		}
		return 0;
	}

	public static int sizeDistinct(wf_Db_Connect connect, String table, String fields, android.softfan.db.filter.expressionBase expression) throws wf_Db_Exception {
		try {
			StringBuffer sql = new StringBuffer();

			sql.append("select count(*) as c ");
			sql.append(" from (select distinct ");
			sql.append(fields);
			sql.append(" from ");
			sql.append(table);

			StringBuffer where_sql = new StringBuffer();
			List<android.softfan.db.filter.IConditionParam> conditionParamList = new ArrayList<android.softfan.db.filter.IConditionParam>();
			try {
				expression.build(where_sql, conditionParamList);
				if (where_sql.length() > 0) {
					sql.append(" where ");
					sql.append(where_sql.toString());
				}
				ArrayList<String> params = new ArrayList<String>();
				int pidx = 1;
				for (Iterator<?> i = conditionParamList.iterator(); i.hasNext();) {
					android.softfan.db.filter.IConditionParam param = (android.softfan.db.filter.IConditionParam) i.next();
					param.applyParam(pidx, params);
					pidx++;
				}
				String[] conditions = new String[params.size()];
				params.toArray(conditions);
				Cursor rs = connect.getConnect().rawQuery(where_sql.toString(), conditions);
				try {
					if (rs.moveToNext()) {
						return rs.getInt(0);
					}
				} finally {
					rs.close();
				}
			} finally {
				where_sql = null;
				conditionParamList.clear();
				conditionParamList = null;
			}
		} catch (Exception e) {
			throw new wf_Db_Exception(e.getMessage());
		}
		return 0;
	}

	public static String buildLoadSql(wf_Db_Connect connect, String fields, String table, String filter, String orderby, int resultLimit) throws wf_Db_Exception {
		return __buildLoadSql(connect, fields, table, filter, orderby, resultLimit, false);
	}

	public static String buildLoadSqlDistinct(wf_Db_Connect connect, String fields, String table, String filter, String orderby, int resultLimit) throws wf_Db_Exception {
		return __buildLoadSql(connect, fields, table, filter, orderby, resultLimit, true);
	}

	public static String __buildLoadSql(wf_Db_Connect connect, String fields, String table, String filter, String orderby, int resultLimit, boolean distinct) throws wf_Db_Exception {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select ");
			if (distinct) {
				sql.append(" distinct ");
			}
			sql.append(wf_Db_Unit.buildLimitSqlStart(connect, 0, resultLimit));
			sql.append(fields);
			sql.append(" from ");
			sql.append(table);
			if (!textUnit.StringIsEmpty(filter)) {
				sql.append(" where ");
				sql.append(filter);
			}
			if (!textUnit.StringIsEmpty(orderby)) {
				sql.append(" order by ");
				sql.append(orderby);
			}
			sql.append(wf_Db_Unit.buildLimitSqlEnd(connect, 0, resultLimit));
			return sql.toString();
		} catch (Exception e) {
			throw new wf_Db_Exception(e.getMessage());
		}
	}

	public static String buildLoadSql(wf_Db_Connect connect, String fields, String table, String filter, String orderby, int top, int resultLimit) throws wf_Db_Exception {
		return __buildLoadSql(connect, fields, table, filter, orderby, top, resultLimit, false);
	}

	public static String buildLoadSqlDistinct(wf_Db_Connect connect, String fields, String table, String filter, String orderby, int top, int resultLimit) throws wf_Db_Exception {
		return __buildLoadSql(connect, fields, table, filter, orderby, top, resultLimit, true);
	}

	private static String __buildLoadSql(wf_Db_Connect connect, String fields, String table, String filter, String orderby, int top, int resultLimit, boolean distinct) throws wf_Db_Exception {
		StringBuffer sql = new StringBuffer();

		if (textUnit.StringIsEmpty(orderby)) {
			throw new wf_Db_Exception("必须有排序字段(Order by)");
		}

		sql.append("select ");
		if (distinct) {
			sql.append(" distinct ");
		}
		sql.append(fields);

		sql.append(" from ");
		sql.append(table);

		if (!textUnit.StringIsEmpty(filter)) {
			sql.append(" where ");
			sql.append(filter);
		}

		sql.append(" order by ");
		sql.append(orderby);

		sql.append(" limit ");
		sql.append(top);
		sql.append(",");
		sql.append(resultLimit);

		return sql.toString();
	}

}
