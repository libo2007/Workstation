package android.softfan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {

	private static DbHelper	dbHelper;

	public DbManager(Context context) {
		dbHelper = new DbHelper(context);
	}

	public static SQLiteDatabase getWritableDatabase() {
		return dbHelper.getWritableDatabase();
	}

	public static DbHelper getDbHelper() {
		return dbHelper;
	}

}
