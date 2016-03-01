package android.softfan.db.filter;

import java.util.List;

public class expressionEqualProperty extends expressionBase {
	private String	field;

	public expressionEqualProperty() {
	}

	public expressionEqualProperty(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) {
		where_sql.append("(");
		if (value == null) {
			where_sql.append(field);
			where_sql.append(" is null");
		} else {
			where_sql.append(field);
			where_sql.append("=");
			where_sql.append(value.toString());
		}
		where_sql.append(")");
	}

	public expressionBase getCopy() {
		expressionEqualProperty expression = new expressionEqualProperty();
		expression.setField(this.getField());
		expression.setValue(this.getValue());
		return expression;
	}
}
