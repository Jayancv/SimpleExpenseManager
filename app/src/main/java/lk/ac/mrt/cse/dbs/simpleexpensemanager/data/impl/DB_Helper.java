package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jayan on 12/4/2015.
 */
public class DB_Helper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dbMiniProject.db";
    private static DB_Helper instance = null;


    public static final String TABLE_NAME_ACCOUNT="account";
    public static final String COLUMN_NAME_ACCOUNT_NO = "account_number";
    public static final String COLUMN_NAME_BANK_NAME = "bank_name";
    public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String COLUMN_NAME_BALANCE = "balance";

    public static final String TABLE_NAME_TRANSACTION = "transaction_history";
    public static final String COLUMN_NAME_ENTRY_INDEX = "record_number";
    public static final String COLUMN_NAME_EXPENSE_TYPE = "expense_type";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_AMOUNT = "amount";

    private  static final String SQL_CREATE_ACCOUNT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ACCOUNT +
                    " (" + COLUMN_NAME_ACCOUNT_NO + " VARCHAR(20) PRIMARY KEY, " +
                           COLUMN_NAME_ACCOUNT_HOLDER_NAME  + " VARCHAR(50) NOT NULL, " +
                           COLUMN_NAME_BANK_NAME + " VARCHAR (50) NOT NULL, "+
                           COLUMN_NAME_BALANCE + " DECIMAL(10,2) NOT NULL )";

    private  static final String SQL_CREATE_TRANSACTION ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TRANSACTION +
                            " (" + COLUMN_NAME_ENTRY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                                   COLUMN_NAME_ACCOUNT_NO +" VARCHAR(20) NOT NULL, " +
                                   COLUMN_NAME_DATE+ " DATE NOT NULL, "+
                                   COLUMN_NAME_AMOUNT+ " DECIMAL(10,2) NOT NULL, "+
                                   COLUMN_NAME_EXPENSE_TYPE + " VARCHAR(20) NOT NULL )";

    private static final String SQL_DELETE_ACCOUNT = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCOUNT ;
    private static final String SQL_DELETE_TRANSACTION =  "DROP TABLE IF EXISTS " + TABLE_NAME_TRANSACTION;



    public DB_Helper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static DB_Helper getInstance(Context context)        //For Singleton
    {
        if(instance == null)
            instance = new DB_Helper(context);
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACCOUNT);
        db.execSQL(SQL_CREATE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ACCOUNT);
        db.execSQL(SQL_DELETE_TRANSACTION);
        onCreate(db);
    }

}


