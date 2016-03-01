package android.softfan.db.filter;

import android.softfan.db.wf_Db_Exception;

import java.util.Iterator;
import java.util.List;

public class expressionIn extends expressionArray {

	private String	field;

	public expressionIn() {
	}

	public expressionIn(String field, Object value) {
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
			where_sql.append(field);
			where_sql.append(" is null");
		} else {
			where_sql.append(field);
			where_sql.append(" in (");
			if (value instanceof String[]) {
				for (int i = 0; i < ((String[]) value).length; i++) {
					if (i > 0) {
						where_sql.append(",");
					}
					where_sql.append("'");
					where_sql.append(((String[]) value)[i]);
					where_sql.append("'");
				}
			} else if (value instanceof Object[]) {
				for (int i = 0; i < ((Object[]) value).length; i++) {
					if (i > 0) {
						where_sql.append(",");
					}
					where_sql.append("'");
					where_sql.append(((Object[]) value)[i].toString());
					where_sql.append("'");
				}
			} else if (value instanceof List) {
				for (int i = 0; i < ((List<?>) value).size(); i++) {
					Object v = ((List<?>) value).get(i);
					if (v != null) {
						if (i > 0) {
							where_sql.append(",");
						}
						where_sql.append("'");
						where_sql.append(v.toString());
						where_sql.append("'");
					}
				}
			} else {
				if (value instanceof String) {
					where_sql.append((String) value);
					if (getExpressions().size() > 0) {
						boolean first = true;
						where_sql.append(" where ");
						for (Iterator<expressionBase> i = getExpressions().iterator(); i.hasNext();) {
							if (first) {
								first = false;
							} else {
								where_sql.append(" and ");
							}
							expressionBase expression = i.next();
							expression.build(where_sql, conditionParamList);
						}
					}
				}
			}
			where_sql.append(")");
		}
		where_sql.append(")");
	}

	public expressionBase getCopy() {
		expressionIn expression = new expressionIn();
		expression.setField(this.getField());
		expression.setValue(this.getValue());
		if (getExpressions() != null) {
			for (Iterator<expressionBase> i = getExpressions().iterator(); i.hasNext();) {
				expressionBase base = i.next();
				expression.add(base.getCopy());
			}
		}
		return expression;
	}
}
