package android.softfan.db.filter;

import android.softfan.db.wf_Db_Exception;

import java.util.Iterator;
import java.util.List;

public class expressionNot extends expressionArray {

	public expressionNot() {
		super();
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) throws wf_Db_Exception {
		if (getExpressions() == null)
			return;
		if (getExpressions().size() < 2) {
			where_sql.append("(not ");
			((expressionBase) getExpressions().get(0)).build(where_sql, conditionParamList);
			where_sql.append(")");
			return;
		}
		boolean first = true;
		where_sql.append("(not ");
		for (Iterator<expressionBase> i = getExpressions().iterator(); i.hasNext();) {
			if (first) {
				first = false;
			} else {
				where_sql.append(" and ");
			}
			expressionBase expression = i.next();
			expression.build(where_sql, conditionParamList);
		}
		where_sql.append(")");
	}

	public expressionBase getCopy() {
		expressionNot expression = new expressionNot();
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
