package android.softfan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class wf_Db_Server {

	private static wf_Db_Server	instance;

	public static synchronized wf_Db_Server getServer() {
		return instance;
	}

	private Context			context;
	private DbHelper		dbHelper;

	private wf_Db_Factory	dbFactory;

	public wf_Db_Server() {
		instance = this;
	}

	public void initial(Context context) throws wf_Db_Exception {
		this.dbFactory = new wf_Db_Factory();
		this.context = context;
		this.dbHelper = new DbHelper(context);

	}

	public void release() {
	}

	public Context getContext() {
		return context;
	}

	public DbHelper getDbHelper() {
		return dbHelper;
	}

	public SQLiteDatabase getWritableDatabase() {
		return dbHelper.getWritableDatabase();
	}

	public IDbFactory getFactory(String name) throws wf_Db_Exception {
		return dbFactory;
	}

	public IDbFactory getDefaultFactory() throws wf_Db_Exception {
		return dbFactory;
	}

}