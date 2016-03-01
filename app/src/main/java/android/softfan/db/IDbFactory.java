package android.softfan.db;

public interface IDbFactory {

	public wf_Db_Connect get() throws wf_Db_Exception;

	public void free(wf_Db_Connect connect);

	public String getDatabaseType();

	public Object getSubmitLock();
}