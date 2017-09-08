package teratroopers.companion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.*;
import java.text.*;


/**
 * Created by Prashanth on 31-08-2017.
 */
//04/09 modified

public class mydbhelper extends SQLiteOpenHelper {
    public static final String DATABSE_NAME="student.sqLiteDatabase";
    public static  String TABLE_NAME;
    public static final String cTABLE_NAME="cTABLE";
    public static final String COL1="rollnos";
    public static final String COL2="studnames";
    public static final String CTCOL1="classname";
    public static final String COL3="roll2";
    public static  String date;
    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + date + " string;";
    boolean k=false;


    public mydbhelper(Context context) {
        super(context,DATABSE_NAME, null, 1);
    }
    public SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase=sqLiteDatabase;
        Log.i("info",sqLiteDatabase.toString());
        sqLiteDatabase.execSQL("create table "+cTABLE_NAME+"("+CTCOL1+" TEXT);");
    }

   @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
       Log.i(" before altering","success");
      // String test="ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + date + "  INTEGER DEFAULT 0";
       sqLiteDatabase.execSQL(DATABASE_ALTER_TEAM_1);
       Log.i("after altering","success");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData( int sr, int er) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();

        //contentvalues.put(COL1, classname);
        //contentvalues.put(COL2, roll1);
        //contentvalues.put(COL3, roll2);

        for(int i=sr;i<=er;i++) {
            contentvalues.put(COL1, i);
            long result = sqLiteDatabase.insert(TABLE_NAME, null, contentvalues);
            if (result == -1) {
                k= false;
            } else {
                k= true;
            }
        }
        return k;
    }
    public Cursor getalldata() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + cTABLE_NAME, null);
        return res;

    }
    public Cursor getcname(String cname){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor result=sqLiteDatabase.rawQuery("Select "+COL1+" from "+cname,null);
        return result;
    }

    public boolean dbname(String cname, int sr, int er){   //test.java activity (class add)
        ContentValues contentvalues = new ContentValues();
        sqLiteDatabase=this.getWritableDatabase();
        TABLE_NAME=cname;
        Log.i("tname",TABLE_NAME);

        sqLiteDatabase.execSQL("create table if not exists "+TABLE_NAME+"("+COL1+" INTEGER,"+COL2+ " TEXT);");
        Log.i("table created:",TABLE_NAME);
       // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       // String date = sdf.format(new Date());
       // Log.i("our  date:", date);
        boolean c=checkclassname();
        if(c==true) {
        contentvalues.put(CTCOL1,TABLE_NAME);
        sqLiteDatabase.insert(cTABLE_NAME, null, contentvalues);

            Log.i("class table insertion:", "success");
            k = insertData(sr, er);
            Log.i("our table data:", "success");
            return k;
        }
        else return false;
    }

    public boolean checkclassname(){ //checks class name for existence

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            Log.i("came to checkclassname","success");
            Cursor result = sqLiteDatabase.rawQuery("Select count(*) from " + cTABLE_NAME + " where " + CTCOL1 + "=" + "'"+TABLE_NAME+"'", null);
            result.moveToNext();
            int k=Integer.parseInt(result.getString(0));
            Log.i("value of k:",String.valueOf(k));
        if(k==0)
            return true;
        else
            return false;
    }
    public void alter(String name,String cname)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        TABLE_NAME=cname;
        date=name;

        Cursor res = sqLiteDatabase.rawQuery("PRAGMA table_info("+cname+")",null);
        int i = res.getColumnIndex(name);
        if(i==-1) {

            Log.i("before altering", "success");

            sqLiteDatabase.execSQL("alter table " + cname + " add " + name + " INTEGER");
            Log.i("after altering", "success");
        }
        else
        {
            Log.i("col","is already present");
        }


        }




       // onUpgrade(sqLiteDatabase,1,2);





    public void deleteclass(String classname){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        //sqLiteDatabase.execSQL("delete TABLE IF EXISTS " + classname);
        sqLiteDatabase.delete(classname,null,null);
        sqLiteDatabase.delete(cTABLE_NAME,CTCOL1+"="+"'"+classname+"'",null);
    }





    public void insertattendance(String cname,int a ,int roll){
        TABLE_NAME=cname;
       // ContentValues contentValues = new ContentValues();
        sqLiteDatabase=this.getWritableDatabase();
       // contentValues.put(date,a);
        try {
            Log.i("before inser","before");
            sqLiteDatabase.execSQL("UPDATE " + TABLE_NAME + " SET " + date + "= " + a + " WHERE " + COL1 + " = " + roll);
            Log.i("after inser","after insert");
        }
        catch (Exception e)
        {
            Log.i("error","error");
        }


    }
    public Cursor viewattendance(String cname)
    {
        sqLiteDatabase=this.getWritableDatabase();
        TABLE_NAME=cname;
        try {
            Log.i("in view","viewbefore");
            Cursor result = sqLiteDatabase.rawQuery("Select " + date + " from " + TABLE_NAME, null);
            return result;
        }
        catch (Exception e)
        {
            Log.i("im in exception","exception");
        }
        return null;
    }



}
