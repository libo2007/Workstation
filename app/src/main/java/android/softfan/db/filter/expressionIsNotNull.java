package android.softfan.db.filter;

import java.util.List;

public class expressionIsNotNull extends expressionBase {
    private String field;

    public expressionIsNotNull() {
    }

    public expressionIsNotNull(String field) {
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
        where_sql.append(" is not null");
        where_sql.append(")");
    }
    
    public expressionBase getCopy() {
        expressionIsNotNull expression = new expressionIsNotNull();
        expression.setField(this.getField());
        expression.setValue(this.getValue());
        return expression;
    }
}
