package android.softfan.db.filter;

import android.database.sqlite.SQLiteStatement;
import android.softfan.db.wf_Db_Exception;

import java.util.ArrayList;

public interface IConditionParam {

	public void applyParam(int pidx, ArrayList<String> stmt) throws wf_Db_Exception;
	
	public void applyParam(int pidx, SQLiteStatement stmt) throws wf_Db_Exception;
}
