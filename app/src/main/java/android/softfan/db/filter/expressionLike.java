
package android.softfan.db.filter;

import android.softfan.db.wf_Db_Exception;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class expressionLike extends expressionBase {

	private String	field;

	public expressionLike() {
	}

	public expressionLike(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) throws wf_Db_Exception {
		where_sql.append("(");
		if (value == null) {
			throw new wf_Db_Exception("条件表达式 like 变量不能为空");
		}
		where_sql.append(field);
		where_sql.append(" like ?");
		conditionParamList.add(this);
		where_sql.append(")");
	}

	public void applyParam(int pidx, PreparedStatement stm) throws wf_Db_Exception {
		try {
			if (value == null) {
				stm.setString(pidx, "");
			} else {
				String v = value.toString();
				if (v.startsWith("%") || v.endsWith("%")) {
					stm.setString(pidx, v);
				} else {
					stm.setString(pidx, "%" + v + "%");
				}
			}
		} catch (SQLException e) {
			throw new wf_Db_Exception(e);
		}
	}

	public expressionBase getCopy() {
		expressionLike expression = new expressionLike();
		expression.setField(this.getField());
		expression.setValue(this.getValue());
		return expression;
	}
}
