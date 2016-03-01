package android.softfan.db.filter;

import android.softfan.db.wf_Db_Exception;

import java.util.List;

public class expressionLessOrEqual extends expressionBase {

	private String	field;

	public expressionLessOrEqual() {
	}

	public expressionLessOrEqual(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) throws wf_Db_Exception {
		where_sql.append("(");
		if (value == null) {
			throw new wf_Db_Exception("条件表达式 <= 变量不能为空");
		}
		where_sql.append(field);
		where_sql.append(" <= ?");
		where_sql.append(")");
		conditionParamList.add(this);
	}

	public expressionBase getCopy() {
		expressionLessOrEqual expression = new expressionLessOrEqual();
		expression.setField(this.getField());
		expression.setValue(this.getValue());
		return expression;
	}
}
