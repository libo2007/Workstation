package android.softfan.db.filter;

import java.util.ArrayList;
import java.util.List;

public abstract class expressionArray extends expressionBase {
    private List<expressionBase> expressions;

    public expressionArray() {
        super();
    }

    public List<expressionBase> getExpressions() {
        if (expressions == null)
            expressions = new ArrayList<expressionBase>();
        return expressions;
    }

    public void add(expressionBase expression) {
        getExpressions().add(expression);
    }

}
