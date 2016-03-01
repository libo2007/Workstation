package android.softfan.db.filter;

import java.util.List;

public class expressionIsNull extends expressionBase {
    private String field;

    public expressionIsNull() {
    }

    public expressionIsNull(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void build(StringBuffer where_sql, List<IConditionParam> conditionParamList) {
        where_sql.append("(");
        where_sql.append(field);
        where_sql.append(" is null");
        where_sql.append(")");
    }
    
    public expressionBase getCopy() {
        expressionIsNull expression = new expressionIsNull();
        expression.setField(this.getField());
        expression.setValue(this.getValue());
        return expression;
    }
}
