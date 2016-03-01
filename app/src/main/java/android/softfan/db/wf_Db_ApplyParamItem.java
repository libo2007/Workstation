package android.softfan.db;

public class wf_Db_ApplyParamItem {

	private Object	value;
	private int		dt;

	public wf_Db_ApplyParamItem(Object value, int dt) {
		this.value = value;
		this.dt = dt;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getDt() {
		return dt;
	}

	public void setDt(int dt) {
		this.dt = dt;
	}

}
