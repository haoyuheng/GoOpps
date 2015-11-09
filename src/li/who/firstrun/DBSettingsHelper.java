package li.who.firstrun;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBSettingsHelper
  extends SQLiteOpenHelper
{
  private static final String dbName = "DBSettings";
  private static final int dbVersion = 1;
  private static final String isFirstRun = "IsFirstRun";
  private Cursor cursor;
  private SQLiteDatabase db;
  
  public DBSettingsHelper(Context paramContext)
  {
    super(paramContext, "DBSettings", null, 1);
  }
  
  
  
  public boolean getIsFirstRun()
  {
    
    this.db = getReadableDatabase();
    this.cursor = this.db.rawQuery("SELECT * FROM SettingMaster", null);
    Boolean localBoolean = true;
    if ((this.cursor != null) && (this.cursor.moveToFirst())) {
      do
      {
        localBoolean = Boolean.valueOf(this.cursor.getString(this.cursor.getColumnIndex("IsFirstRun")).contains("true"));
      } while (this.cursor.moveToNext());
    }
    this.cursor.close();
    this.db.close();
    return localBoolean;
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
	  paramSQLiteDatabase.execSQL("CREATE TABLE SettingMaster (IsFirstRun BOOLEAN)");
	   
	  paramSQLiteDatabase.execSQL("INSERT INTO SettingMaster (IsFirstRun) VALUES ('true')");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {}
  
  public void updateIsFirstRun(Boolean paramBoolean)
  {
    this.db = getWritableDatabase();
    this.db.execSQL("UPDATE SettingMaster SET IsFirstRun = '" + paramBoolean + "'");
    this.db.close();
  }
  
}
