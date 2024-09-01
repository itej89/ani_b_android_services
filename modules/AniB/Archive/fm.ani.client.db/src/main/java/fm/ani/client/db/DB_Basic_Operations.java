package fm.ani.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Framework.DataTypes.GlobalContext;
import fm.ani.client.db.DataTypes.Column_Definition;

import static Framework.DataTypes.GlobalContext.context;

/**
 * Created by tej on 03/02/18.
 */

public class DB_Basic_Operations extends SQLiteOpenHelper {


    //The Android's default system path of your application database.

    private static String DB_PATH = "/data/data/" + context.getPackageName() + "/databases";

    private static String DB_NAME = "CommandStore.sqlite";

    private final Context myContext;

//    private  final static Map<String, SQLiteDatabase> DataBases = new HashMap<>();

    SQLiteDatabase SQLDataBase;

    String databasePath;

    private  String FullDBPath = "";



    public String GetDBDirectoryPath() {
        return DB_PATH;
    }

    public String GetDBPath() {
        return FullDBPath;
    }

    public static String GetDefaultDBPathFromName(String DBName)
    {
        if(android.os.Build.VERSION.SDK_INT >= 17) {
            return context.getApplicationInfo().dataDir + "/databases"+"/"+DBName;
        }
        else {
            return "/data/data/" + context.getPackageName() + "/databases"+"/"+DBName;
        }
    }

    public DB_Basic_Operations(Context context)
    {
        super(context, DB_NAME, null, 1);

        if(android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases";
        }
        else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases";
        }

        FullDBPath = DB_PATH+"/"+DB_NAME;

        this.myContext = context;
        this.databasePath = GetDBPath();
    }

    public DB_Basic_Operations(Context context,  String DBPath) {
        super(context, DB_NAME, null, 1);

        FullDBPath = DBPath;

        this.myContext = context;
        this.databasePath = GetDBPath();
    }


    public  boolean IsOpen()
    {
        if(SQLDataBase != null && SQLDataBase.isOpen())
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }



    public void InitializeDB() {

        SQLDataBase  = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public  String ErrorMessage;

    public Boolean CreateTable(String TableName, ArrayList<Column_Definition> Columns)
    {

    try {
        if (SQLDataBase == null) {
            ErrorMessage = ("Error: DB Null");
            return false;
        }

        if (SQLDataBase.isOpen()) {

            String sql_stmt = "CREATE TABLE IF NOT EXISTS " + TableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT";

            for (Column_Definition column : Columns) {
                sql_stmt += ", " + column.ColumnName + " " + column.ColumnType;
            }

            sql_stmt += ")";

            SQLDataBase.execSQL(sql_stmt);

            //CloseDBConnection();
        } else {
            ErrorMessage = ("Error: Unable to Open DB");
            return false;
        }

        return true;
    }
    catch (Exception e)
    {
        ErrorMessage = ("Error: Unable to Open DB");
        ErrorMessage += "\n"+e.getMessage();
        return  false;
    }

    }

    public Boolean EmptyTable(String TableName) {

        try {
            //CommandStoreDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            if (SQLDataBase.isOpen()) {

                String insertSQL = "DELETE FROM " + TableName;

                SQLDataBase.execSQL(insertSQL);
               // CloseDBConnection();
            } else {
                ErrorMessage = "Error: Unable to Open DB";
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            ErrorMessage = ("Error: Unable to EmptyTable");
            ErrorMessage += "\n"+e.getMessage();
            return  false;
        }

    }

    public Cursor ReadDataFromTable(String SqlQuery) {

    try {
       // CommandStoreDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
        if (SQLDataBase.isOpen()) {


            Cursor cs = SQLDataBase.rawQuery(SqlQuery, null);




            return cs;

        } else {
            ErrorMessage = "Error: Unable to Open DB";
            return null;
        }

    }
    catch (Exception e)
    {
        ErrorMessage = ("Error: Unable to ReadDataFromTable");
        ErrorMessage += "\n"+e.getMessage();
        return null;
    }

    }

    public void CloseDBConnection() {

            if(SQLDataBase.isOpen())
            {
                SQLDataBase.close();
            }

    }

    public long InsertContentValueToTable(String Table_Name, ContentValues values)
    {
        long ID = -1;
        try {
          //  CommandStoreDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            if (SQLDataBase.isOpen()) {

                ID=  SQLDataBase.insert(Table_Name, null, values);
            }
           // CloseDBConnection();
            return ID;
        }
        catch (Exception e)
        {
            ErrorMessage = ("Error: Unable to ExecuteSqlCommand");
            ErrorMessage += "\n"+e.getMessage();
            return ID;
        }
    }


    public Boolean ExecuteSqlCommand(String SqlQuery) {

        try {
          //  CommandStoreDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            if (SQLDataBase.isOpen()) {

                SQLDataBase.execSQL(SqlQuery);
            }
           // CloseDBConnection();
            return true;
        }
        catch (Exception e)
        {
            ErrorMessage = ("Error: Unable to ExecuteSqlCommand");
            ErrorMessage += "\n"+e.getMessage();
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
