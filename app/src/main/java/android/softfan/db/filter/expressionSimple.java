
package android.softfan.db.filter;

import android.softfan.db.wf_Db_Exception;

import java.util.List;

public class expressionSimple extends expressionBase {

	public expressionSimple() {
	}

	public expressionSimple(Object value) {
		this.value = value;
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) throws wf_Db_Exception {
		if (value == null) {
			throw new wf_Db_Exception("条件表达式 simple 变量不能为空");
		}
		where_sql.append("(");
		where_sql.append(value.toString());
		where_sql.append(")");
	}

	public expressionBase getCopy() {
		expressionSimple expression = new expressionSimple();
		expression.setValue(this.getValue());
		return expression;
	}
}
