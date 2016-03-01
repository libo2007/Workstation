package android.softfan.db.filter;

import android.softfan.db.wf_Db_Exception;

import java.util.Iterator;
import java.util.List;

public class expressionOr extends expressionArray {

	public expressionOr() {
		super();
	}

	public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) throws wf_Db_Exception {
		if (getExpressions() == null)
			return;
		if (getExpressions().isEmpty())
			return;
		if (getExpressions().size() < 2) {
			((expressionBase) getExpressions().get(0)).build(where_sql, conditionParamList);
			return;
		}
		boolean first = true;
		where_sql.append("(");
		for (Iterator<expressionBase> i = getExpressions().iterator(); i.hasNext();) {
			if (first) {
				first = false;
			} else {
				where_sql.append(" or ");
			}
			expressionBase expression = i.next();
			expression.build(where_sql, conditionParamList);
		}
		where_sql.append(")");
	}

	public expressionBase getCopy() {
		expressionOr expression = new expressionOr();
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
