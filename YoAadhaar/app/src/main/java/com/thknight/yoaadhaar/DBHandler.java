package com.thknight.yoaadhaar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME ="test.db";
    private static final String TABLE_NAME ="address";
    private static final String UID ="uid";
    private static final String PID ="pid";
    private static final String NAME ="name";
    private static final String DOB ="dob";
    private static final String FATHER_NAME ="father";
    private static final String PIN_CODE ="pincode";
    private static final String DIST ="dist";
    private static final String STREET ="street";
    private static final String POST ="post";
    private static final String STATE ="state";
    private static final String SUB_DIST ="subdist";
    private static final String VILL = "vill";
    private static final int DB_VERSION = 1;

    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME  + " ( " + PID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + UID + " VARCHAR ," + NAME + " " + " TEXT ," + DOB + " " + " TEXT ," + FATHER_NAME + " " + " TEXT ,"
                + PIN_CODE + " " +  " TEXT ," + DIST +  " "+ " TEXT, " + STREET + " "+  " TEXT ," + POST + " "+  " TEXT ," + STATE + " "+ " TEXT ," + SUB_DIST + " "+  " TEXT ," + VILL + " "+  " TEXT )";

        sqLiteDatabase.execSQL(query);
        System.out.println(query);

    }

    public boolean addNewAddress(Context context,String uid, String name, String father, String dob, String pincode, String dist, String street, String post, String state, String subdist, String vill){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println(uid + name + father + dob + pincode + dist + street + post + state + subdist + vill);
        if (name.isEmpty() || father.isEmpty() || dob.isEmpty() || pincode.isEmpty() ||dist.isEmpty() || post.isEmpty() || state.isEmpty() || subdist.isEmpty() || vill.isEmpty()){
            Toast.makeText(context, "Data not received", Toast.LENGTH_LONG).show();
        }
        System.out.println("Value" + uid);
        values.put(SUB_DIST,subdist);

        values.put(NAME,name);

        values.put(FATHER_NAME,father);
        values.put(DOB,dob);
        values.put(PIN_CODE,pincode);
        values.put(DIST,dist);
        values.put(STREET,street);
        values.put(POST,post);
        values.put(STATE,state);
        values.put(UID,uid);

        values.put(VILL,vill);

        db.close();
        return true;
    }
    public StringBuilder showAddress(String uids,int index){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_NAME + " where uid = ? ",new String[]{uids});
        StringBuilder stringBuilder = new StringBuilder();
        while (c.moveToNext()){
            stringBuilder.append(c.getString(index));

        }
        c.close();
        db.close();
        return stringBuilder;

    }
    
//    public ArrayList<String> showAddressFromDB(String uids, int index){
//        System.out.println( " uid " +uids + index);
//        String string = "";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.
//
//        Log.d("<<UID>>" , uids);
//        ArrayList array = new ArrayList<String>();
//
//        if (cursor.getCount() ==0){
//            System.out.println("Data not found");
//
//
//
//        }
//        else{
//            while (cursor.moveToPosition(index)){
//                array.add(cursor.getString(index));
//
//
//            }
//        }
//        cursor.close();
//        db.close();
//        return array;
//    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
