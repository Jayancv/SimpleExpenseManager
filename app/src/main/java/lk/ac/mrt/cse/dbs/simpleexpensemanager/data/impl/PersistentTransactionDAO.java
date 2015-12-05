package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Jayan on 12/5/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private Context context;
    private DB_Helper db_helper;

    public PersistentTransactionDAO(Context context)
    {
        this.context = context;
        db_helper = DB_Helper.getInstance(context);
    }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_Helper.ACCOUNT_NO,accountNo);
        values.put(DB_Helper.EXPENSE_TYPE,expenseType.toString());
        values.put(DB_Helper.AMOUNT,amount);
        values.put(DB_Helper.DATE,dateToString(date, context));

        db.insert(DB_Helper.TABLE_NAME_TRANSACTION,null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogsImpl(null);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return getPaginatedTransactionLogsImpl(String.valueOf(limit));
    }

    private List<Transaction> getPaginatedTransactionLogsImpl(String limit) {
        SQLiteDatabase db = db_helper.getReadableDatabase();

    String[] projection = {
            DB_Helper.ACCOUNT_NO,
            DB_Helper.DATE,
            DB_Helper.EXPENSE_TYPE,
            DB_Helper.AMOUNT
    };
    String sortOrder = DB_Helper.ENTRY_INDEX;
    Cursor c = db.query(DB_Helper.TABLE_NAME_TRANSACTION, projection,null,null,null,null,sortOrder,limit);

    ArrayList<Transaction> results = new ArrayList<>();
    while (c.moveToNext())
    {
        ExpenseType expenseType = ExpenseType.EXPENSE;
        if(ExpenseType.INCOME.toString().equals( c.getString(c.getColumnIndex(DB_Helper.EXPENSE_TYPE))))
        {
            expenseType =ExpenseType.INCOME;
        }
        try {
            String dateString =c.getString(c.getColumnIndex(DB_Helper.DATE));
            Date date = dateFromString(dateString);
            Transaction tr = new Transaction(date,
                    c.getString(c.getColumnIndex(DB_Helper.ACCOUNT_NO)),expenseType,
                    c.getFloat(c.getColumnIndex(DB_Helper.AMOUNT)));
            results.add(tr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return results;
}

    private static String dateToString(Date date, Context context){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        return dateString;
    }

    private static Date dateFromString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = dateFormat.parse(date);
        return strDate;
    }


}
