
package android.softfan.db.filter;


public class Expression {

	// 相等(=)
	public static expressionBase eq(String field, Object value) {
		return new expressionEqual(field, value);
	}

	// 不相等(=)
	public static expressionBase notEq(String field, Object value) {
		return new expressionNotEqual(field, value);
	}

	// 相等(=)
	public static expressionBase ep(String field, Object value) {
		return new expressionEqualProperty(field, value);
	}

	// 大于等于(>=)
	public static expressionBase ge(String field, Object value) {
		return new expressionGreaterOrEqual(field, value);
	}

	// 大于(>)
	public static expressionBase gt(String field, Object value) {
		return new expressionGreater(field, value);
	}

	// 小于等于(<=)
	public static expressionBase le(String field, Object value) {
		return new expressionLessOrEqual(field, value);
	}

	// 小于(<)
	public static expressionBase lt(String field, Object value) {
		return new expressionLess(field, value);
	}

	// 表达式
	public static expressionBase simple(Object value) {
		return new expressionSimple(value);
	}

	// 在...中(in)
	public static expressionIn in(String field, Object value) {
		return new expressionIn(field, value);
	}

	// 不在...中(in)
	public static expressionBase notIn(String field, Object value) {
		expressionNot exp = new expressionNot();
		exp.add(new expressionIn(field, value));
		return exp;
	}

	// 存在(exists)
	public static expressionExists exists(String sql) {
		return new expressionExists(sql);
	}

	// 相似(like)
	public static expressionBase like(String field, Object value) {
		return new expressionLike(field, value);
	}

	public static expressionBase isNull(String field) {
		return new expressionIsNull(field);
	}

	public static expressionBase isNotNull(String field) {
		return new expressionIsNotNull(field);
	}

	public static expressionNot not() {
		return new expressionNot();
	}

	// and
	public static expressionAnd and() {
		return new expressionAnd();
	}

	// and
	public static expressionAnd and(expressionBase[] expressions) {
		expressionAnd and = new expressionAnd();
		for (int i = 0; i < ((expressionBase[]) expressions).length; i++) {
			and.add(((expressionBase[]) expressions)[i]);
		}
		return and;
	}

	// or
	public static expressionOr or() {
		return new expressionOr();
	}

	// or
	public static expressionOr or(expressionBase[] expressions) {
		expressionOr or = new expressionOr();
		for (int i = 0; i < ((expressionBase[]) expressions).length; i++) {
			or.add(((expressionBase[]) expressions)[i]);
		}
		return or;
	}

}