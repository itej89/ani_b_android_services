package fm.ani.client.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.BPM;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.Constants.GeneralConsultation_Table_Columns;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.HEARTBEAT;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.PULSE;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.SPO2;
import fm.ani.client.db.DataTypes.Applet.Nurse.GeneralConsulation.TEMPERATURE;
import fm.ani.client.db.DataTypes.Choreogram.ACTS;
import fm.ani.client.db.DataTypes.Choreogram.BeatsType;
import fm.ani.client.db.DataTypes.Choreogram.Constants.Choreogram_Table_Columns;
import fm.ani.client.db.DataTypes.CommandStore.Captured_Command_Type;
import fm.ani.client.db.DataTypes.CommandStore.Constants.CommandStore_Table_Columns;
import fm.ani.client.db.DataTypes.CommandStore.DataContext;
import fm.ani.client.db.DataTypes.EmSynth.Constants.EmSynth_Table_Columns;
import fm.ani.client.db.DataTypes.EmSynth.EM_SYNTH;
import fm.ani.client.db.DataTypes.CommandStore.Expressions_Type;
import fm.ani.client.db.DataTypes.CommandStore.Machine_Position_Type;
import fm.ani.client.db.DataTypes.CommandStore.Servo_Calibration_Type;
import fm.ani.client.db.DataTypes.CommandStore.Servo_Data_Type;
import fm.ani.client.db.DataTypes.Choreogram.Track;

import static Framework.DataTypes.GlobalContext.context;

/**
 * Created by tej on 02/02/18.
 */

public class DB_Local_Store  {

    private static String APP_BASE_DB = "CommandStore.sqlite";

    public static String APPLET_BASE_DB = "Applet.sqlite";

    public static String GC_TEMPLATE_DB = "GeneralConsulationTemplate.sqlite";

    private static Map<String, DB_Basic_Operations> DataBases = new HashMap<>();

    private String DBKey = "DEFAULT";


    public static String GetDefaultDBPathFromName(String DBName)
    {
        return  DB_Basic_Operations.GetDefaultDBPathFromName(DBName);
    }

    public DB_Local_Store() {

        if(DataBases.keySet().contains("DEFAULT") && DataBases.get("DEFAULT").IsOpen())
        {
            return;
        }

        if(DataBases.keySet().contains("DEFAULT"))
        {
            DataBases.remove("DEFAULT");
        }

        DBKey = "DEFAULT";

        DataBases.put("DEFAULT", new DB_Basic_Operations(context.getApplicationContext()));

       // RemoveDB(DataBases.get("DEFAULT"));
        PlaceDB(DataBases.get("DEFAULT"));

        DataBases.get("DEFAULT").InitializeDB();

    }

    public DB_Local_Store(String DBPath) {
        DBKey = DBPath;
        if(DataBases.keySet().contains(DBPath) && DataBases.get(DBPath).IsOpen())
        {
            return;
        }

        if(DataBases.keySet().contains(DBPath))
        {
            DataBases.remove(DBPath);
        }

        DataBases.put(DBPath, new DB_Basic_Operations(context.getApplicationContext(), DBPath));

      //  RemoveDB(DataBases.get(DBPath));
        PlaceDB(DataBases.get(DBPath));
        DataBases.get(DBPath).InitializeDB();

    }

    public void CloseDBConnection()
    {
        if(DataBases.keySet().contains(DBKey))
        {
            if(DataBases.get(DBKey).IsOpen())
            DataBases.get(DBKey).CloseDBConnection();

            DataBases.remove(DBKey);
        }
    }



    public String RemoveDB(DB_Basic_Operations DB) {
        String databasePath = DB.GetDBPath();

        File file = new File(databasePath);
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            return "Database deltetion failed!";
        }

        return "Database Removed Successfully!";
    }



    public String PlaceDB(DB_Basic_Operations DB) {
        String databasePath = DB.GetDBPath();

        File dbDir = new File(DB.GetDBDirectoryPath());
        File dbFile = new File(databasePath);


        try {

            if (!dbFile.exists())
            {

                if (!dbDir.mkdirs()) { }

                //Open your local db as the input stream
                String AssetName = databasePath.substring(databasePath.lastIndexOf("/")+1);
                InputStream myInput = context.getAssets().open(AssetName);

                dbDir.createNewFile();
                //Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(DB.GetDBPath(), false);

                //transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                //Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();


//
//                Boolean Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.SERVO_DATA.name(), new ArrayList<Column_Definition>(Arrays.asList(new Column_Definition(CommandStore_Table_Columns.SERVO_DATA_COLUMNS.NAME.name(), CommandStore_Table_Columns.COLUMN_TYPES.TEXT.name()),
//
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_DATA_COLUMNS.ADDRESS.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()),
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_DATA_COLUMNS.MIN_ANGLE.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()),
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_DATA_COLUMNS.MAX_ANGLE.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name())))
//                );
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }
//
//                Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.SERVO_TURN.name(), new ArrayList<Column_Definition>(Arrays.asList(
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.DEGREE.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()),
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.ADC.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()))));
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }
//
//                Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.SERVO_LIFT.name(), new ArrayList<Column_Definition>(Arrays.asList(
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.DEGREE.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()),
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.ADC.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()))));
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }
//
//                Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.SERVO_LEAN.name(), new ArrayList<Column_Definition>(Arrays.asList(
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.DEGREE.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()),
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.ADC.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()))));
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }
//
//                Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.SERVO_TILT.name(), new ArrayList<Column_Definition>(Arrays.asList(
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.DEGREE.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()),
//                        new Column_Definition(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.ADC.name(), CommandStore_Table_Columns.COLUMN_TYPES.NUMBER.name()))));
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }
//
//
//                Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.CAPTURED_COMMANDS.name(), new ArrayList<Column_Definition>(Arrays.asList(
//                        new Column_Definition(CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.name(), CommandStore_Table_Columns.COLUMN_TYPES.TEXT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.COMMAND.name(), CommandStore_Table_Columns.COLUMN_TYPES.TEXT.name()))));
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }
//
//
//                Status = dbHelper.CreateTable(CommandStore_Table_Columns.DBTables.EXPRESSIONS.name(), new ArrayList<Column_Definition>(Arrays.asList(
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.NAME.name(), CommandStore_Table_Columns.COLUMN_TYPES.TEXT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ACTION_DATA.name(), CommandStore_Table_Columns.COLUMN_TYPES.TEXT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.JOY.name(), CommandStore_Table_Columns.COLUMN_TYPES.FLOAT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SURPRISE.name(), CommandStore_Table_Columns.COLUMN_TYPES.FLOAT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.FEAR.name(), CommandStore_Table_Columns.COLUMN_TYPES.FLOAT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SADNESS.name(), CommandStore_Table_Columns.COLUMN_TYPES.FLOAT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ANGER.name(), CommandStore_Table_Columns.COLUMN_TYPES.FLOAT.name()),
//                        new Column_Definition(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.DISGUST.name(), CommandStore_Table_Columns.COLUMN_TYPES.FLOAT.name()))));
//
//                if (!Status) {
//                    return dbHelper.ErrorMessage;
//                }

            }
        } catch (Exception ex)
        {
            Log.d("Error", ex.getMessage());
        }


        return "Database Created Successfully!";
    }



    public  Integer GetServoDegreeFromADC(CommandStore_Table_Columns.DBTables TableName, Integer ADC)
    {
        Servo_Calibration_Type DegreeData = null;

        ArrayList<Servo_Calibration_Type> CalibratedData =  ReadServoCalibrationData(TableName.name());

        if(CalibratedData.size() > 0){
            if(CalibratedData.get(0).ADC > ADC){
                DegreeData = CalibratedData.get(0);
            }
            else  if(CalibratedData.get(CalibratedData.size() - 1).ADC < ADC){
                DegreeData = CalibratedData.get(CalibratedData.size() - 1);
            }
            else
            {

                for (Servo_Calibration_Type Calibration : CalibratedData) {
                    if(Calibration.ADC >= ADC){
                        DegreeData = Calibration;
                        break;
                    }
                }
            }


        }
        else
        {
            return -1;
        }

        return (DegreeData.Degree);


    }
    public Machine_Position_Type ReadMachinePositionByName(String name)
    {
        Machine_Position_Type machine_Position_Type = new Machine_Position_Type("");


        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("SELECT "+ CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.NAME.name()+", "+ CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.TURN.name()+", "+ CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.LIFT.name()+", "+ CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.LEAN.name()+", "+ CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.TILT.name()+" from "+ CommandStore_Table_Columns.DBTables.MACHINE_POSITIONS.name()+" WHERE "+ CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.NAME.name()+ " = '"+name+"'"
        );

        if (cursor.moveToFirst()) {

            String NAME =  cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.NAME.toString()));
            Integer TURN =  cursor.getInt
(cursor.getColumnIndex(CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.TURN.toString()));
            Integer LIFT =  cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.LIFT.toString()));
            Integer LEAN =  cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.LEAN.toString()));
            Integer TILT =  cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.MACHINE_POSITIONS_COLUMNS.TILT.toString()));

            machine_Position_Type = new Machine_Position_Type(NAME, TURN, LIFT, LEAN, TILT);
        }

        cursor.close();
        //dbHelper.CloseDBConnection();
        return machine_Position_Type;
    }

    public ArrayList<Servo_Calibration_Type> ReadServoCalibrationData(String TableName) {

        ArrayList<Servo_Calibration_Type> CalibrationData = new ArrayList<Servo_Calibration_Type>();
        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("select * from " + TableName);

        if (cursor.moveToFirst()) {
            do {
                CalibrationData.add(new Servo_Calibration_Type(cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.DEGREE.toString())),
                        cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.ADC.toString()))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        //dbHelper.CloseDBConnection();
        return CalibrationData;
    }

    public ArrayList<Captured_Command_Type> ReadCapturedCommands(String TableName) {
        ArrayList<Captured_Command_Type> CommandData = new ArrayList<Captured_Command_Type>();
        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("select * from " + TableName);

        if (cursor.moveToFirst()) {
            do {

                CommandData.add(new Captured_Command_Type(cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.toString())),
                        cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.COMMAND.toString()))));


            } while (cursor.moveToNext());
        }
        cursor.close();
        //dbHelper.CloseDBConnection();
        return CommandData;

    }


    public String LoadServoDataIntoDB(Servo_Data_Type Data) {

        String insertSQL = "INSERT INTO " + CommandStore_Table_Columns.DBTables.SERVO_DATA.name() + " (" +
                CommandStore_Table_Columns.SERVO_DATA_COLUMNS.NAME.name() + ", " +
                CommandStore_Table_Columns.SERVO_DATA_COLUMNS.ADDRESS.name() + ", " +
                CommandStore_Table_Columns.SERVO_DATA_COLUMNS.MIN_ANGLE.name() + ", " +
                CommandStore_Table_Columns.SERVO_DATA_COLUMNS.MAX_ANGLE.name() +
                ") VALUES ('(Data.Name)', '(Data.Address)', '(Data.Min_Angle)', '(Data.Max_Angle)')";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        if (!Status) {
            return DataBases.get(DBKey).ErrorMessage;
        }


        return "Servo Data Loaded Successfully.";
    }

    public String _EmptyTable(String TableName) {

        Boolean Status = DataBases.get(DBKey).EmptyTable(TableName);

        if (!Status) {
            return DataBases.get(DBKey).ErrorMessage;
        }


        return "Data deleted from Table.";
    }

    public Captured_Command_Type ReadCommandByName(String name) {
        Captured_Command_Type Captured_Command_Data = new Captured_Command_Type("", "");
        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("SELECT " + CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.name() + ", " +
                "" + CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.COMMAND.name() + " from " + CommandStore_Table_Columns.DBTables.CAPTURED_COMMANDS.name() +
                " WHERE " + CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.name() + " = '"+name+"'"
        );


        if (cursor.moveToFirst()) {



            String CommandName = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.toString()));
            String Command = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.COMMAND.toString()));
            Captured_Command_Data = new Captured_Command_Type(CommandName, Command);

        }

        cursor.close();
       // dbHelper.CloseDBConnection();
        return Captured_Command_Data;
    }

    public String DeleteCommandByName(String name) {
        String deleteQuery = "DELETE FROM " + CommandStore_Table_Columns.DBTables.CAPTURED_COMMANDS.name() + " WHERE " +
                CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.name() + " = '" + name + "'";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(deleteQuery);


        if (!Status) {
            return DataBases.get(DBKey).ErrorMessage;
        }


        return "Command Deleted Successfully!";
    }

    public String saveCommand(Captured_Command_Type Data) {
        Captured_Command_Type Found_Command_Data = ReadCommandByName(Data.Name);
        if (Found_Command_Data.Name != "") {
            DeleteCommandByName(Found_Command_Data.Name);
        }

        String insertSQL = "INSERT INTO " + (CommandStore_Table_Columns.DBTables.CAPTURED_COMMANDS.name()) + " (" +
                CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.NAME.name() + ", " +
                CommandStore_Table_Columns.CAPTURED_COMMAND_COLUMNS.COMMAND.name() +
                ") VALUES ('" + Data.Name + "', '" + Data.Command + "')";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        if (!Status) {
            return DataBases.get(DBKey).ErrorMessage;
        }

      //  dbHelper.CloseDBConnection();
        return "Command Data Saved Successfully!";

    }


    public String saveCalibrationData(Servo_Calibration_Type Data) {

        ContentValues row = new ContentValues();
        row.put(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.DEGREE.name(), Data.Degree);
        row.put(CommandStore_Table_Columns.SERVO_CALIBRATION_COLUMNS.ADC.name(), Data.ADC);
        long ID = DataBases.get(DBKey).InsertContentValueToTable(Data.Name, row);

        if (ID == -1) {
            return DataBases.get(DBKey).ErrorMessage;
        }

       // dbHelper.CloseDBConnection();
        return "Calibration Data Saved Successfully!";
    }




    public String  saveInContext(DataContext Data ) {

        ArrayList<DataContext> ContextData = ReadFromContext(Data.KEY);

        if(ContextData.size() > 0)
        {
             DeleteFromContext(Data.KEY);
        }

        String insertSQL = "INSERT INTO "+(CommandStore_Table_Columns.DBTables.CONTEXT.name()) + " (" +
                CommandStore_Table_Columns.CONTEXT_COLUMNS.KEY.name() + ", " +
                CommandStore_Table_Columns.CONTEXT_COLUMNS.VALUE.name() +
                ") VALUES ('"+ Data.KEY+"', '"+Data.VALUE+"')";

        Boolean  Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }

        return "Data Context Saved Successfully!";
    }

    public  String DeleteFromContext(String KEY) {


        Boolean  Status = DataBases.get(DBKey).ExecuteSqlCommand("delete from "+ CommandStore_Table_Columns.DBTables.CONTEXT.name()+" WHERE "+ CommandStore_Table_Columns.CONTEXT_COLUMNS.KEY.name()+ " = '"+KEY+"'");

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }

        return "Data Context delete Successful!";

    }

    public  ArrayList<DataContext> ReadFromContext(String KEY) {

        ArrayList<DataContext> ContextData = new ArrayList<DataContext>();


        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("SELECT " + CommandStore_Table_Columns.CONTEXT_COLUMNS.VALUE.name()+ " from " + CommandStore_Table_Columns.DBTables.CONTEXT.name() +
                " WHERE " + CommandStore_Table_Columns.CONTEXT_COLUMNS.KEY.name() + " = '"+KEY+"'"
        );


        if(cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {


                    String rKEY = CommandStore_Table_Columns.CONTEXT_COLUMNS.KEY.toString();
                    String rVALUE = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.CONTEXT_COLUMNS.VALUE.toString()));
                    DataContext ContextRow = new DataContext(rKEY, rVALUE);
                    ContextData.add(ContextRow);

                } while (cursor.moveToNext());
            }

            cursor.close();

        }

        return ContextData;

    }

    public String  saveAct(ACTS Data) {
        String insertSQL = "INSERT INTO "+(Choreogram_Table_Columns.DBTables.ACTS.name()) + " (" +
                Choreogram_Table_Columns.ACTS_COLUMNS.ACT_NAME.name() + ", " +
                Choreogram_Table_Columns.ACTS_COLUMNS.ACT_AUDIO.name() +
                ") VALUES ('"+(Data.Name)+"', '"+(Data.Audio)+"')";

        Boolean  Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }

        return "Animation Act Saved Successfully!";
    }

    public ArrayList<ACTS> ReadActWithID(int ID) {

        ArrayList<ACTS> ActsData = new ArrayList<ACTS>();

        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("select * from "+ Choreogram_Table_Columns.DBTables.ACTS.name()+" WHERE "+ Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.name()+" = '"+(ID)+"'");

        if (cursor.moveToFirst()) {
            do {


                String ActName = cursor.getString(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_NAME.toString()));
                String ActAudio = cursor.getString(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_AUDIO.toString()));
                ACTS ContextRow = new ACTS(ActName, ID, ActAudio);
                ActsData.add(ContextRow);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return ActsData;
    }

    public int GetLastActID()
    {
        int ID = -1;
        String selectSQL =  "SELECT "+ Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.name()+" FROM "+ Choreogram_Table_Columns.DBTables.ACTS.name()+" ORDER BY "+ Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.name()+" DESC LIMIT 1";

        Cursor cursor  = DataBases.get(DBKey).ReadDataFromTable( selectSQL);

        if (cursor.moveToFirst()) {
            do {


                ID = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.toString()));
                break;

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ID;
    }

    public String DeleteAct(int ID)
    {
        boolean Status = DataBases.get(DBKey).ExecuteSqlCommand("DELETE from "+ Choreogram_Table_Columns.DBTables.ACTS.name()+" WHERE "+ Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.name()+" = '"+(ID)+"'");

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }


        return "Deleted Expression Successfully.";
    }

    public  ArrayList<ACTS> ReadActs(String TableName) {

        ArrayList<ACTS> ActsData = new  ArrayList<ACTS>();
        Cursor cursor  = DataBases.get(DBKey).ReadDataFromTable("select * from "+TableName);

        if (cursor.moveToFirst()) {
            do {


                String ActName = cursor.getString(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_NAME.toString()));
                int  ActID = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.toString()));
                String Audio = cursor.getString(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_AUDIO.toString()));
                ACTS ContextRow = new ACTS(ActName, ActID, Audio);
                ActsData.add(ContextRow);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return ActsData;
    }

    public int GetLastBeatID()
    {
        int ID = -1;
        String selectSQL =  "SELECT "+ Choreogram_Table_Columns.BEATS_COLUMNS.BEAT_ID.name()+" FROM "+ Choreogram_Table_Columns.DBTables.BEATS.name()+" ORDER BY "+ Choreogram_Table_Columns.BEATS_COLUMNS.BEAT_ID.name()+" DESC LIMIT 1";

        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable(selectSQL);

        if (cursor.moveToFirst()) {
            do {

                ID = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.ACTS_COLUMNS.ACT_ID.toString()));
               break;
            } while (cursor.moveToNext());
        }

        cursor.close();

        return ID;
    }

    public String DeleteBeatByActID(int ACT_ID)
    {
        boolean Status = DataBases.get(DBKey).ExecuteSqlCommand("DELETE from "+ Choreogram_Table_Columns.DBTables.BEATS.name()+" WHERE "+ Choreogram_Table_Columns.BEATS_COLUMNS.ACT_ID.name()+" = '"+(ACT_ID)+"'");

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }


        return "Deleted Expression Successfully.";
    }

    public String DeleteBeatByBeatID(int BEAT_ID)
    {
        boolean Status = DataBases.get(DBKey).ExecuteSqlCommand("DELETE from "+ Choreogram_Table_Columns.DBTables.BEATS.name()+" WHERE "+ Choreogram_Table_Columns.BEATS_COLUMNS.BEAT_ID.name()+" = '"+(BEAT_ID)+"'");

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }


        return "Deleted Expression Successfully.";
    }

    public String  saveBeat(BeatsType Data) {
        String insertSQL = "INSERT INTO "+(Choreogram_Table_Columns.DBTables.BEATS.name()) + " (" +
                Choreogram_Table_Columns.BEATS_COLUMNS.ACT_ID.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.ACTION_DATA.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.JOY.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.SURPRISE.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.FEAR.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.SADNESS.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.ANGER.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.DISGUST.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.StartSec.name() + ", " +
                Choreogram_Table_Columns.BEATS_COLUMNS.EndSec.name() +
                ") VALUES ('"+(Data.Act_Id)+"', '"+(Data.Action_Data)+"', '"+(Data.JOY)+"', '"+(Data.SURPRISE)+"', '"+(Data.FEAR)+"', '"+(Data.SADNESS)+"', '"+(Data.ANGER)+"', '"+(Data.DISGUST)+"', '"+(Data.StartSec)+"', '"+(Data.EndSec)+"')";

        boolean  Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }

        return "Animation Act Saved Successfully!";
    }

    public  ArrayList<BeatsType> ReadBeatsByBeatID(String TableName, int BEAT_ID) {

        ArrayList<BeatsType> Beats = new ArrayList<BeatsType>();
        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("select * from "+TableName+" WHERE "+ Choreogram_Table_Columns.BEATS_COLUMNS.BEAT_ID.name()+" = '"+(BEAT_ID)+"'");

        if (cursor.moveToFirst()) {
            do {
                int act_Id = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.ACT_ID.toString()));
                int beat_id = BEAT_ID;
                String action_Data = cursor.getString(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.ACTION_DATA.toString()));
                float joy = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.JOY.toString()));
                float surprise = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.SURPRISE.toString()));
                float fear = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.FEAR.toString()));
                float sadness = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.SADNESS.toString()));
                float anger = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.ANGER.toString()));
                float disgust = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.DISGUST.toString()));
                int startSec = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.StartSec.toString()));
                int endSec = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.EndSec.toString()));

                Beats.add(new BeatsType(act_Id, beat_id, action_Data, joy, surprise, fear, sadness, anger, disgust, startSec, endSec));

            } while (cursor.moveToNext());
        }

        cursor.close();

        return Beats;
    }

    public  Track ReadChoreogramTrack(String TableName) {

        Track audioTrack = null;
        Cursor cursor= DataBases.get(DBKey).ReadDataFromTable("select * from "+TableName);

        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.TRACK_COLUMNS.TRACK_ID.toString()));
                byte[] track = cursor.getBlob(cursor.getColumnIndex(Choreogram_Table_Columns.TRACK_COLUMNS.DATA.toString()));
                audioTrack = new Track(ID, track);
                break;
            } while (cursor.moveToNext());
        }

        cursor.close();

        return audioTrack;
    }


    public  ArrayList<BeatsType> ReadBeatsByActID(String TableName, int ACT_ID) {

        ArrayList<BeatsType> Beats = new ArrayList<BeatsType>();
        Cursor cursor= DataBases.get(DBKey).ReadDataFromTable("select * from "+TableName+" WHERE "+ Choreogram_Table_Columns.BEATS_COLUMNS.ACT_ID.name()+" = '"+(ACT_ID)+"'");

        if (cursor.moveToFirst()) {
            do {

                int act_Id = ACT_ID;
                int beat_id = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.BEAT_ID.toString()));
                String action_Data = cursor.getString(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.ACTION_DATA.toString()));
                float joy = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.JOY.toString()));
                float surprise = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.SURPRISE.toString()));
                float fear = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.FEAR.toString()));
                float sadness = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.SADNESS.toString()));
                float anger = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.ANGER.toString()));
                float disgust = cursor.getFloat(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.DISGUST.toString()));
                int startSec = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.StartSec.toString()));
                int endSec = cursor.getInt(cursor.getColumnIndex(Choreogram_Table_Columns.BEATS_COLUMNS.EndSec.toString()));

                Beats.add(new BeatsType(act_Id, beat_id, action_Data, joy, surprise, fear, sadness, anger, disgust, startSec, endSec));

            } while (cursor.moveToNext());
        }

        cursor.close();

        return Beats;
    }




    public Boolean saveExpression(Expressions_Type Data) {
        String insertSQL = "INSERT INTO " + (CommandStore_Table_Columns.DBTables.EXPRESSIONS.name()) + " (" +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.NAME.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ACTION_DATA.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.JOY.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SURPRISE.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.FEAR.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SADNESS.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ANGER.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.DISGUST.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.EM_SYNTH_ID.name() + ", " +
                CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SOUND_ID.name() +

                ") VALUES ('" + Data.Name + "', '" + Data.Action_Data + "', "+Data.JOY+", "+Data.SURPRISE+", "+Data.FEAR+", "+Data.SADNESS+", "+Data.ANGER+","+Data.DISGUST+","+Data.EM_SYNTH_ID+", '"+Data.SOUND_ID+"' "+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


       // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;

        //return "Expression Saved Successfully!";
    }

    public Expressions_Type readExpression(String ByName) {
        Expressions_Type Saved_Action_Data = new Expressions_Type("", "", (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, -1, "");

        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("SELECT * from " + CommandStore_Table_Columns.DBTables.EXPRESSIONS.name() +
                " WHERE " + CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.NAME.name() + " = '"+ByName+"'"
        );

        if (cursor.moveToFirst()) {
            do {

                String ActionName = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.NAME.toString()));
                String ActionData = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ACTION_DATA.toString()));
                Float Joy = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.JOY.toString()));
                Float Surprise = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SURPRISE.toString()));
                Float Fear = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.FEAR.toString()));
                Float Anger = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ANGER.toString()));
                Float Sadness = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SADNESS.toString()));
                Float Disguest = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.DISGUST.toString()));
                int EmSynthID = cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.EM_SYNTH_ID.toString()));
                String SoundID = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SOUND_ID.toString()));
                Saved_Action_Data = new Expressions_Type(ActionName, ActionData, Joy, Surprise, Fear, Sadness, Anger, Disguest, EmSynthID, SoundID);
                break;
            } while (cursor.moveToNext());
        }

        cursor.close();

        return Saved_Action_Data;
    }

    public Expressions_Type readExpressionByEmotion(Float JOY, Float SURPRISE, Float FEAR, Float ANGER, Float SADNESS, Float DISGUST) {

        Expressions_Type Saved_Action_Data = new Expressions_Type("", "", (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, -1, "");


        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("SELECT * from " + CommandStore_Table_Columns.DBTables.EXPRESSIONS.name() + " order by ABS(JOY-"+JOY+") + ABS(ANGER-"+ANGER+") + ABS(DISGUST-"+DISGUST+") + ABS(SURPRISE-"+SURPRISE+") + ABS(SADNESS-"+SADNESS+") + ABS(FEAR-"+FEAR+") LIMIT 1"
        );


        if (cursor.moveToFirst()) {

            String ActionName = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.NAME.toString()));
            String ActionData = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ACTION_DATA.toString()));
            Float Joy = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.JOY.toString()));
            Float Surprise = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SURPRISE.toString()));
            Float Fear = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.FEAR.toString()));
            Float Anger = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ANGER.toString()));
            Float Sadness = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SADNESS.toString()));
            Float Disguest = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.DISGUST.toString()));
            int EmSynthID = cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.EM_SYNTH_ID.toString()));
            String SoundID = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SOUND_ID.toString()));
            Saved_Action_Data = new Expressions_Type(ActionName, ActionData, Joy, Surprise, Fear, Sadness, Anger, Disguest, EmSynthID, SoundID);


        }
        cursor.close();

        return Saved_Action_Data;

    }



    public int GetLastEmotionID()
    {
        int ID = -1;
        String selectSQL =  "SELECT "+ CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ID.name()+" FROM "+ CommandStore_Table_Columns.DBTables.EXPRESSIONS.name()+" ORDER BY "+ CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ID.name()+" DESC LIMIT 1";

        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable(selectSQL);


        if (cursor.moveToFirst()) {

            ID = cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ID.toString()));
        }

        cursor.close();
        return ID;
    }

    public String DeleteExpression(int ID)
    {
        boolean Status = DataBases.get(DBKey).ExecuteSqlCommand("DELETE from "+ CommandStore_Table_Columns.DBTables.EXPRESSIONS.name()+" WHERE "+ CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ID.name()+" = '"+(ID)+"'");

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }

        return "Deleted Expression Successfully.";
    }

    public ArrayList<Expressions_Type> ReadExpressions(String TableName) {

        ArrayList<Expressions_Type> ExpressionsData = new ArrayList<Expressions_Type>();
        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("select * from "+TableName);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ID.toString()));
            String name = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.NAME.toString()));
            String action_Data = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ACTION_DATA.toString()));
            float joy = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.JOY.toString()));
            float surprise = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SURPRISE.toString()));
            float fear = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.FEAR.toString()));
            float sadness = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SADNESS.toString()));
            float anger = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.ANGER.toString()));
            float disgust = cursor.getFloat(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.DISGUST.toString()));
            int EmSynthID = cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.EM_SYNTH_ID.toString()));
            String SoundID = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EXPRESSIONS_COLUMNS.SOUND_ID.toString()));

            Expressions_Type Expression = new Expressions_Type(id, name, action_Data, joy, surprise, fear, sadness, anger, disgust, EmSynthID, SoundID);
        }

        cursor.close();

        return ExpressionsData;
    }



    public String DeleteEmSynthByEmSythID(int EM_SYNTH_ID)
    {
        boolean Status = DataBases.get(DBKey).ExecuteSqlCommand("DELETE from "+ EmSynth_Table_Columns.DBTables.EM_SYNTH.name()+" WHERE "+ CommandStore_Table_Columns.EM_SYNTH.ID.name()+" = '"+(EM_SYNTH_ID)+"'");

        if(!Status) {return DataBases.get(DBKey).ErrorMessage; }

        return "Deleted EmSynth Successfully.";
    }

    public  boolean SaveEmSynth(String TableName, EM_SYNTH Data) {

        String insertSQL = "INSERT INTO " + (EmSynth_Table_Columns.DBTables.EM_SYNTH.name()) + " (" +
                CommandStore_Table_Columns.EM_SYNTH.NAME.name() + ", " +
                CommandStore_Table_Columns.EM_SYNTH.PATH.name() +

                ") VALUES ('" + Data.Name + "', '" + Data.Path + "'"+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;
    }

    public  EM_SYNTH ReadEmSynthByEmSynthID(int Em_Synth_ID) {

        EM_SYNTH emSynth = new EM_SYNTH("", -1);
        Cursor cursor = DataBases.get(DBKey).ReadDataFromTable("select * from "+ EmSynth_Table_Columns.DBTables.EM_SYNTH.name()+" WHERE "+ CommandStore_Table_Columns.EM_SYNTH.ID.name()+" = '"+(Em_Synth_ID)+"'");

        if (cursor.moveToFirst()) {
            do {

                int ID = cursor.getInt(cursor.getColumnIndex(CommandStore_Table_Columns.EM_SYNTH.ID.toString()));
                String NAME = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EM_SYNTH.NAME.toString()));
                String PATH = cursor.getString(cursor.getColumnIndex(CommandStore_Table_Columns.EM_SYNTH.PATH.toString()));

                emSynth = new EM_SYNTH(NAME, ID, PATH);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return emSynth;
    }


    public Boolean InsertHeartBeat(HEARTBEAT Data) {
        String insertSQL = "INSERT INTO " + (GeneralConsultation_Table_Columns.DBTables.HEARTBEAT.name()) + " (" +
                GeneralConsultation_Table_Columns.HEARTBEAT_COLUMNS.TIMESTAMP.name() + ", " +
                GeneralConsultation_Table_Columns.HEARTBEAT_COLUMNS.VALUE.name() +

                ") VALUES ('" + Data.TIMESTAMP + "', '"+Data.VALUE+"' "+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;

        //return "Expression Saved Successfully!";
    }

    public Boolean InsertPulse(PULSE Data) {
        String insertSQL = "INSERT INTO " + (GeneralConsultation_Table_Columns.DBTables.PULSE.name()) + " (" +
                GeneralConsultation_Table_Columns.PULSE_COLUMNS.TIMESTAMP.name() + ", " +
                GeneralConsultation_Table_Columns.PULSE_COLUMNS.VALUE.name() +

                ") VALUES ('" + Data.TIMESTAMP + "', '"+Data.VALUE+"' "+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;

        //return "Expression Saved Successfully!";
    }

    public Boolean InsertSPO2(SPO2 Data) {
        String insertSQL = "INSERT INTO " + (GeneralConsultation_Table_Columns.DBTables.SPO2.name()) + " (" +
                GeneralConsultation_Table_Columns.SPO2_COLUMNS.TIMESTAMP.name() + ", " +
                GeneralConsultation_Table_Columns.SPO2_COLUMNS.VALUE.name() +

                ") VALUES ('" + Data.TIMESTAMP + "', '"+Data.VALUE+"' "+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;

        //return "Expression Saved Successfully!";
    }

    public Boolean InsertBPM(BPM Data) {
        String insertSQL = "INSERT INTO " + (GeneralConsultation_Table_Columns.DBTables.BPM.name()) + " (" +
                GeneralConsultation_Table_Columns.BPM_COLUMNS.TIMESTAMP.name() + ", " +
                GeneralConsultation_Table_Columns.BPM_COLUMNS.VALUE.name() +

                ") VALUES ('" + Data.TIMESTAMP + "', '"+Data.VALUE+"' "+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;

        //return "Expression Saved Successfully!";
    }

    public Boolean InsertTemperature(TEMPERATURE Data) {
        String insertSQL = "INSERT INTO " + (GeneralConsultation_Table_Columns.DBTables.TEMPERATURE.name()) + " (" +
                GeneralConsultation_Table_Columns.TEMPERATURE_COLUMNS.TIMESTAMP.name() + ", " +
                GeneralConsultation_Table_Columns.TEMPERATURE_COLUMNS.VALUE.name() +

                ") VALUES ('" + Data.TIMESTAMP + "', '"+Data.VALUE+"' "+")";

        Boolean Status = DataBases.get(DBKey).ExecuteSqlCommand(insertSQL);


        // dbHelper.CloseDBConnection();

        if(!Status) {
            //return dbHelper.ErrorMessage;
            return  false;
        }


        return true;

        //return "Expression Saved Successfully!";
    }


}

