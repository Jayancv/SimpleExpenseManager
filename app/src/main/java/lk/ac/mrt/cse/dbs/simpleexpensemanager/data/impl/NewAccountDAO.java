package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Jayan on 12/4/2015.
 */
public class NewAccountDAO implements AccountDAO {

    private Context context;
    public NewAccountDAO(Context context)  {
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        DB_Helper db_helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = db_helper.getReadableDatabase();
        String[] resultString = {
                DB_Helper.COLUMN_NAME_ACCOUNT_NO
        };
        String sortOrder =DB_Helper.COLUMN_NAME_ACCOUNT_NO;
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, resultString,null,null,null,null,sortOrder,null);

        ArrayList<String> results = new ArrayList<>();
        while (c.moveToNext())
        {
            results.add(c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_ACCOUNT_NO)));
        }
        return results;
    }

    @Override
    public List<Account> getAccountsList() {
        DB_Helper db_helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = db_helper.getReadableDatabase();

        String[] projection = {
                DB_Helper.COLUMN_NAME_ACCOUNT_NO,
                DB_Helper.COLUMN_NAME_ACCOUNT_HOLDER_NAME,
                DB_Helper.COLUMN_NAME_BANK_NAME,
                DB_Helper.COLUMN_NAME_BALANCE
        };
        String sortOrder = DB_Helper.COLUMN_NAME_ACCOUNT_NO;
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, projection,null,null,null,null,sortOrder,null);

        ArrayList<Account> results = new ArrayList<>();
        while (c.moveToNext())
        {
            Account ac = new Account(c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_BANK_NAME)),
                    c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(DB_Helper.COLUMN_NAME_BALANCE)));

            results.add(ac);
        }
        return results;


    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {


        DB_Helper db_helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = db_helper.getReadableDatabase();

        String[] projection = {
                DB_Helper.COLUMN_NAME_ACCOUNT_NO,
                DB_Helper.COLUMN_NAME_ACCOUNT_HOLDER_NAME,
                DB_Helper.COLUMN_NAME_BANK_NAME,
                DB_Helper.COLUMN_NAME_BALANCE
        };
        String sortOrder = DB_Helper.COLUMN_NAME_ACCOUNT_NO + " DESC";
        Cursor c = db.query(DB_Helper.TABLE_NAME_ACCOUNT, projection,DB_Helper.COLUMN_NAME_ACCOUNT_NO + "='" + accountNo +"'",null,null,null,sortOrder,null);

        if(c.moveToNext()) {
            Account ac = new Account(c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_BANK_NAME)),
                    c.getString(c.getColumnIndex(DB_Helper.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(DB_Helper.COLUMN_NAME_BALANCE)));

            return  ac;
        }
        else {
            throw new InvalidAccountException("Account number invalid");
        }
    }

    @Override
    public void addAccount(Account account) {
        DB_Helper db_helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_Helper.COLUMN_NAME_ACCOUNT_NO,account.getAccountNo());
        values.put(DB_Helper.COLUMN_NAME_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(DB_Helper.COLUMN_NAME_BANK_NAME,account.getBankName());
        values.put(DB_Helper.COLUMN_NAME_BALANCE,account.getBalance());

        db.insert(DB_Helper.TABLE_NAME_ACCOUNT,null,values); db_helper = DB_Helper.getInstance(context);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        DB_Helper db_helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = db_helper.getWritableDatabase();

        String selection = DB_Helper.COLUMN_NAME_ACCOUNT_NO + "= ?'";
        String[] selectionArgs = { accountNo };
        db.delete(DB_Helper.TABLE_NAME_ACCOUNT,selection,selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account ac = getAccount(accountNo);

        DB_Helper helper = DB_Helper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch (expenseType)
        {
            case EXPENSE:
                values.put(DB_Helper.COLUMN_NAME_BALANCE,ac.getBalance() - amount);
                break;
            case INCOME:
                values.put(DB_Helper.COLUMN_NAME_BALANCE,ac.getBalance() + amount);
                break;
        }

        String selection = DB_Helper.COLUMN_NAME_ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        db.update(DB_Helper.TABLE_NAME_ACCOUNT,values, selection,selectionArgs);


    }
}
