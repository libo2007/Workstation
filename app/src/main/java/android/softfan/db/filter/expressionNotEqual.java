package android.softfan.db.filter;

import java.util.List;

public class expressionNotEqual extends expressionBase {
	private String	field;

	public expressionNotEqual() {
	}

	public expressionNotEqual(String field, Object value) {
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
			where_sql.append(" is not null");
		} else {
			where_sql.append(field);
			where_sql.append(" is null) or (");
			where_sql.append(field);
			where_sql.append(" <> ?");
			conditionParamList.add(this);
		}
		where_sql.append(")");
	}

	public expressionBase getCopy() {
		expressionNotEqual expression = new expressionNotEqual();
		expression.setField(this.getField());
		expression.setValue(this.getValue());
		return expression;
	}
}
